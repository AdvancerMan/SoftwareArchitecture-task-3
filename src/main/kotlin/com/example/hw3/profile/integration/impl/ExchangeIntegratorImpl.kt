package com.example.hw3.profile.integration.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import com.example.hw3.exchange.model.UserStocks
import com.example.hw3.profile.integration.ExchangeIntegrator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity

@Service
class ExchangeIntegratorImpl(

    @Value("\${exchange.host}")
    private val exchangeHost: String,
) : ExchangeIntegrator {

    companion object {
        private val JSON_MAPPER = JsonMapper()
            .registerModule(
                KotlinModule.Builder()
                    .configure(KotlinFeature.SingletonSupport, true)
                    .build())
    }

    private inline fun <reified R> postForData(path: String, data: Any?): R {
        val url = "$exchangeHost/$path"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val json = JSON_MAPPER.writeValueAsString(data)
        val httpEntity = HttpEntity(json, headers)
        val responseJson = restTemplate.postForEntity<JsonNode>(url, httpEntity)
            .body
            ?: JSON_MAPPER.readTree("{}")

        return JSON_MAPPER.treeToValue(responseJson)
    }

    private inline fun <reified R> getData(path: String): R {
        val url = "$exchangeHost/$path"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity<JsonNode>(headers)
        val responseJson = restTemplate.getForEntity<JsonNode>(url, httpEntity)
            .body
            ?: JSON_MAPPER.readTree("{}")

        return JSON_MAPPER.treeToValue(responseJson)
    }

    override fun addCompanyStocks(companyStocksBody: CompanyStocksBody): CompanyStocks {
        return postForData("/company/stocks/add", companyStocksBody)
    }

    override fun updateCompanyStocks(companyStocks: CompanyStocks) {
        postForData<Unit>("/company/stocks/update", companyStocks)
    }

    override fun getCompanyStocks(companyStocksId: String): CompanyStocks {
        return getData("/company/stocks/$companyStocksId")
    }

    override fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        postForData<Unit>("/user/stocks/$userId/$companyStocksId?stocksQuantityDelta=$stocksQuantityDelta", null)
    }

    override fun getUserStocks(userId: String): List<UserStocks> {
        return getData("/user/stocks/$userId")
    }
}
