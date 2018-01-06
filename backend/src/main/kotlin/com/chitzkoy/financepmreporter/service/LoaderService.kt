package com.chitzkoy.financepmreporter.service

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.chitzkoy.financepmreporter.util.*
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.math.BigDecimal


/**
 * Created by dtikhonov on 22-Oct-17.
 */

class LoaderService {
    companion object {
        val log by logger()
    }

    private val listToMerge = mutableListOf<JsonObject>()

    fun addToMerge(data: JsonObject) {
        listToMerge.add(data)
    }

    fun commitMerge(load: Boolean): JsonObject? {
        val iterator = listToMerge.iterator()
        var data1 = if (iterator.hasNext()) iterator.next() else return null

        while (iterator.hasNext()) {
            val data2 = iterator.next()

            data1 = merge(data1, data2)
        }

        if (load) {
            clearDatabase()
            load(data1)
        }
        listToMerge.clear()

        return data1
    }

    private fun merge(data1: JsonObject, data2: JsonObject): JsonObject {
        val resultMap = mutableMapOf<String, Any>()

        val categories = mergeCategories(data1, data2)
        val currencies = mergeCurrencies(data1, data2)
        val accounts = mergeAccounts(data1, data2)
        val transactions = mergeTransactions(data1, data2)
        val transfers = mergeTransfers(data1, data2)

        convertToTransfer(transactions, transfers)

        resultMap.put("categories", categories)
        resultMap.put("currencies", currencies)
        resultMap.put("accounts", accounts)
        resultMap.put("transactions", transactions)
        resultMap.put("transfers", transfers)

        return JsonObject(resultMap)
    }

    fun load(data: JsonObject) {
        fun Int.toBooleanFromInt(): Boolean = this != 0
        fun Int.toNullIfZero(): Int? = if (this != 0) { this } else { null }

        val categories = data["categories"] as JsonArray<JsonObject>
        val currencies = data["currencies"] as JsonArray<JsonObject>
        val accounts = data["accounts"] as JsonArray<JsonObject>
        val transactions = data["transactions"] as JsonArray<JsonObject>
        val transfers = data["transfers"] as JsonArray<JsonObject>

        categories.sortWith(categoryComparator)
        currencies.sortBy { it["id"] as Int }
        accounts.sortBy { it["id"] as Int }
        transactions.sortBy { it["id"].toString().toInt() }
        transfers.sortBy { it["id"].toString().toInt()  }

        transaction {
            for (json in categories) {
                CategoriesLoader.insert {
                    it[id] = json["id"].toString().toInt()
                    it[name] = json["name"].toString()
                    it[type] = json["type"].toString().toInt()
                    it[parentId] = json["parentId"].toString().toInt().toNullIfZero()
                    it[orderId] = json["orderId"].toString().toInt()
                }
            }
            log.info("Categories loaded")

            CurrenciesLoader.batchInsert(currencies) { json ->
                this[CurrenciesLoader.id] = json["id"].toString().toInt()
                this[CurrenciesLoader.name] = json["name"].toString()
                this[CurrenciesLoader.shortName] = json["shortName"].toString()
            }
            log.info("Currencies loaded")
        }

        transaction {
            AccountsLoader.batchInsert(accounts) { json ->
                this[AccountsLoader.id] = json["id"].toString().toInt()
                this[AccountsLoader.name] = json["name"].toString()
                this[AccountsLoader.active] = json["active"].toString().toInt().toBooleanFromInt()
                this[AccountsLoader.balance] = BigDecimal.valueOf(json["balance"].toString().toDouble().roundWithPrecision(2))
                this[AccountsLoader.currencyId] = json["currencyId"].toString().toInt()
            }
            log.info("Accounts loaded")
        }

        transaction {
            TransactionsLoader.batchInsert(transactions) { json ->
                this[TransactionsLoader.id] = json["id"].toString().toInt()
                this[TransactionsLoader.name] = json["name"].toString()
                this[TransactionsLoader.type] = json["type"].toString().toInt()
                this[TransactionsLoader.categoryId] = json["categoryId"].toString().toInt().toNullIfZero()
                this[TransactionsLoader.date] = DateTime(json["date"].toString().toLong())
                this[TransactionsLoader.sum] = BigDecimal.valueOf(json["sum"].toString().toDouble().roundWithPrecision(2))
                this[TransactionsLoader.accountId] = json["accountId"].toString().toInt()
                this[TransactionsLoader.description] = json["description"].toString()
                this[TransactionsLoader.sourceId] = json["source"].toString().toInt().toNullIfZero()
            }
            log.info("Transactions loaded")
        }

        transaction {
            TransfersLoader.batchInsert(transfers) { json ->
                this[TransfersLoader.id] = json["id"].toString().toInt()
                this[TransfersLoader.name] = json["name"].toString()
                this[TransfersLoader.from] = json["transactionIdFrom"].toString().toInt()
                this[TransfersLoader.to] = json["transactionIdTo"].toString().toInt()
            }
            log.info("Transfers loaded")
        }
    }

    private fun clearDatabase() {
        transaction {
            TransfersLoader.deleteAll()
            TransactionsLoader.deleteAll()
            AccountsLoader.deleteAll()
            CurrenciesLoader.deleteAll()
            CategoriesLoader.deleteAll()
        }
        log.info("Database cleared")
    }

}