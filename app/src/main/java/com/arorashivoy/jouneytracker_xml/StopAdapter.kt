package com.arorashivoy.jouneytracker_xml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StopsAdapter(private val stops: List<Stop>) : RecyclerView.Adapter<StopsAdapter.StopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return StopViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        holder.stopText.text = stops[position].stop
    }

    override fun getItemCount(): Int = stops.size

    class StopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stopText: TextView = view.findViewById(android.R.id.text1)
    }
}
