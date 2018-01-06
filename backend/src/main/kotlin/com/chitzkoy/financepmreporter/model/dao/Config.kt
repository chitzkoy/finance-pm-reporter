package com.chitzkoy.financepmreporter.model.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

/**
 * Created by dtikhonov on 17-Nov-17.
 */
class Config(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Config>(Configs)

    var param by Configs.param
    var value by Configs.value

    fun toTO() : ConfigTO {
        return ConfigTO(param, value)
    }
}

object Configs : IntIdTable("Config") {
    val param = varchar("param", length = 120).uniqueIndex()
    val value = varchar("value", length = 180)
}

class ConfigTO(val param: String, val value: String)