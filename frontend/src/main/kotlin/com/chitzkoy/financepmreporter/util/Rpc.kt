package com.chitzkoy.financepmreporter.util

import com.chitzkoy.financepmreporter.model.YearReportModel
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import kotlin.browser.window
import kotlin.js.json



suspend fun getYearReport(year: Int, currency: String): YearReportModel =
        getAndParseResult("/report/$currency/$year", null, ::parseYearReportResponse)

fun parseYearReportResponse(json: dynamic): YearReportModel {
    return YearReportModel(json)
}

suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("POST", url, body, parse)

suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("GET", url, body, parse)

suspend fun <T> requestAndParseResult(method: String, url: String, body: dynamic, parse: (dynamic) -> T): T {
    val response = window.fetch(url, object: RequestInit {
        override var method: String? = method
        override var body: dynamic = body
        override var credentials: RequestCredentials? = "same-origin".asDynamic()
        override var headers: dynamic = json("Accept" to "application/json")
    }).await()
    return parse(response.json().await())
}
