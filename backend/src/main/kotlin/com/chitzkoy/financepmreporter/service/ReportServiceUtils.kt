package com.chitzkoy.financepmreporter.service

import com.chitzkoy.financepmreporter.model.CategoryInfo
import com.chitzkoy.financepmreporter.model.common.Month
import com.chitzkoy.financepmreporter.model.common.LocalDate
import com.chitzkoy.financepmreporter.model.to.CategoryTO
import com.chitzkoy.financepmreporter.model.to.TransactionTO
import com.chitzkoy.financepmreporter.util.roundWithPrecision
import java.time.YearMonth
import java.util.function.Predicate


/**
 * Рассчитывает баланс для последовательности транзакций.
 * Возоможна фильтрация по дате (баланс на определенную дату)
 * и по валюте (если вызвается на последовательности с транзакциями в разных валютах)
 *
 * @param [byDate] Дата, используемая для рассчета балана; при отсутствии параметра рассчет ведется для всей выборки
 * @param [currency] Символьный код валюты, используемый для фильтрации; при отсутствии параметра рассчет ведется для всей выборки
 *
 * @return суммарный баланс для последовательности транзакций.
 */
internal fun Iterable<TransactionTO>.balance(byDate: LocalDate? = null, currency: String? = null) : Double {
    return calculateBalance(this, byDate, currency)
}
internal fun Iterable<TransactionTO>.balance(byDate: Long? = null, currency: String? = null) : Double {
    val date = LocalDate(byDate)
    return calculateBalance(this, date, currency)
}
internal fun calculateBalance(transactions: Iterable<TransactionTO>, byDate: LocalDate? = null, currency: String? = null) : Double {
    val currencyPredicate: Predicate<TransactionTO> = Predicate { t ->
        if (currency != null) {
            t.account.currency.shortName == currency
        } else {
            true
        }
    }

    val datePredicate: Predicate<TransactionTO> = Predicate { t ->
        if (byDate != null) {
            t.date <= byDate.millis
        } else {
            true
        }
    }

    val dateFiltered = transactions
            .filter { datePredicate.test(it) }
            .filter { currencyPredicate.test(it) }
            .filter { it.account.active }

    val accountsDefaultBalance = dateFiltered
            .groupBy{ it.account }
            .map { it.key.balance }
            .sum()

    return (accountsDefaultBalance + dateFiltered
            .groupBy{ it.type }
            .mapValues { it.value.sumByDouble { it.sum } }
            .mapValues { if (it.key.isIncome) it.value else -it.value }
            .values.sumByDouble { it })
            .roundWithPrecision(2)
}

/**
 * Рассчитывает пару "расход-доход" для заданного промежутка времени для последовательности транзакций.
 * Возоможна фильтрация по валюте (если вызвается на последовательности с транзакциями в разных валютах)
 *
 * @param [dateRange] Временной промежуток, для которого тербуется рассчитать доход-расход
 * @param [currency] Символьный код валюты, используемый для фильтрации; при отсутствии параметра рассчет ведется для всей выборки
 *
 * @return [Pair] "расход-доход" для заданного промежутка времени, где  first - доход (положительное значениче), second - расход (отрицательное значение).
 */
internal fun Iterable<TransactionTO>.incomeExpenses(dateRange: ClosedRange<LocalDate>, currency: String? = null) : Pair<Double,Double> {
    val currencyPredicate: Predicate<TransactionTO> = Predicate { t ->
        if (currency != null) {
            t.account.currency.shortName == currency
        } else {
            true
        }
    }

    val result = this
            .filter { LocalDate(it.date) in dateRange }
            .filter { it.source == null }
            .filter { currencyPredicate.test(it) }
            .filter { it.account.active }
            .groupBy{ it.type }
            .mapValues { it.value.sumByDouble { it.sum } }
            .mapValues { if (it.key.isIncome) it.value else -it.value }
            .values.map { it.roundWithPrecision(2) }
            .sortedDescending()
            .toMutableList()

    while (result.size < 2) {
        result.add(0.0)
    }

    return Pair(result[0], result[1])
}

