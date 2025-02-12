package com.arorashivoy.journeytracker_compose

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.arorashivoy.journeytracker_compose.ui.theme.JourneyTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.BufferedReader
import java.io.InputStreamReader

data class Stop(val stop: String, val distance: Double?, val time: Double?, val visaRequired: Boolean = false)

data class StopUIState (
    var currentStopIndex: Int = 0,
    var distanceCovered: Double = 0.0,
    var timeCovered: Double = 0.0,
    var stops: List<Stop> = listOf(),
    var isKm: Boolean = true,
    var totalDistance: Double = 0.0,
    var totalTime: Double = 0.0,
)

class StopViewModel(resources: Resources): ViewModel() {
    private val _uiState = MutableStateFlow(StopUIState())
    val uiState: StateFlow<StopUIState> = _uiState.asStateFlow()

    init {
        val stops = loadStopsFromResource(resources)
        setStops(stops)
    }

    fun nextState() {
        _uiState.update { currentState ->
            if (currentState.currentStopIndex < currentState.stops.size - 1) {
                val newIndex = currentState.currentStopIndex + 1
                val newDistanceCovered = currentState.distanceCovered + (currentState.stops[newIndex].distance ?: 0.0)
                val newTimeCovered = currentState.timeCovered + (currentState.stops[newIndex].time ?: 0.0)
                currentState.copy(
                    currentStopIndex = newIndex,
                    distanceCovered = newDistanceCovered,
                    timeCovered = newTimeCovered,
                )
            } else {
                currentState
            }
        }
    }

    fun toggleUnit() {
        _uiState.update { currentState ->
            currentState.copy(
                isKm = !currentState.isKm
            )
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun setStops(stops: List<Stop>) {
        _uiState.update { currentState ->
            currentState.copy(
                stops = stops,
                totalDistance = stops.sumOf { it.distance ?: 0.0 },
                totalTime = stops.sumOf { it.time ?: 0.0 },
            )
        }
    }

    private fun loadStopsFromResource(resources: Resources): List<Stop> {
        return try {
            val inputStream = resources.openRawResource(R.raw.stops)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLines().map {
                val parts = it.split(",").map { part -> part.trim() }
                Stop(
                    stop = parts[0],
                    distance = parts.getOrNull(1)?.toDoubleOrNull(),
                    time = parts.getOrNull(2)?.toDoubleOrNull(),
                    visaRequired = parts.getOrNull(3)?.equals("YES", ignoreCase = true) == true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf(Stop("Unknown", 0.0, 0.0, false))
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: StopViewModel = remember { StopViewModel(resources) }
            JourneyTrackerTheme {
                JourneyTrackerScreen(appViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyTrackerScreen(appViewModel: StopViewModel) {
    val stopUIState by appViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Journey Tracker") })
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Stop: ${stopUIState.stops[stopUIState.currentStopIndex].stop}",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            val remainingDistance = stopUIState.totalDistance - stopUIState.distanceCovered
            val remainingTime = stopUIState.totalTime - stopUIState.timeCovered
            val displayDistance = if (stopUIState.isKm) remainingDistance else remainingDistance * 0.621371
            val displayDistanceCovered = if (stopUIState.isKm) stopUIState.distanceCovered else stopUIState.distanceCovered * 0.621371
            val unit = if (stopUIState.isKm) "km" else "miles"

            Text(text = "Total Distance covered: %.2f %s".format(displayDistanceCovered, unit))
            Text(text = "Remaining Distance: %.2f %s".format(displayDistance, unit))
            Text(text = "Remaining Time: %.2f hrs".format(remainingTime))

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (stopUIState.distanceCovered / stopUIState.totalDistance).toFloat() },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { appViewModel.nextState() }) {
                    Text(text = "Next Stop")
                }
                Button(onClick = { appViewModel.toggleUnit() }) {
                    Text(text = "Toggle Unit")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Upcoming Stops:", style = MaterialTheme.typography.headlineSmall)

            if (stopUIState.stops.size - stopUIState.currentStopIndex -1 > 3) {
                LazyColumn {
                    items(
                        stopUIState.stops.subList(
                            stopUIState.currentStopIndex + 1,
                            stopUIState.stops.size
                        )
                    ) { stop ->
                        StopItem(stop = stop, isKm = stopUIState.isKm)
                    }
                }
            }
            else {
                Log.i("MainActivity", "SHIVOY: Non lazy text box")
                Column {
                    for (i in (stopUIState.currentStopIndex + 1) until stopUIState.stops.size) {
                        StopItem(stop = stopUIState.stops[i], isKm = stopUIState.isKm)
                    }
                }
            }
        }
    }
}

@Composable
fun StopItem(stop: Stop, isKm: Boolean) {
    val distance = if (isKm) stop.distance ?: 0.0 else (stop.distance?.times(0.621371) ?: 0.0)
    val unit = if (isKm) "km" else "miles"
    val visaRequired = if (stop.visaRequired) "Yes" else "No"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = stop.stop, style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Distance: %.2f %s".format(distance, unit), style = MaterialTheme.typography.bodyMedium)
                Text(text = "Time: %.2f hrs".format(stop.time ?: 0.0), style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "Visa Required: $visaRequired", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JourneyTrackerPreview() {
    JourneyTrackerTheme {
        val appViewModel: StopViewModel = remember { StopViewModel(Resources.getSystem()) }
        JourneyTrackerScreen(appViewModel)
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    JourneyTrackerTheme {
//        Greeting("Android")
//    }
//}
