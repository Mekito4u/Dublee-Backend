package com.data.database

import com.data.database.tables.LikesTable
import com.data.database.tables.MatchesTable
import com.data.database.tables.PairsTable
import com.data.database.tables.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/dublee"
        username = "postgres"
        password = ""
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 10
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(UsersTable, PairsTable, LikesTable, MatchesTable)
        println("+ Tables created")
    }
}