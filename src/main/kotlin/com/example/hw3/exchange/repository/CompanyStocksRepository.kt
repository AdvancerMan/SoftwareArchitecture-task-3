package com.example.hw3.exchange.repository

import com.example.hw3.exchange.model.CompanyStocks

interface CompanyStocksRepository {

    fun addCompanyStocks(companyStocks: CompanyStocks)

    fun updateCompanyStocks(companyStocks: CompanyStocks)

    fun getCompanyStocks(companyStocksId: String): CompanyStocks?
}
