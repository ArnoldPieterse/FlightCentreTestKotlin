package com.example.apiet.flightcentrekotlintest.activity.service

import java.text.SimpleDateFormat
import java.util.*

class Util {
    val BASE_URL = "https://glacial-caverns-15124.herokuapp.com/flights/"

    fun getTimeString(dateString: String): String {
        val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        val date = SimpleDateFormat(dateFormat, Locale.US).parse(dateString)
        val calender = Calendar.getInstance()
        calender.time = date

        var hour :String = calender.get(Calendar.HOUR_OF_DAY).toString()
        var minute :String = calender.get(Calendar.MINUTE).toString()
        if(hour.toInt() < 10) {
            hour = "0$hour"
        }
        if(minute.toInt() < 10) {
            minute = "0$minute"
        }
        return " $hour : $minute"
    }

    fun getDateString(dateString: String): String {
        val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        val date = SimpleDateFormat(dateFormat, Locale.US).parse(dateString)
        val calender = Calendar.getInstance()
        calender.time = date

        val year :String = calender.get(Calendar.YEAR).toString()
        var month :String = (calender.get(Calendar.MONTH)+1).toString()
        var day :String = calender.get(Calendar.DAY_OF_MONTH).toString()
        if(month.toInt() < 10) {
            month = "0$month"
        }
        if(day.toInt() < 10) {
            day = "0$day"
        }
        return " $year/$month/$day"
    }
}