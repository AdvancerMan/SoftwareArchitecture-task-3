package com.example.hw3.exchange.service

interface UserStocksService {

    fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long)
}