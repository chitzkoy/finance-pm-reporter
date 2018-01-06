package com.chitzkoy.financepmreporter.util

import java.util.*

/**
 * Created by dtikhonov on 13-Nov-17.
 */
object Property {

    private val props = Properties().apply {
        Property::class.java.classLoader.getResourceAsStream("application.properties").use {
            load(it)
        }
    }

    operator fun get(key: String) = props[key].toString()

}