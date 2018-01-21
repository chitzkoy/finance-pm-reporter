package com.chitzkoy.financepmreporter.service

import com.chitzkoy.financepmreporter.model.dao.*
import com.chitzkoy.financepmreporter.model.dao.imported.*
import com.chitzkoy.financepmreporter.util.defaultConfig
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import com.chitzkoy.financepmreporter.model.common.LocalDate
import com.chitzkoy.financepmreporter.model.to.CategoryTO
import com.chitzkoy.financepmreporter.model.to.CurrencyTO
import com.chitzkoy.financepmreporter.model.to.TransactionTO
import java.time.Year

/**
 * Created by dtikhonov on 15-Nov-17.
 */
fun allTransactions(currencyCode: String): List<TransactionTO> {
    return transaction {
        return@transaction Transaction
                .all()
                .filter { it.account.currency.shortName == currencyCode }
                .map { it.toTO() }
                .toList()
    }
}
fun transactionsInRange(fromDate: LocalDate, toDate: LocalDate, currencyCode: String): List<TransactionTO> {
    return transaction {
        return@transaction Transaction
                .find { (Transactions.date greaterEq fromDate) and (Transactions.date lessEq toDate) }
                .filter { it.account.currency.shortName.toUpperCase() == currencyCode.toUpperCase() }
                .map { it.toTO() }
                .toList()
    }
}

fun allAvailableCurrencies(): List<CurrencyTO> {
    return transaction {
        return@transaction Transaction
                .all()
                .groupBy { it.account.currency }
                .map { entry -> entry.key }
                .map { it.toTO() }
                .toList()
    }
}

fun allRegularCategories() : List<RegularCategoryTO> {
    return transaction {
        return@transaction RegularCategory.all().map { it.toTO() }
    }
}

fun getAvgMonthDistance(): Int {
    var param: ConfigTO? = null
    transaction {
        param = Config.find { (Configs.param eq "avgMonths") }.first().toTO()
    }
    if (param == null) {
        param = defaultConfig().first { it.param == "avgMonths" }
    }
    return param?.value!!.toInt()
}

fun getAvailableYearsRange(): IntRange {
    var minDate = Year.now().value
    var maxDate = Year.now().value
    var currencies: Iterable<Currency>
    var accounts: Iterable<Account> = emptyList()
    var transactions: Iterable<Transaction>
    transaction {
        currencies = Currency.find { Currencies.shortName eq "RUB" }
        accounts = Account.find { Accounts.currency inList currencies }

        // can't use { Transactions.account inList accounts }; I have no any idea why
        transactions = Transaction.find { Transactions.sourceId.isNotNull() }.sortedBy { it.date }


        minDate = if (transactions.count() != 0) transactions.first().date.year else Year.now().value
        maxDate = if (transactions.count() != 0) transactions.last().date.year else Year.now().value
    }

    return minDate..maxDate
}


fun topLevelCategories(): List<CategoryTO> {
    var result: List<CategoryTO> = emptyList()
    transaction {
        result = Category.find { Categories.parent.isNull() }.map { it.toTO() }.toList()
    }
    return result
}