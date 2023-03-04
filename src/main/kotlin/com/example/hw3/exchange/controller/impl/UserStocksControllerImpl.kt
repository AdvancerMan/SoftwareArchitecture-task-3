package com.example.hw3.exchange.controller.impl

import com.example.hw3.exchange.controller.UserStocksController
import com.example.hw3.exchange.model.UserStocks
import com.example.hw3.exchange.service.UserStocksService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class UserStocksControllerImpl(

    private val userStocksService: UserStocksService,
) : UserStocksController {

    @Transactional
    override fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        userStocksService.updateUserStocks(userId, companyStocksId, stocksQuantityDelta)
    }

    @Transactional
    override fun getUserStocks(userId: String): List<UserStocks> {
        return userStocksService.getUserStocks(userId)
    }

    @ExceptionHandler(value = [ResponseStatusException::class])
    fun handleExceptions(e: ResponseStatusException): ResponseEntity<String> {
        return ResponseEntity("Error occurred: ${e.message}", e.status)
    }
}