package com.example.hw3.exchange.repository.impl

import com.example.hw3.exchange.model.CompanyStocks
import com.example.hw3.exchange.model.CompanyStocksBody
import com.example.hw3.exchange.repository.CompanyStocksRepository
import com.example.hw3.exchange.repository.Migration
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class CompanyStocksRepositoryImpl(

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    dataSource: DataSource,
) : JdbcDaoSupport(), CompanyStocksRepository, Migration {

    init {
        setDataSource(dataSource)
    }

    override fun migrate() {
        val sql = "create table if not exists company_stocks (" +
                "id varchar(128), " +
                "stocks_quantity int, " +
                "stocks_price int " +
                ");"
        jdbcTemplate!!.execute(sql)
    }

    override fun addCompanyStocks(companyStocks: CompanyStocks) {
        val sql = "insert into company_stocks (id, stocks_quantity, stocks_price) " +
                "values (?, ?, ?);"
        with(companyStocks) {
            jdbcTemplate!!.update(sql, id, body.stocksQuantity, body.stocksPrice)
        }
    }

    override fun updateCompanyStocks(companyStocks: CompanyStocks) {
        val sql = "update company_stocks " +
                "set stocks_quantity = ?, " +
                "stocks_price = ? " +
                "where id = ?;"
        with(companyStocks) {
            jdbcTemplate!!.update(sql, body.stocksQuantity, body.stocksPrice, id)
        }
    }

    override fun getCompanyStocks(companyStocksId: String): CompanyStocks? {
        val sql = "select id, stocks_quantity, stocks_price " +
                "from company_stocks " +
                "where id = ?;"

        val record = jdbcTemplate!!.query(
            sql,
            DataClassRowMapper(CompanyStocksRecord::class.java),
            companyStocksId,
        )

        return record.firstOrNull()?.let { mapToModel(it) }
    }

    private fun mapToModel(record: CompanyStocksRecord): CompanyStocks {
        return CompanyStocks(
            record.id,
            CompanyStocksBody(
                record.stocksQuantity,
                record.stocksPrice,
            )
        )
    }

    private data class CompanyStocksRecord(
        val id: String,
        val stocksQuantity: Long,
        val stocksPrice: Double,
    )
}
