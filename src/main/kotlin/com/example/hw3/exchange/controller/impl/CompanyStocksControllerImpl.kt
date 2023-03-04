package com.example.hw3.exchange.controller.impl

import com.example.hw3.exchange.controller.CompanyStocksController
import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import com.example.hw3.exchange.service.CompanyStocksService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class CompanyStocksControllerImpl(

    private val companyStocksService: CompanyStocksService,
) : CompanyStocksController {

    @Transactional
    override fun addCompanyStocks(companyStocksBody: CompanyStocksBody): CompanyStocks {
        return companyStocksService.addCompanyStocks(companyStocksBody)
    }

    @Transactional
    override fun updateCompanyStocks(companyStocks: CompanyStocks) {
        companyStocksService.updateCompanyStocks(companyStocks)
    }

    @Transactional
    override fun getCompanyStocks(companyStocksId: String): CompanyStocks? {
        return companyStocksService.getCompanyStocks(companyStocksId)
    }

    @ExceptionHandler(value = [ResponseStatusException::class])
    fun handleExceptions(e: ResponseStatusException): ResponseEntity<String> {
        return ResponseEntity("Error occurred: ${e.message}", e.status)
    }
}