package com.example.hw3.exchange.service

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.UserStocks

interface UserStocksValidator {

    fun validateTakeStocksFrom(companyStocks: CompanyStocks, stocksToTake: Long)

    fun validateAddStocks(userStocks: UserStocks)

    fun validateUpdateStocks(userStocks: UserStocks, stocksQuantityDelta: Long)
}