package com.example.hw3.profile.service

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody

interface UserValidator {

    fun validateAddUser(userBody: UserBody)

    fun validateUpdateCurrency(user: User, currencyDelta: Double)

    fun validateUpdateUserStocks(user: User, companyStocks: CompanyStocks, stocksQuantityDelta: Long)
}
