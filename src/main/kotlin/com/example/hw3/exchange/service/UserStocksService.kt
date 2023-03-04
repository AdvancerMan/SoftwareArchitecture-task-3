package com.example.hw3.exchange.service

import com.example.hw3.exchange.model.UserStocks

interface UserStocksService {

    fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long)

    fun getUserStocks(userId: String): List<UserStocks>
}