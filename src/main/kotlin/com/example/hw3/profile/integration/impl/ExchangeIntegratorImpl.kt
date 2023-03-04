package com.example.hw3.profile.integration.impl

import com.example.hw3.exchange.model.CompanyStocks
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

    private fun postForData(path: String, data: Any?) {
        val url = "$exchangeHost/$path"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val json = JSON_MAPPER.writeValueAsString(data)
        val httpEntity = HttpEntity(json, headers)
        restTemplate.postForEntity<Unit>(url, httpEntity)
    }

    private fun getData(path: String): JsonNode {
        val url = "$exchangeHost/$path"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity<JsonNode>(headers)
        return restTemplate.getForEntity<JsonNode>(url, httpEntity)
            .body
            ?: JSON_MAPPER.readTree("{}")
    }

    override fun getCompanyStocks(companyStocksId: String): CompanyStocks {
        val json = getData("/company/stocks/$companyStocksId")
        return JSON_MAPPER.treeToValue(json)
    }

    override fun updateUserStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        postForData("/user/stocks/$userId/$companyStocksId?stocksQuantityDelta=$stocksQuantityDelta", null)
    }

    override fun getUserStocks(userId: String): List<UserStocks> {
        val json = getData("/user/stocks/$userId")
        return JSON_MAPPER.treeToValue(json)
    }
}
