package com.example.hw3.profile.repository.impl

import com.example.hw3.profile.repository.Migration
import com.example.hw3.profile.model.User
import com.example.hw3.profile.model.UserBody
import com.example.hw3.profile.repository.UserRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.support.JdbcDaoSupport
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class UserRepositoryImpl(

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    dataSource: DataSource,
) : JdbcDaoSupport(), UserRepository, Migration {

    init {
        setDataSource(dataSource)
    }

    override fun migrate() {
        val sql = "create table if not exists user (" +
                "id varchar(128), " +
                "currency int" +
                ");"
        jdbcTemplate!!.execute(sql)
    }

    override fun addUser(user: User) {
        val sql = "insert into user (id, currency) " +
                "values (?, ?);"

        with(user) {
            jdbcTemplate!!.update(sql, id, body.currency)
        }
    }

    override fun updateUserCurrency(userId: String, currencyDelta: Double) {
        val sql = "update user " +
                "set currency = currency + ? " +
                "where id = ?;"

        jdbcTemplate!!.update(sql, currencyDelta, userId)
    }

    override fun getUser(userId: String): User? {
        val sql = "select id, currency " +
                "from user " +
                "where id = ?;"

        return jdbcTemplate!!.query(
            sql,
            DataClassRowMapper(UserRecord::class.java),
            userId,
        )
            .firstOrNull()
            ?.let { mapToModel(it) }
    }

    private fun mapToModel(record: UserRecord): User {
        return User(
            record.id,
            UserBody(
                record.currency,
            )
        )
    }

    private data class UserRecord(
        val id: String,
        val currency: Double,
    )
}