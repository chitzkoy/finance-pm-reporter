package com.chitzkoy.financepmreporter.model.common

/**
 * Partial copy-paste of java.time.Month
 */
//actual typealias IllegalArgumentException = kotlin.IllegalArgumentException

//actual enum class Month {
//    /**
//     * The singleton instance for the month of January with 31 days.
//     * This has the numeric value of `1`.
//     */
//    JANUARY,
//    /**
//     * The singleton instance for the month of February with 28 days, or 29 in a leap year.
//     * This has the numeric value of `2`.
//     */
//    FEBRUARY,
//    /**
//     * The singleton instance for the month of March with 31 days.
//     * This has the numeric value of `3`.
//     */
//    MARCH,
//    /**
//     * The singleton instance for the month of April with 30 days.
//     * This has the numeric value of `4`.
//     */
//    APRIL,
//    /**
//     * The singleton instance for the month of May with 31 days.
//     * This has the numeric value of `5`.
//     */
//    MAY,
//    /**
//     * The singleton instance for the month of June with 30 days.
//     * This has the numeric value of `6`.
//     */
//    JUNE,
//    /**
//     * The singleton instance for the month of July with 31 days.
//     * This has the numeric value of `7`.
//     */
//    JULY,
//    /**
//     * The singleton instance for the month of August with 31 days.
//     * This has the numeric value of `8`.
//     */
//    AUGUST,
//    /**
//     * The singleton instance for the month of September with 30 days.
//     * This has the numeric value of `9`.
//     */
//    SEPTEMBER,
//    /**
//     * The singleton instance for the month of October with 31 days.
//     * This has the numeric value of `10`.
//     */
//    OCTOBER,
//    /**
//     * The singleton instance for the month of November with 30 days.
//     * This has the numeric value of `11`.
//     */
//    NOVEMBER,
//    /**
//     * The singleton instance for the month of December with 31 days.
//     * This has the numeric value of `12`.
//     */
//    DECEMBER;
//
//    /**
//     * Private cache of all the constants.
//     */
//
//    //-----------------------------------------------------------------------
//    /**
//     * Obtains an instance of `Month` from an `int` value.
//     *
//     *
//     * `Month` is an enum representing the 12 months of the year.
//     * This factory allows the enum to be obtained from the `int` value.
//     * The `int` value follows the ISO-8601 standard, from 1 (January) to 12 (December).
//     *
//     * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
//     * @return the month-of-year, not null
//     * @throws DateTimeException if the month-of-year is invalid
//     */
//    companion object {
//        private val ENUMS = Month.values()
//
//        fun of(month: Int): Month {
//            if (month < 1 || month > 12) {
//                throw IllegalArgumentException("Invalid value for MonthOfYear: " + month)
//            }
//            return ENUMS[month - 1]
//        }
//    }
//
//    actual open fun getValue(): Int {
//        return ordinal + 1
//    }
//
//    //-----------------------------------------------------------------------
//    /**
//     * Returns the month-of-year that is the specified number of quarters after this one.
//     *
//     *
//     * The calculation rolls around the end of the year from December to January.
//     * The specified period may be negative.
//     *
//     *
//     * This instance is immutable and unaffected by this method call.
//     *
//     * @param months  the months to add, positive or negative
//     * @return the resulting month, not null
//     */
//    operator fun plus(months: Long): Month {
//        val amount = (months % 12).toInt()
//        return ENUMS[(ordinal + (amount + 12)) % 12]
//    }
//
//    /**
//     * Returns the month-of-year that is the specified number of months before this one.
//     *
//     *
//     * The calculation rolls around the start of the year from January to December.
//     * The specified period may be negative.
//     *
//     *
//     * This instance is immutable and unaffected by this method call.
//     *
//     * @param months  the months to subtract, positive or negative
//     * @return the resulting month, not null
//     */
//    operator fun minus(months: Long): Month {
//        return plus(-(months % 12))
//    }
//
//    //-----------------------------------------------------------------------
//    /**
//     * Gets the length of this month in days.
//     *
//     *
//     * This takes a flag to determine whether to return the length for a leap year or not.
//     *
//     *
//     * February has 28 days in a standard year and 29 days in a leap year.
//     * April, June, September and November have 30 days.
//     * All other months have 31 days.
//     *
//     * @param leapYear  true if the length is required for a leap year
//     * @return the length of this month in days, from 28 to 31
//     */
//    actual open fun length(leapYear: Boolean): Int {
//        return when (this) {
//            FEBRUARY -> if (leapYear) 29 else 28
//            APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30
//            else -> 31
//        }
//    }
//
//    override fun toString(): String {
//        return this.name
//    }
//
//}