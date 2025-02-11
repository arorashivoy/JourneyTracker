package com.arorashivoy.jouneytracker_xml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StopsAdapter(private val stops: List<Stop>, private val isKm: Boolean) : RecyclerView.Adapter<StopsAdapter.StopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stop_item, parent, false)
        return StopViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        val visaRequired = if (stops[position].visaRequired) "Yes" else "No"
        val distance = if (isKm) stops[position].distance ?: 0.0 else stops[position].distance?.times(0.621371) ?: 0.0
        val time = stops[position].time ?: 0.0
        val unit = if (isKm) "km" else "miles"

        holder.stopText.text = stops[position].stop
        holder.distanceText.text = "Distance: %.2f %s".format(distance, unit)
        holder.timeText.text = "Time: %.2f hrs".format(time)
        holder.visaRequired.text = "Visa Required: $visaRequired"
    }

    override fun getItemCount(): Int = stops.size

    class StopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stopText: TextView = view.findViewById(R.id.stopName)
        val distanceText: TextView = view.findViewById(R.id.stopDistance)
        val timeText: TextView = view.findViewById(R.id.stopTime)
        val visaRequired: TextView = view.findViewById(R.id.visaRequired)
    }
}
