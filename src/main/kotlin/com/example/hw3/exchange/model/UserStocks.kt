package com.example.hw3.exchange.model

data class UserStocks(
    val userId: String,
    val companyStocksId: String,
    val stocksQuantity: Long,
)
