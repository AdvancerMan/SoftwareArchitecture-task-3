package com.example.hw3.profile.controller

import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.model.UserStocksBundle
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping(value = ["/user/"])
interface UserController {

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addUser(@RequestBody userBody: UserBody): User

    @RequestMapping(value = ["/{userId}/currency/update"], method = [RequestMethod.POST])
    fun updateCurrency(@PathVariable userId: String, @RequestParam currencyDelta: Double)

    @RequestMapping(value = ["/{userId}/stocks"], method = [RequestMethod.GET])
    fun getUserStocks(@PathVariable userId: String): List<UserStocksBundle>

    @RequestMapping(value = ["/{userId}/currency/all"], method = [RequestMethod.GET])
    fun getAllCurrency(@PathVariable userId: String): Double

    @RequestMapping(value = ["/{userId}/stocks/{companyStocksId}/update"], method = [RequestMethod.POST])
    fun updateUserStocks(
        @PathVariable userId: String,
        @PathVariable companyStocksId: String,
        @RequestParam stocksQuantityDelta: Long,
    )
}
