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
import com.example.apiet.flightcentrekotlintest.activity.service.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_flight.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FlightActivity : AppCompatActivity() {

    private val TAG = FlightActivity::class.java.simpleName
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight)

        compositeDisposable = CompositeDisposable()

        //a error will show as a toast on fail if default value gets used
        val intentObject : Intent = intent
        val flightId = intentObject.getIntExtra("flightId", -1)

        swipe_refresh_detail.isRefreshing = true
        swipe_refresh_detail.setOnRefreshListener { loadJSON(flightId) }
        loadJSON(flightId)
    }

    private fun loadJSON(flightId: Int) {
        val api = Retrofit.Builder()
                .baseUrl(Util().BASE_URL)
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

        departureDate.text = Util().getDateString(flight.departure_date)
        departureTime.text = Util().getTimeString(flight.departure_date)
        departureCity.text = getString(R.string.identifier_city, flight.departure_city)
        departureAirport.text = getString(R.string.identifier_airport, flight.departure_airport)

        scheduledDuration.text = getString(R.string.identifier_duration, flight.scheduled_duration)

        arrivalDate.text = Util().getDateString(flight.arrival_date)
        arrivalTime.text = Util().getTimeString(flight.arrival_date)
        arrivalCity.text = getString(R.string.identifier_city, flight.arrival_city)
        arrivalAirport.text = getString(R.string.identifier_airport, flight.arrival_airport)

        createdAt.text = getString(R.string.identifier_created_at, "${Util().getDateString(flight.created_at)} \n ${Util().getTimeString(flight.created_at)}")
        updatedAt.text = getString(R.string.identifier_updated_at, "${Util().getDateString(flight.updated_at)} \n ${Util().getTimeString(flight.updated_at)}")
        swipe_refresh_detail.isRefreshing = false
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
