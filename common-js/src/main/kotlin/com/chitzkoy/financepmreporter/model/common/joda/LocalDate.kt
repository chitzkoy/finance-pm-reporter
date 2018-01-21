package com.chitzkoy.financepmreporter.model.common.joda

external open class LocalDate {

    companion object {
        fun now(): LocalDate
        fun of(year: Int, month: Int, dayOfMonth: Int): LocalDate
        fun ofInstant(millis: Instant): LocalDate
    }

    /**
     *
     * @return {number} gets the year
     */
    fun year() : Int

    /**
     *
     * @return {number} gets the month value
     */
    fun monthValue() : Int

    /**
     *
     * @returns {Month} month
     */
//    fun month() : Int

    /**
     *
     * @return {number} gets the day of month
     */
    fun dayOfMonth() : Int

    /**
      * Gets the day-of-year field.
      *
      * This method returns the primitive int value for the day-of-year.
      *
      * @return {number} the day-of-year, from 1 to 365, or 366 in a leap year
      */
    fun dayOfYear() : Int

    fun isAfter(instant: LocalDate): Boolean

    fun isBefore(instant: LocalDate): Boolean

    open fun minusMonths(months: Int): LocalDate

    open fun plusMonths(months: Int): LocalDate
}