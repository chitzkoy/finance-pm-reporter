package com.chitzkoy.financepmreporter.server.routes

import com.chitzkoy.financepmreporter.service.ReportService
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route

/**
 * Created by dtikhonov on 13-Nov-17.
 */
@location("/report/{currency}/{year}")
class YearReport(val year: Int, val currency: String)

fun Route.transactions(reportService: ReportService) {
    get<YearReport> {
        val report = reportService.reportByYear(it.year, it.currency)
        val model = mapOf("report" to report)
        call.respond(FreeMarkerContent("reportIndex.ftl", model))
    }
}