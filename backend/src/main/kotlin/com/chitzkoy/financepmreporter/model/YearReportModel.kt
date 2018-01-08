package com.chitzkoy.financepmreporter.model

import com.chitzkoy.financepmreporter.model.dao.imported.TransactionTO
import com.chitzkoy.financepmreporter.service.*
import com.chitzkoy.financepmreporter.util.roundWithPrecision
import org.joda.time.DateTime
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

/**
 * Created by dtikhonov on 15-Nov-17.
 */
data class YearReportModel(
        val year: Int,
        val currency: String,
        val months: List<Month>,
        val categoryInfoList: List<CategoryInfo>,
        private val transactionList: List<TransactionTO>
) {

    fun isCurrentMonth(month: Month) : Boolean {
        return YearMonth.now().month == month
    }

    fun localizeMonth(month: Month, locale: String): String {
        return month.getDisplayName(TextStyle.FULL_STANDALONE, Locale(locale.split("_")[0]))
    }

    fun currentYear(offset: Int): String {
        return (year + offset).toString()
    }

    fun getAvailableYears(): List<Int> {
        return getAvailableYearsRange().toList()
    }

    fun getPaginatorYears(): List<Int> {
        val available = getAvailableYears()
        val maxIndex = available.size - 1
        val indexOfCurrent = available.indexOf(year)

        //todo add test
        return if (indexOfCurrent == 0 || indexOfCurrent == maxIndex) {
            val first = indexOfCurrent
            val last = Math.min(Math.abs(indexOfCurrent - 2), maxIndex)
            if (first > last) available.subList(last, first + 1) else available.subList(first, last + 1)
        } else {
            val first = Math.abs(indexOfCurrent - 1)
            val last = Math.min(Math.abs(indexOfCurrent + 1), maxIndex)
            if (first > last) available.subList(last, first + 1) else available.subList(first, last + 1)
        }
    }

    fun getAvgMonths(): Double {
        val currentYear = YearMonth.now().year
        return if (currentYear == year) {
            getAvgMonthDistance()
        } else {
            12.0 // whole year
        }
    }

    fun availableCurrencies() : List<String> {
        return allAvailableCurrencies().map { it.shortName }
    }

    fun balance(month: Month): Double {
        val date = DateTime(year, month.value, month.length(Year.isLeap(year.toLong())), 23, 59, 59)
        return if (date.isAfterNow && !isCurrentMonth(month)) 0.0 else transactionList.balance(date)
    }

    fun income(month: Month): Double {
        val from = DateTime(year, month.value, 1, 0, 0, 0)
        val to = DateTime(year, month.value, month.length(Year.isLeap(year.toLong())), 23, 59, 59)
        return transactionList.incomeExpenses(from..to).first
    }

    fun expenses(month: Month): Double {
        val from = DateTime(year, month.value, 1, 0, 0, 0)
        val to = DateTime(year, month.value, month.length(Year.isLeap(year.toLong())), 23, 59, 59)
        return transactionList.incomeExpenses(from..to).second
    }

    fun avgIncome(): Double {
        val monthDistance = getAvgMonths()
        val currentMonth = YearMonth.now().month.value
        val from = DateTime(year, currentMonth, 1, 0, 0, 0).minusMonths(monthDistance.toInt())
        val to = DateTime(year, currentMonth, Month.of(currentMonth).length(Year.isLeap(year.toLong())), 23, 59, 59)
        return ( transactionList.incomeExpenses(from..to).first / monthDistance ).roundWithPrecision(2)
    }

    fun avgExpenses(): Double {
        val currentMonth = YearMonth.now().month.value
        val currentYear = YearMonth.now().year
        val from: DateTime
        val to: DateTime
        val monthDistance = getAvgMonths()
        if (currentYear == year) {
            from = DateTime(currentYear, currentMonth, 1, 0, 0, 0).minusMonths(monthDistance.toInt())
            to = DateTime(currentYear, currentMonth, Month.of(currentMonth).length(Year.isLeap(year.toLong())), 23, 59, 59)
        } else {
            from = DateTime(year, 1, 1, 0, 0, 0).minusMonths(monthDistance.toInt())
            to = DateTime(year, 12, 31, 23, 59, 59)
        }
        return ( transactionList.incomeExpenses(from..to).second / monthDistance ).roundWithPrecision(2)
    }
}