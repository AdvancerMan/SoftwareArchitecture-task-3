package com.example.hw3.exchange.controller

import com.example.hw3.exchange.model.UserStocks
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping(value = ["/user/stocks"])
interface UserStocksController {

    @RequestMapping(value = ["/{userId}/{companyStocksId}"], method = [RequestMethod.POST])
    fun updateUserStocks(
        @PathVariable userId: String,
        @PathVariable companyStocksId: String,
        @RequestParam stocksQuantityDelta: Long,
    )

    @RequestMapping(value = ["/{userId}"], method = [RequestMethod.GET])
    fun getUserStocks(
        @PathVariable userId: String,
    ): List<UserStocks>
}
