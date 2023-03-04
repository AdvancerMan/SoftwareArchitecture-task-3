package com.example.hw3.exchange.service.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.service.CompanyStocksValidator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CompanyStocksValidatorImpl : CompanyStocksValidator {

    override fun validateCompanyStocks(companyStocks: CompanyStocks) {
        if (companyStocks.body.stocksPrice < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "stocksPrice < 0")
        }
        if (companyStocks.body.stocksQuantity < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "stocksQuantity < 0")
        }
    }
}
