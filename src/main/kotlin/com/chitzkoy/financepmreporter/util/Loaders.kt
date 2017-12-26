package com.chitzkoy.financepmreporter.util

import org.jetbrains.exposed.sql.Table

/**
 * Created by dtikhonov on 16-Nov-17.
 */
object TransactionTypesLoader : Table("TransactionType") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
}

object SourcesLoader : Table("Source") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
}

object CategoriesLoader : Table("Category") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
    val type = (integer("type") references TransactionTypesLoader.id)
    val parentId = (integer("parentId") references id).nullable()
    val orderId = integer("orderId")
}

object CurrenciesLoader : Table("Currency") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
    val shortName = varchar("shortName", length = 5)
}

object AccountsLoader : Table("Account") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
    val balance = decimal("balance", 10, 2)
    val currencyId = (integer("currencyId") references CurrenciesLoader.id)
    val active = bool("active")
}

object TransactionsLoader : Table("Transaction") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
    val type = (integer("type") references TransactionTypesLoader.id)
    val categoryId = (integer("categoryId") references CategoriesLoader.id).nullable()
    val date = datetime("date")
    val sum = decimal("sum", 10, 2)
    val accountId = integer("accountId") references AccountsLoader.id
    val description = varchar("description", length = 120)
    val sourceId = (integer("sourceId") references SourcesLoader.id).nullable()
}

object TransfersLoader : Table("Transfer") {
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
    val from = (integer("transactionIdFrom") references TransactionsLoader.id)
    val to = (integer("transactionIdTo") references TransactionsLoader.id)
}