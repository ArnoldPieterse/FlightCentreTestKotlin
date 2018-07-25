package com.example.apiet.flightcentrekotlintest.activity.service

import com.example.apiet.flightcentrekotlintest.activity.model.Flight
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("all")
    fun getAllFlights() : Observable<List<Flight>>

    @GET("{id}")
    fun getFlightById(@Path("id") id: Int) : Observable<Flight>
}