internal fun calculateAvgPerCategory(categories: Collection<CategoryTO>, currency: String) : MutableMap<CategoryTO, Double> {
    val months = getAvgMonthDistance()

    val now = YearMonth.now()
    val xMonthsAgo = now.minusMonths(months.toLong())
    val from = LocalDate(xMonthsAgo.year, xMonthsAgo.month.getValue(), 1, 0, 0)
    val to = LocalDate(now.year, now.month.value, now.month.length(now.isLeapYear), 23, 59, 59)
    val transactions = transactionsInRange(from, to, currency).groupBy { it.category }

    val avgPerCategory: MutableMap<CategoryTO, Double> = mutableMapOf()

    for (category in categories) {
        try {
            val sum = transactions[category]?.sumByDouble { it.sum } ?: 0.0
            avgPerCategory.put(category, sum / months)
        } catch (e : NullPointerException) {
            println(category)
        }
    }

    return avgPerCategory
}

internal fun calculateTotalsPerMonth(
        categorizationFunctions: MutableMap<CategoryTO, (Int) -> Double>,
        categoryTotals: MutableMap<CategoryTO, MutableMap<Month, Double>>
) {

    categorizationFunctions.forEach { entry ->
        val category = entry.key
        val sumFunction = entry.value
        val totalsPerCategory: MutableMap<Month, Double> = mutableMapOf()
        Month.values().forEach { month ->
            totalsPerCategory.put(month, sumFunction(month.getValue()))
        }
        categoryTotals.put(category, totalsPerCategory)
    }

}

internal fun calculateTotalsForChildCategories(
        childCategories: MutableList<CategoryInfo>,
        categoryTotals: MutableMap<CategoryTO, MutableMap<Month, Double>>,
        topLevelCategory: CategoryTO,
        avgPerCategory: MutableMap<CategoryTO, Double>
) {

    val allSubCategoryTotals = categoryTotals
            .filterKeys { it != topLevelCategory }
            .filter { topLevelCategory(it.key) == topLevelCategory }

    val subCategoriesByLevel = allSubCategoryTotals.keys.groupBy { foldLevelFromTop(it) }
    if (subCategoriesByLevel.isEmpty()) {
        return
    }

    val range = subCategoriesByLevel.keys.sorted()
    val rangeWithoutFirst = range.subList(1, range.size)

    // fill childCategories list with first-level categories
    subCategoriesByLevel[1]!!.sortedBy { it.orderId }.forEach {
        childCategories.add(CategoryInfo(it, categoryTotals[it]!!, mutableListOf(), avgPerCategory[it]))
    }

    // build categories hierarchy for first-level categories
    if (rangeWithoutFirst.isNotEmpty()) {
        for (i in rangeWithoutFirst) {
            subCategoriesByLevel[i]!!.sortedBy { it.orderId }.forEach { foldedCategory ->
                val siblings = extractSiblingsForCategory(childCategories, foldedCategory)
                siblings.add(CategoryInfo(foldedCategory, categoryTotals[foldedCategory]!!, mutableListOf(), avgPerCategory[foldedCategory]))
            }
        }
    }

    // calculate totalSumsPerMonth for each parent node
    for (i in range.reversed().subList(1, range.size)) {
        subCategoriesByLevel[i]!!.forEach { category ->
            val categoryInfo = extractSiblingsForCategory(childCategories, category).first { it.category == category }
            val ownCategoryInfoSums = categoryInfo.totalSumsPerMonth
            val lowLevelChilds = categoryInfo.childCategories

            lowLevelChilds.forEach { childCategory ->
                childCategory.totalSumsPerMonth.forEach { entry ->
                    val month = entry.key
                    val sum = entry.value
                    ownCategoryInfoSums[month] = ownCategoryInfoSums[month]!!.plus(sum)
                }
                categoryInfo.avg = categoryInfo.avg?.plus (childCategory.avg ?: 0.0)
            }
        }
    }

}

