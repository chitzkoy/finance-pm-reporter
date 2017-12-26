package com.chitzkoy.financepmreporter.server.routes

import com.chitzkoy.financepmreporter.service.ConfigService
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
        // + todo 1: добавить orderId в Categories, задействовать его при отображении
        // + todo 2: натянуть стили на отчет, собрать страницы в структуру
        // + todo 3: добавить возмножость посмотреть расходы по дочерним категориям прямо из отчета (иерархия катеорий)
        // + todo 4: сделать конвертацию расхода "Семья" в перевод
        // - todo 5: сделать конфигурирование: порядок вывода категорий, какую категорию конвертируем в перевод
        // - todo 6: подумать о создании diff'ов - как это делать быстро? (сохранять последний импортированный json в поле таблицы history? при невозможности найти дифф и долить - сбрасывать все и заливать по новой)
        // - todo 7: научиться маркировать (тегировать) категории как регулярные и строить предполагаемые траты вперед
        val model = mapOf("config" to service.gatherConfig())
        call.respond(FreeMarkerContent("config.ftl", model))
    }
}