package com.chitzkoy.financepmreporter.server.routes

import com.chitzkoy.financepmreporter.model.dao.Config
import com.chitzkoy.financepmreporter.model.dao.Configs
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.ktor.locations.get
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.locations.locations
import org.jetbrains.ktor.response.respondRedirect
import org.jetbrains.ktor.routing.Route
import java.time.Year

/**
 * Created by dtikhonov on 27-Nov-17.
 */
@location("/")
class IndexRoute

fun Route.index() {
    get<IndexRoute> {
        var currency = "RUB"
        transaction {
            currency = Config.find { Configs.param eq "currency" }.first().value
        }
        call.respondRedirect(locations().href(YearReport(Year.now().value, currency)))

    }
}