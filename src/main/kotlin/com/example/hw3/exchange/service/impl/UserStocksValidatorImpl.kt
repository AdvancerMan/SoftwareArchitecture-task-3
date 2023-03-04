package com.example.hw3.exchange.service.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.UserStocks
import com.example.hw3.exchange.service.UserStocksValidator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserStocksValidatorImpl : UserStocksValidator {

    override fun validateTakeStocksFrom(companyStocks: CompanyStocks, stocksToTake: Long) {
        if (companyStocks.body.stocksQuantity - stocksToTake < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "companyStocks.body.stocksQuantity - stocksToTake < 0")
        }
    }

    override fun validateAddStocks(userStocks: UserStocks) {
        if (userStocks.stocksQuantity < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "userStocks.stocksQuantity < 0")
        }
    }

    override fun validateUpdateStocks(userStocks: UserStocks, stocksQuantityDelta: Long) {
        if (userStocks.stocksQuantity + stocksQuantityDelta < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "userStocks.stocksQuantity + stocksQuantityDelta < 0")
        }
    }
}
