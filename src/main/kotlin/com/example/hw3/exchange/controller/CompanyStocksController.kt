package com.example.hw3.exchange.controller

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@RequestMapping(value = ["/company/stocks"])
interface CompanyStocksController {

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addCompanyStocks(@RequestBody companyStocksBody: CompanyStocksBody): CompanyStocks

    @RequestMapping(value = ["/update"], method = [RequestMethod.POST])
    fun updateCompanyStocks(@RequestBody companyStocks: CompanyStocks)

    @RequestMapping(value = ["/{companyStocksId}"], method = [RequestMethod.GET])
    fun getCompanyStocks(@PathVariable companyStocksId: String): CompanyStocks?
}
