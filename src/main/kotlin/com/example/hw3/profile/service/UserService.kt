package com.example.hw3.profile.service

import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.model.UserStocksBundle

interface UserService {

    fun addUser(userBody: UserBody): User

    fun updateCurrency(userId: String, currencyDelta: Double)

    fun getUserStocks(userId: String): List<UserStocksBundle>

    fun getAllCurrency(userId: String): Double

    fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long)
}
