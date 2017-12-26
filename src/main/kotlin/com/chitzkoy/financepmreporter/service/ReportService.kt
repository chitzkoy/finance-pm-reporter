package com.chitzkoy.financepmreporter.service

import com.chitzkoy.financepmreporter.model.CategoryInfo
import com.chitzkoy.financepmreporter.model.YearReportModel
import com.chitzkoy.financepmreporter.model.dao.imported.CategoryTO
import org.joda.time.DateTime
import java.time.Month
import java.util.*

class ReportService {

    fun reportByYear(year: Int, currency: String): YearReportModel {
        val from = DateTime(year, 1, 1, 0, 0)
        val to = DateTime(year, 12, 31, 23, 59, 59)
        val transactions = transactionsInRange(from, to, currency)

        val topLevelCategories = topLevelCategories().sortedWith(Comparator { o1, o2 ->
            val type1 = o1.type.id
            val type2 = o2.type.id
            val typeComparison = type1.compareTo(type2)

            // place income in head of list
            return@Comparator if (typeComparison != 0) {
                typeComparison
            } else {
                o1.orderId.compareTo(o2.orderId)
            }
        })

        val incomeCategorized = transactions.filter { it.isIncome() }.groupBy { it.category }
        val expensesTopLevelCategorized = transactions.filter { !it.isIncome() }.groupBy { it.category }

        val categorizationFunctions: MutableMap<CategoryTO, Function1<Int, Double>> = mutableMapOf()
        val categoryTotals: MutableMap<CategoryTO, MutableMap<Month, Double>> = mutableMapOf()

        populateCategorizationFunctions(categorizationFunctions, incomeCategorized)
        populateCategorizationFunctions(categorizationFunctions, expensesTopLevelCategorized)

        val avgPerCategory: MutableMap<CategoryTO, Double> = calculateAvgPerCategory(categorizationFunctions.keys, currency)

        // calculate totals per month for each category
        calculateTotalsPerMonth(categorizationFunctions, categoryTotals)

        val categoryInfos: MutableList<CategoryInfo> = mutableListOf()
        topLevelCategories.forEach { topLevelCategory ->
            val childCategories = mutableListOf<CategoryInfo>()

            calculateTotalsForChildCategories(childCategories, categoryTotals, topLevelCategory, avgPerCategory)

            val topLevelSums = calculateTopLevelTotals(categoryTotals[topLevelCategory], childCategories)
            val topLevelAvg = calculateTopLevelAvg(avgPerCategory[topLevelCategory], childCategories)
            if (topLevelSums.isNotEmpty()) {
                categoryInfos.add(CategoryInfo(topLevelCategory, topLevelSums, childCategories, topLevelAvg))
            }
        }

        return YearReportModel(year, currency.toUpperCase(), Month.values().toList(), categoryInfos, allTransactions(currency.toUpperCase()))
    }

}