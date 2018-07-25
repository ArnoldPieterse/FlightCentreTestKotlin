package com.example.apiet.flightcentrekotlintest.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apiet.flightcentrekotlintest.R
import com.example.apiet.flightcentrekotlintest.activity.model.Flight
import com.example.apiet.flightcentrekotlintest.activity.service.Util
import java.util.*

class FlightsAdapter (private val dataList : ArrayList<Flight>, private val listener : Listener): RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>() {

    interface Listener {
        fun onItemClick(flight : Flight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flight_listitem_layout, parent, false)
        return FlightViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.count()

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bindModel(dataList[position], listener)
    }

    inner class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val flightNumberTxt : TextView = itemView.findViewById(R.id.flight_number)
        private val flightDepartureTxt : TextView = itemView.findViewById(R.id.flight_from)
        private val flightDestinationTxt : TextView = itemView.findViewById(R.id.flight_destination)
        private val flightDateTxt : TextView = itemView.findViewById(R.id.flight_date)
        private val flightTimeTxt : TextView = itemView.findViewById(R.id.flight_time)

        fun bindModel(flight: Flight, listener: Listener) {

            flightNumberTxt.text = flight.flight_number
            flightDepartureTxt.text = flight.departure_city
            flightDestinationTxt.text = flight.arrival_city
            flightDateTxt.text = Util().getDateString(flight.departure_date)
            flightTimeTxt.text = Util().getTimeString(flight.departure_date)

            itemView.setOnClickListener{ listener.onItemClick(flight)}
        }
    }
}