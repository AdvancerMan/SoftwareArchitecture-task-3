package com.example.hw3.exchange

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import java.time.Clock

@SpringBootApplication
@PropertySource("classpath:application-exchange.properties")
class ExchangeApplication {

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}

fun main(args: Array<String>) {
    runApplication<ExchangeApplication>(*args)
}
