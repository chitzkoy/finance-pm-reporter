package com.chitzkoy.financepmreporter.util

import kotlin.math.floor
import kotlin.math.pow

fun Double.roundWithPrecision(precision : Int): Double {
    return floor(this * 10.0.pow(precision)) / 10.0.pow(precision)
}