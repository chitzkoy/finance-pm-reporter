package com.chitzkoy.financepmreporter.server

import com.chitzkoy.financepmreporter.server.routes.*
import com.chitzkoy.financepmreporter.service.ConfigService
import com.chitzkoy.financepmreporter.service.LoaderService
import com.chitzkoy.financepmreporter.service.ReportService
import com.chitzkoy.financepmreporter.util.Property
import freemarker.cache.ClassTemplateLoader
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.content.*
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.features.StatusPages
import org.jetbrains.ktor.freemarker.FreeMarker
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.locations.Locations
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.route
import java.io.File

/**
 * Created by dtikhonov on 13-Nov-17.
 */
fun startServer() = embeddedServer(
        Netty,
        Property["server.port"].toInt(),
        watchPaths = listOf("MainKt"),
        module = Application::module).start(wait = true)

val reportService = ReportService()

fun Application.module() {
    Database()

    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(Application::class.java.classLoader, Property["freemarker.path"])
    }
    install(StatusPages) {
        exception<Throwable> {
            println(it)
            it.stackTrace.forEach(::println)
            call.respond(FreeMarkerContent("errorPage.ftl", Any()))
        }
    }
    install(Routing) {
        index()
        transactions(reportService)
        upload(LoaderService())
        config(ConfigService())

        static("public") { // it's about URL
            files("public") // it's about folder under root of project
        }
    }
}