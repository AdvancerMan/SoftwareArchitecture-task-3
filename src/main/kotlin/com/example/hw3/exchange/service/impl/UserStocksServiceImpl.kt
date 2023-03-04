package com.example.hw3.exchange.service.impl

import com.example.hw3.exchange.model.UserStocks
import com.example.hw3.exchange.repository.CompanyStocksRepository
import com.example.hw3.exchange.repository.UserStocksRepository
import com.example.hw3.exchange.service.UserStocksService
import com.example.hw3.exchange.service.UserStocksValidator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserStocksServiceImpl(

    private val userStocksRepository: UserStocksRepository,

    private val companyStocksRepository: CompanyStocksRepository,

    private val userStocksValidator: UserStocksValidator,
) : UserStocksService {

    override fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        takeStocksFromCompany(companyStocksId, stocksQuantityDelta)

        val existingStocks = userStocksRepository.getStocks(userId, companyStocksId)

        if (existingStocks == null) {
            val userStocks = UserStocks(userId, companyStocksId, stocksQuantityDelta)
            userStocksValidator.validateAddStocks(userStocks)
            userStocksRepository.addStocks(userStocks)
            return
        }

        userStocksValidator.validateUpdateStocks(existingStocks, stocksQuantityDelta)
        userStocksRepository.updateStocks(userId, companyStocksId, stocksQuantityDelta)
    }

    private fun takeStocksFromCompany(companyStocksId: String, stocksQuantityDelta: Long) {
        val companyStocks = companyStocksRepository.getCompanyStocks(companyStocksId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Company stocks $companyStocksId were not found")

        userStocksValidator.validateTakeStocksFrom(companyStocks, stocksQuantityDelta)

        val updatedCompanyStocks = companyStocks.copy(
            body = companyStocks.body.copy(
                stocksQuantity = companyStocks.body.stocksQuantity - stocksQuantityDelta,
            ),
        )

        companyStocksRepository.updateCompanyStocks(updatedCompanyStocks)
    }
}