internal fun calculateTopLevelTotals(ownTopLevelTotals: MutableMap<Month, Double>?, childCategories: MutableList<CategoryInfo>): MutableMap<Month, Double> {
    val topLevelTotals : MutableMap<Month, Double> = ownTopLevelTotals ?: mutableMapOf()

    if (topLevelTotals.isEmpty() && childCategories.isEmpty()) {
        return mutableMapOf()
    }

    if (topLevelTotals.isEmpty()) {
        childCategories.first().totalSumsPerMonth.forEach { entry ->
            topLevelTotals.put(entry.key, 0.0)
        }
    }

    childCategories.forEach { childCategory ->
        childCategory.totalSumsPerMonth.forEach { entry ->
            val month = entry.key
            val sum = entry.value
            topLevelTotals[month] = topLevelTotals[month]!! + sum
        }
    }

    return topLevelTotals
}

internal fun calculateTopLevelAvg(ownTopLevelAvg: Double?, childCategories: MutableList<CategoryInfo>): Double? {
    if (ownTopLevelAvg == null && childCategories.isEmpty()) {
        return null
    }

    var topLevelAvg = ownTopLevelAvg ?: 0.0
    childCategories.forEach { childCategory ->
        topLevelAvg += childCategory.avg ?: 0.0
    }

    return if (topLevelAvg != 0.0) topLevelAvg else null
}

internal fun extractSiblingsForCategory(childCategories: MutableList<CategoryInfo>, category: CategoryTO): MutableList<CategoryInfo> {
    fun findAncestorOfLevel(category: CategoryTO, level: Int): CategoryTO {
        val ownLevel = foldLevelFromTop(category)
        if (ownLevel < level) throw IllegalArgumentException("ownLevel < level | ownLevel: $ownLevel, level: $level")

        var parent = category
        for (i in level until ownLevel) {
            parent = parent.parent!!
        }
        return parent
    }

    var tmp = childCategories
    for (level in 1 until foldLevelFromTop(category)) {
        tmp = tmp.first { it.category == findAncestorOfLevel(category, level) }.childCategories
    }
    return tmp
}


internal fun populateCategorizationFunctions(
        categorizationFunctions: MutableMap<CategoryTO, Function1<Int, Double>>,
        map: Map<CategoryTO?, List<TransactionTO>>
) {
    map.keys
            .filterNotNull()
            .sortedBy { it.orderId }
            .forEach {
                categorizationFunctions[it] = { month -> sumByMonth(map[it], month) }
            }
}

/**
 * Возвращает категорию верхнего уровня для заданной
 */
internal fun topLevelCategory(category: CategoryTO): CategoryTO {
    val parentCategory = category.parent
    while (parentCategory != null) {
        return topLevelCategory(parentCategory)
    }
    return category
}

/**
 * Возвращает уровень вложенности для заданной категории, где 0 - категории верхнего уровня
 */
internal fun foldLevelFromTop(category: CategoryTO): Int {
    fun foldLevelFromTop(category: CategoryTO, level: Int): Int {
        val parentCategory = category.parent
        while (parentCategory != null) {
            return foldLevelFromTop(parentCategory, level + 1)
        }
        return level
    }

    val parentCategory = category.parent
    while (parentCategory != null) {
        return foldLevelFromTop(parentCategory, 1)
    }
    return 0
}

internal fun sumByMonth(transactions: List<TransactionTO>?, month: Int): Double {
    if (transactions == null) {
        return 0.0
    }

    val groupedTransactions = transactions.groupBy { transaction ->
        LocalDate(transaction.date).monthOfYear == month
    }

    return if (groupedTransactions[true] != null) {
        groupedTransactions[true]!!.sumByDouble {
            it.sum
        }
    } else {
        0.0
    }
}