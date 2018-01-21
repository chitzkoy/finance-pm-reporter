package com.chitzkoy.financepmreporter.model

import com.chitzkoy.financepmreporter.model.common.Month
import com.chitzkoy.financepmreporter.model.to.CategoryTO
import com.chitzkoy.financepmreporter.model.to.TransactionTO
import kotlin.math.abs
import kotlin.math.min

/**
 * Created by dtikhonov on 15-Nov-17.
 */
data class Repository<T>(val current: T, val available: List<T>)
data class Totals(val balance: Double, val income: Double, val expenses: Double)
data class YearTotals(val avgTotals: Totals, val totalsPerMonth: MutableMap<Month, Totals>)
data class CategoryInfo(
        val category: CategoryTO,
        val totalSumsPerMonth: MutableMap<Month, Double>,
        val childCategories: MutableList<CategoryInfo>,
        var avg: Double? = null
)
data class YearReportModel(
        val yearRepo: Repository<Int>,
        val currencyRepo: Repository<String>,
        val monthRepo: Repository<Month>,
        val categoryInfoList: List<CategoryInfo>,
        val yearTotals: YearTotals,
        val defaultAvgMonthDistance: Int
//        private val transactionList: List<TransactionTO>
) {

    constructor(model: YearReportModel) : this(
        model.yearRepo,
        model.currencyRepo,
        model.monthRepo,
        model.categoryInfoList,
        model.yearTotals,
        model.defaultAvgMonthDistance
    )

    fun isCurrentMonth(month: Month) : Boolean {
        return month == monthRepo.current
    }

    fun getPaginatorYears(): List<Int> {
        val available = yearRepo.available
        val maxIndex = available.size - 1
        val indexOfCurrent = available.indexOf(yearRepo.current)

        //todo add test
        return if (indexOfCurrent == 0 || indexOfCurrent == maxIndex) {
            val first = indexOfCurrent
            val last = min(abs(indexOfCurrent - 2), maxIndex)
            if (first > last) available.subList(last, first + 1) else available.subList(first, last + 1)
        } else {
            val first = abs(indexOfCurrent - 1)
            val last = min(abs(indexOfCurrent + 1), maxIndex)
            if (first > last) available.subList(last, first + 1) else available.subList(first, last + 1)
        }
    }

}