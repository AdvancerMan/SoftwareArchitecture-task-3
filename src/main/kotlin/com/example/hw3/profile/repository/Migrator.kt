package com.example.hw3.profile.repository

import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class Migrator(

    private val migrations: List<Migration>,
) {

    @PostConstruct
    fun init() {
        migrations.forEach { it.migrate() }
    }
}
