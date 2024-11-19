package com.prototype.aishiteru.classes

import java.util.Calendar

class CustomDate {
    private var day_in_month: Int
    private var month: Int
    private var year: Int
    private var hour: Int
    private var min: Int

    // Creates a CustomDate for today
    constructor() {
        val c = Calendar.getInstance()
        year = c[Calendar.YEAR]
        day_in_month = c[Calendar.DAY_OF_MONTH]
        month = c[Calendar.MONTH]
        hour = c[Calendar.HOUR_OF_DAY]
        min = c[Calendar.MINUTE]
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
    constructor(year: Int, month: Int, day_in_month: Int, hour: Int = 0, min: Int = 0) {
        this.year = year
        this.day_in_month = day_in_month
        this.month = month
        this.hour = hour
        this.min = min
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1, 2023
    // if CustomDate has 2023, 11, 1: Dec 1, 2023
    fun toStringFull(): String {
        return monthString[month] + " " + day_in_month + ", " + year
    }

    fun toStringISO(detailed: Boolean = false): String {
        if (detailed)
            return year.toString() + "/" + month.toString() + "/" + day_in_month.toString() + " " + String.format("%02d:%02d", hour,min)

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