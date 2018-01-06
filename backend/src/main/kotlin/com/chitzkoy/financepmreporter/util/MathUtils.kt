package com.chitzkoy.financepmreporter.util

fun Double.roundWithPrecision(precision : Int): Double {
    return Math.floor(this * Math.pow(10.0, precision.toDouble())) / Math.pow(10.0, precision.toDouble())
}