package com.chitzkoy.financepmreporter.service

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.chitzkoy.financepmreporter.model.dao.Config
import com.chitzkoy.financepmreporter.model.dao.Configs
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created by dtikhonov on 19-Nov-17.
*/
val categoryComparator = Comparator<JsonObject> { o1, o2 ->
    val id1 = o1["id"].toString().toInt()
    val id2 = o2["id"].toString().toInt()

    val parentId1 = o1["parentId"].toString().toInt()
    val parentId2 = o2["parentId"].toString().toInt()

    return@Comparator if (((parentId1 > id1) && (parentId2 > id2)) || ((parentId1 < id1) && (parentId2 < id2))) {
        id1 - id2
    } else if ((parentId1 > id1) && (parentId2 < id2)) {
        1
    } else {
        -1
    }
}

fun mergeCategories(data1: JsonObject, data2: JsonObject): JsonArray<JsonObject> {
    val categories1 = data1["categories"] as JsonArray<JsonObject>
    val categories2 = data2["categories"] as JsonArray<JsonObject>

    // get keys which will replaced by value in transactions
    val categoryIdReplaceMap = findDuplicatingCategories(categories1, categories2)
    val mergingCategories = categories2.filterNot {
        it["id"] in categoryIdReplaceMap.keys
    }
    val lastIdCategories1 = categories1.map { it["id"] as Int }.max()!!
    mergingCategories.forEachIndexed{ index, json ->
        val oldId =  json["id"]
        val newId = lastIdCategories1 + index + 1
        categoryIdReplaceMap.put(oldId, newId)
        json["id"] = newId
    }

    val transactions2 = data2["transactions"] as JsonArray<JsonObject>
    categoryIdReplaceMap.filterNot { entry -> entry.key == entry.value }.forEach { entry ->
        transactions2.filter { it["categoryId"] == entry.key }.forEach {
            it["categoryId"] = entry.value
        }
    }

    val categories = categories1.toMutableList()
    categories.addAll(mergingCategories)
//    categories.sortWith(categoryComparator)

    return JsonArray(categories.toList())
}

fun mergeCurrencies(data1: JsonObject, data2: JsonObject): JsonArray<JsonObject> {
    val currencies1 = data1["currencies"] as JsonArray<JsonObject>
    val currencies2 = data2["currencies"] as JsonArray<JsonObject>

    val currencyIdReplaceMap = findDuplicatingCurrencies(currencies1, currencies2)
    val mergingCurrencies = currencies2.filterNot {
        it["id"] in currencyIdReplaceMap.keys
    }
    val lastIdCurrencies1 = currencies1.map { it["id"] as Int }.max()!!
    mergingCurrencies.forEachIndexed{ index, json ->
        val oldId =  json["id"]
        val newId = lastIdCurrencies1 + index + 1
        currencyIdReplaceMap.put(oldId, newId)
        json["id"] = newId
    }

    val accounts2 = data2["accounts"] as JsonArray<JsonObject>
    currencyIdReplaceMap.filterNot { entry -> entry.key == entry.value }.forEach { entry ->
        accounts2.filter { it["currencyId"] == entry.key }.forEach {
            it["currencyId"] = entry.value
        }
    }

    val currencies = currencies1.toMutableList()
    currencies.addAll(mergingCurrencies)

    return JsonArray(currencies.toList())
}

fun mergeAccounts(data1: JsonObject, data2: JsonObject): JsonArray<JsonObject> {
    val accounts1 = data1["accounts"] as JsonArray<JsonObject>
    val accounts2 = data2["accounts"] as JsonArray<JsonObject>

    val accountIdReplaceMap = HashMap<Any?, Any?>()
    val lastIdAccounts1 = accounts1.map { it["id"] as Int }.max()!!
    accounts2.forEachIndexed{ index, json ->
        val oldId = json["id"]
        val newId = lastIdAccounts1 + index + 1
        accountIdReplaceMap.put(oldId, newId)
        json["id"] = newId
    }

    val transactions2 = data2["transactions"] as JsonArray<JsonObject>
    accountIdReplaceMap.forEach { entry ->
        transactions2.filter { it["accountId"] == entry.key }.forEach {
            it["accountId"] = entry.value
        }
    }

    val accounts = accounts1.toMutableList()
    accounts.addAll(accounts2)

    return JsonArray(accounts.toList())
}

fun mergeTransactions(data1: JsonObject, data2: JsonObject): JsonArray<JsonObject> {
    val transactions1 = data1["transactions"] as JsonArray<JsonObject>
    val transactions2 = data2["transactions"] as JsonArray<JsonObject>

    var transactionIdReplaceMap = HashMap<Any?, Any?>()
    val lastIdTransaction1 = transactions1.map { it["id"].toString().toInt() }.max()!!
    transactions2.forEachIndexed{ index, json ->
        val oldId = json["id"]
        val newId = (lastIdTransaction1 + index + 1).toString()
        transactionIdReplaceMap.put(oldId, newId)
        json["id"] = newId
    }

    val transfers2 = data2["transfers"] as JsonArray<JsonObject>
    transactionIdReplaceMap.forEach { entry ->
        transfers2.find { it["transactionIdFrom"] == entry.key }?.set("transactionIdFrom", entry.value)
        transfers2.find { it["transactionIdTo"] == entry.key }?.set("transactionIdTo", entry.value)
    }

    val transactions = transactions1.toMutableList()
    transactions.addAll(transactions2)

    return JsonArray(transactions.filter { it["available"] == 1 }.toList())
}

