package com.chitzkoy.financepmreporter.util

import com.chitzkoy.financepmreporter.model.dao.ConfigTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by dtikhonov on 26-Nov-17.
 */
fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this::class.java.name) }
}

fun defaultConfig() : List<ConfigTO> {
    val props = Properties().apply {
        Property::class.java.classLoader.getResourceAsStream("defaults.properties").use {
            load(it)
        }
    }

    return props.stringPropertyNames().map { it -> ConfigTO(it, props[it].toString()) }
}