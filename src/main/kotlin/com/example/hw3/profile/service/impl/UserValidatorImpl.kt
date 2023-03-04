package com.example.hw3.profile.service.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.service.UserValidator
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserValidatorImpl : UserValidator {

    override fun validateAddUser(userBody: UserBody) {
        if (userBody.currency < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "userBody.currency < 0")
        }
    }

    override fun validateUpdateCurrency(user: User, currencyDelta: Double) {
        if (user.body.currency + currencyDelta < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "user.body.currency + currencyDelta < 0")
        }
    }

    override fun validateUpdateUserStocks(user: User, companyStocks: CompanyStocks, stocksQuantityDelta: Long) {
        if (companyStocks.body.stocksQuantity + stocksQuantityDelta < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "companyStocks.body.stocksQuantity + stocksQuantityDelta < 0")
        }
        validateUpdateCurrency(user, -stocksQuantityDelta * companyStocks.body.stocksPrice)
    }
}
