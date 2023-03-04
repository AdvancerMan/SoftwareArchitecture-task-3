package com.example.hw3.exchange.service

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody

interface CompanyStocksService {

    fun addCompanyStocks(companyStocksBody: CompanyStocksBody): CompanyStocks

    fun updateCompanyStocks(companyStocks: CompanyStocks)

    fun getCompanyStocks(companyStocksId: String): CompanyStocks?
}