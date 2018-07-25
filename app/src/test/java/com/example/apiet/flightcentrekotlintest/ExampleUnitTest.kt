package com.example.apiet.flightcentrekotlintest

import com.example.apiet.flightcentrekotlintest.activity.service.Util
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun date_conversion_isCorrect() {
        assertEquals(" 2017/11/15", Util().getDateString("2017-11-15T06:05:00.000"))
    }

    //Note that this test shows us that the time conversion stayed in GMT
    @Test
    fun time_conversion_isCorrect() {
        assertEquals(" 06 : 05", Util().getTimeString("2017-11-15T06:05:00.000"))
    }
}
