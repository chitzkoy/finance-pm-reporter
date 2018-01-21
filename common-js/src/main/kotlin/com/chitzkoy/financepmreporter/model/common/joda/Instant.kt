package com.chitzkoy.financepmreporter.model.common.joda

external open class Instant {

    companion object {
        fun now(): Instant
        fun ofEpochMilli(millis: Long): Instant
    }

}