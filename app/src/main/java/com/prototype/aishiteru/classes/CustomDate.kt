package com.prototype.aishiteru.classes

import java.util.Calendar

class CustomDate {
    private var day_in_month: Int
    private var month: Int
    private var year: Int

    // Creates a CustomDate for today
    constructor() {
        val c = Calendar.getInstance()
        year = c[Calendar.YEAR]
        day_in_month = c[Calendar.DAY_OF_MONTH]
        month = c[Calendar.MONTH]
    }

    // Getters
    fun getYear(): Int {
        return year
    }

    fun getMonth(): Int {
        return month
    }

    fun getDay(): Int {
        return day_in_month
    }


    // When supplied the day, month, and year, create the date accordingly.
    // Month here is expected to be 0-11
    constructor(year: Int, month: Int, day_in_month: Int) {
        this.year = year
        this.day_in_month = day_in_month
        this.month = month
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1, 2023
    // if CustomDate has 2023, 11, 1: Dec 1, 2023
    fun toStringFull(): String {
        return monthString[month] + " " + day_in_month + ", " + year
    }

    fun toStringISO(): String {
        return year.toString() + "/" + month.toString() + "/" + day_in_month.toString()
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1
    // if CustomDate has 2023, 11, 1: Dec 1
    fun toStringNoYear(): String {
        return monthString[month] + " " + day_in_month
    }

    companion object {
        private val monthString = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
    }


}