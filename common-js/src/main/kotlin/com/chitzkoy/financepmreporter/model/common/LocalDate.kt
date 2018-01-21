package com.chitzkoy.financepmreporter.model.common

import com.chitzkoy.financepmreporter.model.common.joda.Instant

actual class LocalDate: com.chitzkoy.financepmreporter.model.common.joda.LocalDate(){

    /**
     * Get the month of year field value.
     *
     * @return the month of year
     */
    actual open fun getMonthOfYear(): Int {
        return monthValue()
    }

    /**
     * Get the year field value.
     *
     * @return the year
     */
    actual open fun getYear(): Int {
        return year()
    }


    actual override fun minusMonths(months: Int): LocalDate {
        return super.minusMonths(months) as LocalDate
    }

    actual override fun plusMonths(months: Int): LocalDate {
        return super.plusMonths(months) as LocalDate
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instant strictly after the millisecond instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  a millisecond instant to check against
     * @return true if this instant is strictly after the instant passed in
     */
    actual open fun isAfter(instant: Long): Boolean {
        return isAfter(ofInstant(Instant.ofEpochMilli(instant)))
    }

    actual open fun isBefore(instant: Long): Boolean {
        return isBefore(ofInstant(Instant.ofEpochMilli(instant)))
    }

    /**
     * Is this instant strictly after the current instant
     * comparing solely by millisecond.
     *
     * @return true if this instant is strictly after the current instant
     */
    actual open fun isAfterNow(): Boolean {
        return isAfter(now())
    }

    actual open fun isBeforeNow(): Boolean {
        return isBefore(now())
    }

//
//    actual constructor(
//            year: Int,
//            monthOfYear: Int,
//            dayOfMonth: Int,
//            hourOfDay: Int,
//            minuteOfHour: Int,
//            secondOfMinute: Int) : this(js("new Date(year, monthOfYear-1, dayOfMonth, hourOfDay, minuteOfHour, minuteOfHour)")) {
//    }
//
//    companion object {
//        fun now(): DateTime {
//            return DateTime(js("new Date()"))
//        }
//    }
//
//    /**
//     * Get the month of year field value.
//     *
//     * @return the month of year
//     */
//    actual open fun getMonthOfYear(): Int {
//        return getMonth() + 1
//    }
//
//    /**
//     * Get the year field value.
//     *
//     * @return the year
//     */
//    actual open fun getYear(): Int {
//        return js("date.getFullYear()")
//    }
//
//    private fun getMonth(): Int {
//        return js("date.getMonth()")
//    }
//
//    private fun getDay(): Int {
//        return js("date.getDate()")
//    }
//
//    private fun getHours(): Int {
//        return js("date.getHours()")
//    }
//
//    private fun getMinutes(): Int {
//        return js("date.getMinutes()")
//    }
//
//    private fun getSeconds(): Int {
//        return js("date.getSeconds()")
//    }
//
//    actual open fun minusMonths(months: Int): DateTime {
//        return DateTime(getYear(), getMonthOfYear() - months, getDay(), getHours(), getMinutes(), getSeconds())
//    }
//
//    actual open fun plusMonths(months: Int): DateTime {
//        return minusMonths(-months)
//    }
//
//    //-----------------------------------------------------------------------
//    /**
//     * Is this instant strictly after the millisecond instant passed in
//     * comparing solely by millisecond.
//     *
//     * @param instant  a millisecond instant to check against
//     * @return true if this instant is strictly after the instant passed in
//     */
//    actual open fun isAfter(instant: Long): Boolean {
//        return date.getTime().toLong() > instant
//    }
//
//    actual open fun isBefore(instant: Long): Boolean {
//        return date.getTime().toLong() < instant
//    }
//
//    actual open fun isAfterNow(): Boolean {
//        return isAfter(now().date.getTime().toLong())
//    }
//
//    actual open fun isBeforeNow(): Boolean {
//        return isBefore(now().date.getTime().toLong())
//    }
}