fun mergeTransfers(data1: JsonObject, data2: JsonObject): JsonArray<JsonObject> {
    val transfers1 = data1["transfers"] as JsonArray<JsonObject>
    val transfers2 = data2["transfers"] as JsonArray<JsonObject>

    val lastIdTransfer1 = transfers1.map { it["id"].toString().toInt() }.max()!!
    transfers2.forEachIndexed{ index, json ->
        json["id"] = (lastIdTransfer1 + index + 1).toString()
    }

    val transfers = transfers1.toMutableList()
    transfers.addAll(transfers2)

    return JsonArray(transfers.toList())
}

fun convertToTransfer(transactions: MutableList<JsonObject>, transfers: JsonArray<JsonObject>) {
    var categoryFrom : Int? = null
    var categoryTo : Int? = null

    transaction {
        categoryFrom = Config.find { Configs.param eq "convertFromCategoryTransfer" }.first().value.toInt()
        categoryTo = Config.find { Configs.param eq "convertToCategoryTransfer" }.first().value.toInt()
    }

    if (categoryFrom == null || categoryTo == null) {
        return
    }

    var lastTransferId = if (transfers.isEmpty()) 0 else transfers.map { it["id"].toString().toInt() }.max()!!
    transactions
            .filter { it["categoryId"] == categoryFrom || it["categoryId"] == categoryTo }
            .groupBy { it["date"] }.filter { it.value.size > 1 }
            .forEach { entry ->
                val pair = entry.value.groupBy { it["sum"] }.filter { it.value.size > 1 }
                for (sum in pair.keys) {
                    if (pair[sum] == null || pair[sum]!!.size < 2) {
                        continue
                    }

                    val first = pair[sum]!![0]
                    val second = pair[sum]!![1]

                    if (first["type"] == second["type"]) {
                        continue
                    }

                    val from = if (first["type"] == 2 && second["type"] == 1) first else second
                    val to = if (first["type"] == 2 && second["type"] == 1) second else first
                    from["categoryId"] = 0
                    from["source"] = 1
                    to["categoryId"] = 0
                    to["source"] = 1

                    val newTransfer = JsonObject()
                    newTransfer["id"] = ++lastTransferId
                    newTransfer["available"] = 1
                    newTransfer["transactionIdFrom"] = from["id"]
                    newTransfer["transactionIdTo"] = to["id"]
                    transfers.add(newTransfer)
                }
            }
}


private fun findDuplicatingCategories(categories1: JsonArray<JsonObject>, categories2: JsonArray<JsonObject>): MutableMap<Any?, Any?> {
    infix fun JsonObject.equals(another: JsonObject): Boolean {
        if (this === another) return true
        if (this["name"] != another["name"]) return false
        if (this["type"] != another["type"]) return false
        if (this["parentId"] != another["parentId"]) return false
        return true
    }

    val categoryIdReplaceMap = HashMap<Any?, Any?>()

    categories1.forEach { category ->
        for (duplicateCandidate in categories2) {
            if (category equals duplicateCandidate) {
                categoryIdReplaceMap.put(duplicateCandidate["id"], category["id"])
                break
            }
        }
    }

    // fix parentId for merging categories
    categories2.filter {
        categoryIdReplaceMap.filterNot { entry -> entry.key == entry.value }.keys.contains(it["parentId"])
    }.forEach { duplicateCandidate ->
        duplicateCandidate["parentId"] = categoryIdReplaceMap[duplicateCandidate["parentId"]]
        categories1.forEach {
            if (it equals duplicateCandidate) {
                categoryIdReplaceMap.put(duplicateCandidate["id"], it["id"])
            }
        }
    }

    return categoryIdReplaceMap
}

private fun findDuplicatingCurrencies(currencies1: JsonArray<JsonObject>, currencies2: JsonArray<JsonObject>): MutableMap<Any?, Any?> {
    infix fun JsonObject.equals(another: JsonObject): Boolean {
        if (this === another) return true
        if (this["name"] != another["name"]) return false
        if (this["shortName"] != another["shortName"]) return false
        return true
    }

    val currencyIdReplaceMap = HashMap<Any?, Any?>()

    currencies1.forEach { category ->
        for (duplicateCandidate in currencies2) {
            if (category equals duplicateCandidate) {
                currencyIdReplaceMap.put(duplicateCandidate["id"], category["id"])
                break
            }
        }
    }

    return currencyIdReplaceMap
}