package com.example.hw3.profile.service.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import com.example.hw3.exchange.model.UserStocks
import com.example.hw3.profile.integration.ExchangeIntegrator
import com.example.hw3.profile.integration.impl.ExchangeIntegratorImpl
import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.model.UserStocksBundle
import com.example.hw3.profile.repository.UserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.HttpClientErrorException
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@RunWith(SpringRunner::class)
class UserServiceImplTest {

    lateinit var exchangeIntegrator: ExchangeIntegrator

    lateinit var userServiceImpl: UserServiceImpl

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var companyStocks1: CompanyStocks

    lateinit var companyStocks2: CompanyStocks

    lateinit var user: User

    @JvmField
    @Rule
    final val exchangeApplication: GenericContainer<*> = GenericContainer(DockerImageName.parse("exchange-application:latest"))
        .withExposedPorts(8080)

    @Before
    fun setUp() {
        exchangeIntegrator = ExchangeIntegratorImpl("http://localhost:${exchangeApplication.firstMappedPort}")
        companyStocks1 = exchangeIntegrator.addCompanyStocks(CompanyStocksBody(10, 10.0))
        companyStocks2 = exchangeIntegrator.addCompanyStocks(CompanyStocksBody(1, 1.0))

        userServiceImpl = UserServiceImpl(
            userRepository,
            exchangeIntegrator,
            mock(),
        )

        val userBody = UserBody(123456.0)
        user = userServiceImpl.addUser(userBody)
    }

    @Test
    fun testAddUser() {
        val actualUser = userRepository.getUser(user.id)
        Assert.assertEquals(user, actualUser)
    }

    @Test
    fun testUpdateCurrency() {
        userServiceImpl.updateCurrency(user.id, 1.1)
        val actualCurrency = userRepository.getUser(user.id)!!.body.currency
        Assert.assertEquals(user.body.currency + 1.1, actualCurrency, 0.0)
    }

    @Test
    fun testUpdateUserStocks() {
        userServiceImpl.updateUserStocks(user.id, companyStocks1.id, 1)

        val expectedUserStocks = setOf(
            UserStocksBundle(
                UserStocks(user.id, companyStocks1.id, 1),
                companyStocks1.copy(
                    body = companyStocks1.body.copy(
                        stocksQuantity = companyStocks1.body.stocksQuantity - 1
                    )
                ),
            )
        )

        val actualUserStocks = userServiceImpl.getUserStocks(user.id).toSet()
        Assert.assertEquals(expectedUserStocks, actualUserStocks)
    }

    @Test
    fun testUpdateUserManyStocks() {
        userServiceImpl.updateUserStocks(user.id, companyStocks1.id, 5)
        userServiceImpl.updateUserStocks(user.id, companyStocks2.id, 1)

        val expectedUserStocks = setOf(
            UserStocksBundle(
                UserStocks(user.id, companyStocks1.id, 5),
                companyStocks1.copy(
                    body = companyStocks1.body.copy(
                        stocksQuantity = companyStocks1.body.stocksQuantity - 5
                    )
                ),
            ),
            UserStocksBundle(
                UserStocks(user.id, companyStocks2.id, 1),
                companyStocks2.copy(
                    body = companyStocks2.body.copy(
                        stocksQuantity = companyStocks2.body.stocksQuantity - 1
                    )
                ),
            ),
        )

        val actualUserStocks = userServiceImpl.getUserStocks(user.id).toSet()
        Assert.assertEquals(expectedUserStocks, actualUserStocks)
    }

    @Test(expected = HttpClientErrorException.BadRequest::class)
    fun testInvalidUpdateUserStocks() {
        userServiceImpl.updateUserStocks(user.id, companyStocks2.id, 2)
    }

    @Test
    fun testGetAllCurrency() {
        val currency1 = userServiceImpl.getAllCurrency(user.id)
        Assert.assertEquals(user.body.currency, currency1, 0.0)

        userServiceImpl.updateUserStocks(user.id, companyStocks1.id, 5)
        userServiceImpl.updateUserStocks(user.id, companyStocks2.id, 1)

        val currency2 = userServiceImpl.getAllCurrency(user.id)
        Assert.assertEquals(user.body.currency, currency2, 0.0)
    }

    @Test
    fun testGetAllCurrencyWhenStocksRise() {
        userServiceImpl.updateUserStocks(user.id, companyStocks1.id, 5)
        userServiceImpl.updateUserStocks(user.id, companyStocks2.id, 1)
        val companyStocks1Updated = companyStocks1.copy(
            body = companyStocks1.body.copy(
                stocksPrice = companyStocks1.body.stocksPrice + 5.0,
            ),
        )
        val companyStocks2Updated = companyStocks2.copy(
            body = companyStocks2.body.copy(
                stocksPrice = companyStocks2.body.stocksPrice + 22.0,
            ),
        )

        exchangeIntegrator.updateCompanyStocks(companyStocks1Updated)
        exchangeIntegrator.updateCompanyStocks(companyStocks2Updated)

        val currency = userServiceImpl.getAllCurrency(user.id)
        Assert.assertEquals(user.body.currency + 5 * 5 + 1 * 22, currency, 0.0)
    }
}
