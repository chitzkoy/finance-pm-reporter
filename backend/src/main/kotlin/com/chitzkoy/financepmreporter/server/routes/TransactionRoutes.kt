package com.chitzkoy.financepmreporter.server.routes

import com.chitzkoy.financepmreporter.server.ApplicationPage
import com.chitzkoy.financepmreporter.service.ReportService
import org.jetbrains.ktor.content.TextContent
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.html.respondHtmlTemplate
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.etag
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.accept
import org.jetbrains.ktor.routing.contentType
import org.jetbrains.ktor.sessions.sessionOrNull

/**
 * Created by dtikhonov on 13-Nov-17.
 */
@location("/report/{currency}/{year}")
class YearReport(val year: Int, val currency: String)

fun Route.transactions(reportService: ReportService) {
    accept(ContentType.Text.Html) {
        get<YearReport> {
            call.respondHtmlTemplate(ApplicationPage()) {
                caption { +"Finance PM reporter" }
            }
        }
    }
    accept(ContentType.Application.Json) {
        get<YearReport> {
            call.respond(reportService.reportByYear(it.year, it.currency))
        }
    }
//    get<YearReport> {
//        val report = reportService.reportByYear(it.year, it.currency)
//        val model = mapOf("report" to report)
//        call.respond(FreeMarkerContent("reportIndex.ftl", model))
//    }
}