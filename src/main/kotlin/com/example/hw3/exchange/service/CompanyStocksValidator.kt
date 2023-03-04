package com.example.hw3.exchange.service

import com.example.hw3.exchange.model.CompanyStocks

interface CompanyStocksValidator {

    fun validateCompanyStocks(companyStocks: CompanyStocks)
}
