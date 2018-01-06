package com.chitzkoy.financepmreporter.server

import com.chitzkoy.financepmreporter.model.dao.Configs
import com.chitzkoy.financepmreporter.model.dao.RegularCategories
import com.chitzkoy.financepmreporter.util.*
import org.h2.jdbc.JdbcSQLException
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

/**
 * Created by dtikhonov on 15-Nov-17.
 */

class Database {
    companion object {
        val log by logger()
    }

    private val dataSource : DataSource = JdbcDataSource()

    init {
        if (dataSource is JdbcDataSource) {
            dataSource.setUrl(Property["datasource.url"])
            dataSource.user = Property["datasource.username"]
            dataSource.password = Property["datasource.password"]
        }

        Database.connect(dataSource)
        initSchema()
        log.info("Database initialized")
    }

    fun initSchema() {
        transaction {
            create(
                    TransactionTypesLoader,
                    SourcesLoader,
                    CategoriesLoader,
                    CurrenciesLoader,
                    AccountsLoader,
                    TransactionsLoader,
                    TransfersLoader,
                    Configs,
                    RegularCategories
            )

            try {
                TransactionTypesLoader.insert { it[id] = 1; it[name] = "Доход" }
                TransactionTypesLoader.insert { it[id] = 2; it[name] = "Расход" }

                SourcesLoader.insert { it[id] = 1; it[name] = "Перевод" }
                SourcesLoader.insert { it[id] = 2; it[name] = "Создание долга" }
                SourcesLoader.insert { it[id] = 3; it[name] = "Погашение долга" }
                SourcesLoader.insert { it[id] = 4; it[name] = "Увеличение долга" }
                SourcesLoader.insert { it[id] = 5; it[name] = "Правка баланса" }

                Configs.batchInsert(defaultConfig()) { config ->
                    this[Configs.param] = config.param
                    this[Configs.value] = config.value
                }

            } catch (ignored : JdbcSQLException) {
                log.warn("Schema already initialized")
            }
        }
    }

}