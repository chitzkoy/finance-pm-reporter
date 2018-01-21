package com.chitzkoy.financepmreporter.service

import com.chitzkoy.financepmreporter.model.*
import com.chitzkoy.financepmreporter.model.common.LocalDate
import com.chitzkoy.financepmreporter.model.common.Month
import com.chitzkoy.financepmreporter.model.to.CategoryTO
import com.chitzkoy.financepmreporter.model.to.TransactionTO
import com.chitzkoy.financepmreporter.util.roundWithPrecision
import java.time.Year
import java.time.YearMonth
import java.util.*

class ReportService {

    fun reportByYear(year: Int, currency: String): YearReportModel {
        val from = LocalDate(year, 1, 1, 0, 0, 0)
        val to = LocalDate(year, 12, 31, 23, 59, 59)
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

        val allTransactions = allTransactions(currency.toUpperCase())
        val totalsPerMonth = mutableMapOf<Month, Totals>()
        Month.values().forEach {
            totalsPerMonth.put(it, Totals(
                    balance(year, allTransactions, it),
                    income(year, allTransactions, it),
                    expenses(year, allTransactions, it)
            ))
        }
        val yearTotals = YearTotals(
                Totals(Double.NaN, avgIncome(year, allTransactions), avgExpenses(year, allTransactions)),
                totalsPerMonth
        )

        return YearReportModel(
                Repository(year, getAvailableYearsRange().toList()),
                Repository(currency.toUpperCase(), allAvailableCurrencies().map { it.shortName }),
                Repository(Month.of(YearMonth.now().monthValue), Month.values().toList()),
                categoryInfos,
                yearTotals,
                getAvgMonthDistance()
        )
    }

}



//todo clean up this

fun balance(year: Int, transactionList: List<TransactionTO>, month: Month): Double {
    val date = LocalDate(year, month.getValue(), month.length(Year.isLeap(year.toLong())), 23, 59, 59)
    return if (date.isAfterNow && LocalDate.now().monthOfYear != month.getValue()) 0.0 else transactionList.balance(date)
}

fun income(year: Int, transactionList: List<TransactionTO>, month: Month): Double {
    val from = LocalDate(year, month.getValue(), 1, 0, 0, 0)
    val to = LocalDate(year, month.getValue(), month.length(Year.isLeap(year.toLong())), 23, 59, 59)
    return transactionList.incomeExpenses(from..to).first
}

fun expenses(year: Int, transactionList: List<TransactionTO>, month: Month): Double {
    val from = LocalDate(year, month.getValue(), 1, 0, 0, 0)
    val to = LocalDate(year, month.getValue(), month.length(Year.isLeap(year.toLong())), 23, 59, 59)
    return transactionList.incomeExpenses(from..to).second
}

fun avgIncome(year: Int, transactionList: List<TransactionTO>): Double {
    val monthDistance = getAvgMonthDistance()
    val currentMonth = LocalDate.now().getMonthOfYear()
    val from = LocalDate(year, currentMonth, 1, 0, 0, 0).minusMonths(monthDistance)
    val to = LocalDate(year, currentMonth, java.time.Month.of(currentMonth).length(Year.isLeap(year.toLong())), 23, 59, 59)
    return ( transactionList.incomeExpenses(from..to).first / monthDistance ).roundWithPrecision(2)
}

fun avgExpenses(year: Int, transactionList: List<TransactionTO>): Double {
    val currentMonth = LocalDate.now().getMonthOfYear()
    val currentYear = YearMonth.now().year
    val from: LocalDate
    val to: LocalDate
    val monthDistance = getAvgMonthDistance()
    if (currentYear == year) {
        from = LocalDate(currentYear, currentMonth, 1, 0, 0, 0).minusMonths(monthDistance.toInt())
        to = LocalDate(currentYear, currentMonth, java.time.Month.of(currentMonth).length(Year.isLeap(year.toLong())), 23, 59, 59)
    } else {
        from = LocalDate(year, 1, 1, 0, 0, 0).minusMonths(monthDistance)
        to = LocalDate(year, 12, 31, 23, 59, 59)
    }
    return ( transactionList.incomeExpenses(from..to).second / monthDistance ).roundWithPrecision(2)
}