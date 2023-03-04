package com.example.hw3.exchange.repository

import com.example.hw3.exchange.model.UserStocks

interface UserStocksRepository {

    fun addStocks(userStocks: UserStocks)

    fun updateStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long)

    fun getStocks(userId: String, stocksId: String): UserStocks?

    fun getStocksByUserId(userId: String): List<UserStocks>
}
