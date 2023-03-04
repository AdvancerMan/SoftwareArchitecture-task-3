package com.example.hw3.exchange.service.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import com.example.hw3.exchange.repository.CompanyStocksRepository
import com.example.hw3.exchange.service.CompanyStocksService
import com.example.hw3.exchange.service.CompanyStocksValidator
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CompanyStocksServiceImpl(

    private val companyStocksRepository: CompanyStocksRepository,

    private val companyStocksValidator: CompanyStocksValidator,
) : CompanyStocksService {

    override fun addCompanyStocks(companyStocksBody: CompanyStocksBody): CompanyStocks {
        val companyStocks = CompanyStocks(
            UUID.randomUUID().toString(),
            companyStocksBody,
        )
        companyStocksValidator.validateCompanyStocks(companyStocks)
        companyStocksRepository.addCompanyStocks(companyStocks)

        return companyStocks
    }

    override fun updateCompanyStocks(companyStocks: CompanyStocks) {
        companyStocksValidator.validateCompanyStocks(companyStocks)
        companyStocksRepository.updateCompanyStocks(companyStocks)
    }

    override fun getCompanyStocks(companyStocksId: String): CompanyStocks? {
        return companyStocksRepository.getCompanyStocks(companyStocksId)
    }
}
