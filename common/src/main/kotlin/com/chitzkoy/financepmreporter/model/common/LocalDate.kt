package com.chitzkoy.financepmreporter.model.common

expect class LocalDate {

    /**
     * Get the month of year field value.
     *
     * @return the month of year
     */
    open fun getMonthOfYear(): Int

    /**
     * Get the year field value.
     *
     * @return the year
     */
    open fun getYear(): Int

    open fun minusMonths(months: Int): LocalDate

    open fun plusMonths(months: Int): LocalDate

    //-----------------------------------------------------------------------
    /**
     * Is this instant strictly after the millisecond instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  a millisecond instant to check against
     * @return true if this instant is strictly after the instant passed in
     */
    open fun isAfter(instant: Long): Boolean

    open fun isBefore(instant: Long): Boolean

    /**
     * Is this instant strictly after the current instant
     * comparing solely by millisecond.
     *
     * @return true if this instant is strictly after the current instant
     */
    open fun isAfterNow(): Boolean

    open fun isBeforeNow(): Boolean

}