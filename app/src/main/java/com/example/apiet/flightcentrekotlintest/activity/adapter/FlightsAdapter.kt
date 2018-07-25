package com.example.apiet.flightcentrekotlintest.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apiet.flightcentrekotlintest.R
import com.example.apiet.flightcentrekotlintest.activity.model.Flight
import java.text.SimpleDateFormat
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
            val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
            val date = SimpleDateFormat(dateFormat, Locale.US).parse(flight.departure_date)
            val calender = Calendar.getInstance()
            calender.time = date

            flightNumberTxt.text = flight.flight_number
            flightDepartureTxt.text = flight.departure_city
            flightDestinationTxt.text = flight.arrival_city
            flightDateTxt.text = getDateString(calender)
            flightTimeTxt.text = getTimeString(calender)

            itemView.setOnClickListener{ listener.onItemClick(flight)}
        }

        private fun getTimeString(calender: Calendar): String {
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

        private fun getDateString(calender: Calendar): String {
            var year :String = calender.get(Calendar.YEAR).toString()
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
}