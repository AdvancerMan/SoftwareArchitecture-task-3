package com.example.hw3.profile.model

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.UserStocks

data class UserStocksBundle(
    val userStocks: UserStocks,
    val companyStocks: CompanyStocks,
)
