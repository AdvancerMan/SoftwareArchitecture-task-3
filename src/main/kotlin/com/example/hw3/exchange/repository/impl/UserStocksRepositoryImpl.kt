package com.example.hw3.exchange.repository.impl

import com.example.hw3.exchange.model.UserStocks
import com.example.hw3.exchange.repository.Migration
import com.example.hw3.exchange.repository.UserStocksRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class UserStocksRepositoryImpl(

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    dataSource: DataSource,
) : JdbcDaoSupport(), UserStocksRepository, Migration {

    init {
        setDataSource(dataSource)
    }

    override fun migrate() {
        val sql = "create table if not exists user_stocks (" +
                "user_id varchar(128), " +
                "company_stocks_id varchar(128), " +
                "stocks_quantity int" +
                ");"
        jdbcTemplate!!.execute(sql)
    }

    override fun addStocks(userStocks: UserStocks) {
        val sql = "insert into user_stocks (user_id, company_stocks_id, stocks_quantity) " +
                "values (?, ?, ?);"

        with(userStocks) {
            jdbcTemplate!!.update(sql, userId, companyStocksId, stocksQuantity)
        }
    }

    override fun updateStocks(userId: String, companyStocksId: String, stocksQuantityDelta: Long) {
        val sql = "update user_stocks " +
                "set stocks_quantity = stocks_quantity + ? " +
                "where user_id = ? " +
                "and company_stocks_id = ?;"

        jdbcTemplate!!.update(sql, stocksQuantityDelta, userId, companyStocksId)
    }

    override fun getStocks(userId: String, stocksId: String): UserStocks? {
        val sql = "select user_id, company_stocks_id, stocks_quantity " +
                "from user_stocks " +
                "where user_id = ? " +
                "and company_stocks_id = ?;"

        return jdbcTemplate!!.query(
            sql,
            DataClassRowMapper(UserStocks::class.java),
            userId,
            stocksId,
        )
            .firstOrNull()
    }

    override fun getStocksByUserId(userId: String): List<UserStocks> {
        val sql = "select user_id, company_stocks_id, stocks_quantity " +
                "from user_stocks " +
                "where user_id = ?;"

        return jdbcTemplate!!.query(
            sql,
            DataClassRowMapper(UserStocks::class.java),
            userId,
        )
    }
}
