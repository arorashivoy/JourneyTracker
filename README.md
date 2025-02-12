# Journey Tracker Android App (XML + Kotlin)

## Overview
This is an Android app built using XML and Kotlin to track a journey with multiple stops. The app displays information such as the stops, distances, time left, visa requirements, and overall progress. It allows users to switch between kilometers and miles and mark stops as reached.

## Features
- Displays journey stops, distances, time left, and visa requirements.
- Supports unit conversion between kilometers and miles.
- Allows users to progress through the journey by marking stops as reached.
- Displays total distance covered and remaining distance.
- Uses a ProgressBar to indicate journey progress.
- Implements a traditional list for fewer than 3 stops and a RecyclerView for 3 or more stops.
- Reads the list of stops from a text file stored as a resource.
- Works on both Android devices and the Android emulator.

## Implementation Details
### 1. **MainActivity.kt**
- Handles the UI logic and interactions.
- Loads stops from a resource file (`res/raw/stops.txt`).
- Updates UI elements dynamically based on the current stop.
- Implements a `RecyclerView` adapter for dynamic list handling.
- Supports toggling between kilometers and miles.
- Tracks journey progress using a `ProgressBar`.

### 2. **StopAdapter.kt**
- Custom adapter for `RecyclerView`.
- Displays stop details including name, distance, time, and visa requirement.
- Updates distance dynamically based on selected unit (km/miles).

### 3. **XML Layout Files**
- `activity_main.xml`: Defines the main layout including `TextView`, `RecyclerView`, `ProgressBar`, and buttons.
- `stop_item.xml`: Layout for individual journey stops displayed in the list.

## Running the App
### Prerequisites
- **Android Studio** (latest version recommended)
- **Emulator or Physical Android Device**

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/arorashivoy/JourneyTracker.git
   ```
2. Checkout the Compose branch:
   ```sh
   git checkout xml
   ```
3. Open the project in **Android Studio**.
4. Run the app on an emulator or physical device.

