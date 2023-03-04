package com.example.hw3.profile.controller.impl

import com.example.hw3.profile.controller.UserController
import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.model.UserStocksBundle
import com.example.hw3.profile.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class UserControllerImpl(

    private val userService: UserService,
) : UserController {

    @Transactional
    override fun addUser(userBody: UserBody): User {
        return userService.addUser(userBody)
    }

    @Transactional
    override fun updateCurrency(userId: String, currencyDelta: Double) {
        return userService.updateCurrency(userId, currencyDelta)
    }

    @Transactional
    override fun getUserStocks(userId: String): List<UserStocksBundle> {
        return userService.getUserStocks(userId)
    }

    @Transactional
    override fun getAllCurrency(userId: String): Double {
        return userService.getAllCurrency(userId)
    }

    @Transactional
    override fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        return userService.updateUserStocks(userId, companyStocksId, stocksQuantityDelta)
    }

    @ExceptionHandler(value = [ResponseStatusException::class])
    fun handleExceptions(e: ResponseStatusException): ResponseEntity<String> {
        return ResponseEntity("Error occurred: ${e.message}", e.status)
    }
}
