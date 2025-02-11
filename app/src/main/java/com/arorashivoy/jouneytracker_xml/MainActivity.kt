package com.arorashivoy.jouneytracker_xml

import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader

data class Stop(val stop: String, val distance: Double?, val visaRequired: Boolean = false)
class MainActivity : AppCompatActivity() {
    private lateinit var stopTextView: TextView
    private lateinit var distanceTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var nextStopButton: Button
    private lateinit var unitToggleButton: Button
    private lateinit var recyclerView: RecyclerView

    private var stops: List<Stop> = listOf()
    private var currentStopIndex = 0
    private var totalDistance = 0.0
    private var distanceCovered = 0.0
    private var isKm = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        stopTextView = findViewById(R.id.stopTextView)
        distanceTextView = findViewById(R.id.distanceTextView)
        progressBar = findViewById(R.id.progressBar)
        nextStopButton = findViewById(R.id.nextStopButton)
        unitToggleButton = findViewById(R.id.unitToggleButton)
        recyclerView = findViewById(R.id.recyclerView)

        stops = loadStopsFromResource(resources)
        totalDistance = calculateTotalDistance()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StopsAdapter(stops, isKm)
        updateUI()

        nextStopButton.setOnClickListener {
            if (currentStopIndex < stops.size - 1) {
                distanceCovered += stops[currentStopIndex].distance ?: 0.0
                currentStopIndex++
                updateUI()
            }
        }

        unitToggleButton.setOnClickListener {
            isKm = !isKm
            updateUI()
        }
    }

    private fun loadStopsFromResource(resources: Resources): List<Stop> {
        return try {
            val inputStream = resources.openRawResource(R.raw.stops)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLines().map { Stop(it.split(",")[0].trim(), it.split(",")[1].trim().toDoubleOrNull(),
                it.split(",")[2].trim() == "YES"
            ) }

        } catch (e: Exception) {
            e.printStackTrace()
            listOf(Stop("Unknown", 0.0))
        }
    }
    private fun calculateTotalDistance(): Double {
        return stops.sumOf { it.distance ?: 0.0 }
    }

    private fun updateUI() {
        stopTextView.text = "Current Stop: ${stops[currentStopIndex].stop}"
        val remainingDistance = totalDistance - distanceCovered
        val displayDistance = if (isKm) remainingDistance else remainingDistance * 0.621371
        val unit = if (isKm) "km" else "miles"
        distanceTextView.text = "Remaining Distance: %.2f %s".format(displayDistance, unit)
        progressBar.progress = ((distanceCovered / totalDistance) * 100).toInt()
        recyclerView.adapter = StopsAdapter(stops.subList(currentStopIndex + 1, stops.size), isKm)
    }
}
