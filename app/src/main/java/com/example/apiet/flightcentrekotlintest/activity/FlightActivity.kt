package com.example.apiet.flightcentrekotlintest.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.apiet.flightcentrekotlintest.R
import com.example.apiet.flightcentrekotlintest.activity.model.Flight
import com.example.apiet.flightcentrekotlintest.activity.service.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_flight.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class FlightActivity : AppCompatActivity() {

    private val TAG = FlightActivity::class.java.simpleName
    private val BASE_URL = "https://glacial-caverns-15124.herokuapp.com/flights/"
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight)

        compositeDisposable = CompositeDisposable()

        //handle -1 value as an error
        val intentObject : Intent = intent
        val flightId = intentObject.getIntExtra("flightId", -1)

        swipe_refresh_detail.isRefreshing = true
        swipe_refresh_detail.setOnRefreshListener { loadJSON(flightId) }
        loadJSON(flightId)
    }

    private fun loadJSON(flightId: Int) {
        val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Api::class.java)

        compositeDisposable?.add(api.getFlightById(flightId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(flight: Flight) {
        val airlineCode : TextView = findViewById(R.id.airline_code)
        val flightNumber : TextView = findViewById(R.id.flight_number)

        val departureDate : TextView = findViewById(R.id.departure_date)
        val departureTime : TextView = findViewById(R.id.departure_time)
        val departureCity : TextView = findViewById(R.id.departure_city)
        val departureAirport : TextView = findViewById(R.id.departure_airport)

        val scheduledDuration : TextView = findViewById(R.id.scheduled_duration)

        val arrivalDate : TextView = findViewById(R.id.arrival_date)
        val arrivalTime : TextView = findViewById(R.id.arrival_time)
        val arrivalCity : TextView = findViewById(R.id.arrival_city)
        val arrivalAirport : TextView = findViewById(R.id.arrival_airport)

        val createdAt : TextView = findViewById(R.id.created_at)
        val updatedAt : TextView = findViewById(R.id.updated_at)

        airlineCode.text = getString(R.string.identifier_airline_code, flight.airline_code)
        flightNumber.text = getString(R.string.identifier_flight_number, flight.flight_number)

        departureDate.text = getDateString(flight.departure_date)
        departureTime.text = getTimeString(flight.departure_date)
        departureCity.text = getString(R.string.identifier_city, flight.departure_city)
        departureAirport.text = getString(R.string.identifier_airport, flight.departure_airport)

        scheduledDuration.text = getString(R.string.identifier_duration, flight.scheduled_duration)

        arrivalDate.text = getDateString(flight.arrival_date)
        arrivalTime.text = getTimeString(flight.arrival_date)
        arrivalCity.text = getString(R.string.identifier_city, flight.arrival_city)
        arrivalAirport.text = getString(R.string.identifier_airport, flight.arrival_airport)

        createdAt.text = getString(R.string.identifier_created_at, "${getDateString(flight.created_at)} \n ${getTimeString(flight.created_at)}")
        updatedAt.text = getString(R.string.identifier_updated_at, "${getDateString(flight.updated_at)} \n ${getTimeString(flight.updated_at)}")
        swipe_refresh_detail.isRefreshing = false
    }

    //The date converters should be places in a helper/util class
    private fun getTimeString(dateString: String): String {
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

    private fun getDateString(dateString: String): String {
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

    private fun handleError(error: Throwable) {
        swipe_refresh_detail.isRefreshing = false
        Log.d(TAG, error.localizedMessage)
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}
