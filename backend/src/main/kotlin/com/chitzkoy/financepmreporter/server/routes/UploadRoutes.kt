package com.chitzkoy.financepmreporter.server.routes

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.chitzkoy.financepmreporter.model.dao.Config
import com.chitzkoy.financepmreporter.model.dao.Configs
import com.chitzkoy.financepmreporter.service.LoaderService
import com.chitzkoy.financepmreporter.util.logger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.ktor.content.file
import org.jetbrains.ktor.content.static
import org.jetbrains.ktor.freemarker.FreeMarkerContent
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.locations.locations
import org.jetbrains.ktor.locations.post
import org.jetbrains.ktor.request.PartData
import org.jetbrains.ktor.request.isMultipart
import org.jetbrains.ktor.request.receiveMultipart
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondRedirect
import org.jetbrains.ktor.response.respondWrite
import org.jetbrains.ktor.routing.Route
import java.time.Year

/**
 * Created by dtikhonov on 16-Nov-17.
 */

@location("/upload")
class UploadJson

fun Route.upload(loaderService: LoaderService) {
    val log by logger()

    get<UploadJson> {
        call.respond(FreeMarkerContent("uploadIndex.ftl", Any()))
    }

    post<UploadJson> {
        val parser = Parser()
        val multipart = call.receiveMultipart()
        if (!call.request.isMultipart()) {
            log.error("Not a multipart request")
        } else {
            val partIterator = multipart.parts.iterator()
            while (partIterator.hasNext()) {
                val part = partIterator.next()
                if (part is PartData.FileItem) {

                    val inputStream = part.streamProvider.invoke()
                    if (inputStream.available() == 0) {
                        log.warn("Empty file occurred. Dispose part...")
                        part.dispose()
                        continue
                    }

                    try {
                        loaderService.addToMerge(parser.parse(inputStream) as JsonObject)
                    } catch (ignored: Exception) {
                        log.error("FinancePM file parsing error")
                    }
                }
                part.dispose()
            }

        }

        if (loaderService.commitMerge(true) != null) {
            log.info("File successfully parsed. Redirecting to report...")

            var currency = "RUB"
            transaction {
                currency = Config.find { Configs.param eq "currency" }.first().value
            }
            call.respondRedirect(locations().href(YearReport(Year.now().value, currency)))
        } else {
            call.respondWrite { appendln("Not a multipart request or parsing error of FinancePM file") }
        }
    }
}