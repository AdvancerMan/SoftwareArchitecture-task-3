package com.example.hw3.profile.repository

import com.example.hw3.profile.model.User

interface UserRepository {

    fun addUser(user: User)

    fun updateUserCurrency(userId: String, currencyDelta: Double)

    fun getUser(userId: String): User?
}
