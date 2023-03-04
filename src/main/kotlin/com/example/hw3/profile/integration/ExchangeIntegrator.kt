package com.example.hw3.profile.integration

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import com.example.hw3.exchange.model.UserStocks

interface ExchangeIntegrator {

    fun addCompanyStocks(companyStocksBody: CompanyStocksBody): CompanyStocks

    fun updateCompanyStocks(companyStocks: CompanyStocks)

    fun getCompanyStocks(companyStocksId: String): CompanyStocks

    fun updateUserStocks(
        userId: String,
        companyStocksId: String,
        stocksQuantityDelta: Long,
    )

    fun getUserStocks(userId: String): List<UserStocks>
}