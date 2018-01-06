package com.chitzkoy.financepmreporter.server.routes

import com.chitzkoy.financepmreporter.service.ConfigService
import org.jetbrains.ktor.content.file
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Route

/**
 * Created by dtikhonov on 13-Nov-17.
 */
@location("/configuration")
class ConfigurationRoute

fun Route.config(service : ConfigService) {
    get<ConfigurationRoute> {
        val model = mapOf("config" to service.gatherConfig())
        call.respond(FreeMarkerContent("config.ftl", model))
    }
}