package com.example.apiet.flightcentrekotlintest.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.apiet.flightcentrekotlintest.R
import com.example.apiet.flightcentrekotlintest.activity.adapter.FlightsAdapter
import com.example.apiet.flightcentrekotlintest.activity.model.Flight
import com.example.apiet.flightcentrekotlintest.activity.service.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), FlightsAdapter.Listener {

    private val TAG = MainActivity::class.java.simpleName
    private val BASE_URL = "https://glacial-caverns-15124.herokuapp.com/flights/"
    private var compositeDisposable: CompositeDisposable? = null
    private var flightsArrayList: ArrayList<Flight>? = null
    private var adapter: FlightsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_refresh.isRefreshing = true

        compositeDisposable = CompositeDisposable()
        initRecyclerView()

        swipe_refresh.setOnRefreshListener {loadJSON()}
        loadJSON()
    }

    private fun initRecyclerView() {
        flights_list.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        flights_list.layoutManager = layoutManager
    }

    private fun loadJSON() {
        val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Api::class.java)

        compositeDisposable?.add(api.getAllFlights()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(flightList: List<Flight>) {
        flightsArrayList = ArrayList(flightList)
        adapter = FlightsAdapter(flightsArrayList!!, this)
        flights_list.adapter = adapter
        swipe_refresh.isRefreshing = false
    }

    private fun handleError(error: Throwable) {
        swipe_refresh.isRefreshing = false
        Log.d(TAG, error.localizedMessage)
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(flight: Flight) {
        val intent = Intent(this, FlightActivity::class.java)
        intent.putExtra("flightId", flight.id)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}
