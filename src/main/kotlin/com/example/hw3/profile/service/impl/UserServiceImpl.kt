package com.example.hw3.profile.service.impl

import com.example.hw3.profile.integration.ExchangeIntegrator
import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.model.UserStocksBundle
import com.example.hw3.profile.repository.UserRepository
import com.example.hw3.profile.service.UserService
import com.example.hw3.profile.service.UserValidator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserServiceImpl(

    private val userRepository: UserRepository,

    private val exchangeIntegrator: ExchangeIntegrator,

    private val userValidator: UserValidator,
) : UserService {

    override fun addUser(userBody: UserBody): User {
        userValidator.validateAddUser(userBody)

        val user = User(
            UUID.randomUUID().toString(),
            userBody,
        )
        userRepository.addUser(user)

        return user
    }

    override fun updateCurrency(userId: String, currencyDelta: Double) {
        val user = userRepository.getUser(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User $userId was not found")
        userValidator.validateUpdateCurrency(user, currencyDelta)

        userRepository.updateUserCurrency(userId, currencyDelta)
    }

    override fun getUserStocks(userId: String): List<UserStocksBundle> {
        return exchangeIntegrator
            .getUserStocks(userId)
            .map { userStocks ->
                val companyStocks = exchangeIntegrator.getCompanyStocks(userStocks.companyStocksId)
                UserStocksBundle(userStocks, companyStocks)
            }
    }

    override fun getAllCurrency(userId: String): Double {
        val user = userRepository.getUser(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User $userId was not found")

        return getUserStocks(userId)
            .sumOf { it.userStocks.stocksQuantity * it.companyStocks.body.stocksPrice }
            .plus(user.body.currency)
    }

    override fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        val user = userRepository.getUser(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User $userId was not found")
        val companyStocks = exchangeIntegrator.getCompanyStocks(companyStocksId)
        userValidator.validateUpdateUserStocks(user, companyStocks, stocksQuantityDelta)

        exchangeIntegrator.updateUserStocks(userId, companyStocksId, stocksQuantityDelta)
        userRepository.updateUserCurrency(userId, -stocksQuantityDelta * companyStocks.body.stocksPrice)
    }
}
