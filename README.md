# Journey Tracker App (Compose Version)

## Overview
This is the Compose version of the Journey Tracker app for **Mobile Computing - Winter 2024, Assignment 1**. The app helps users track their journey progress, including multiple stops, distances, time left, and visa requirements. It provides functionality to switch between distance units (km/miles) and mark progress to the next stop.

## Features
- **Displays journey stops** with details on distance, time left, and visa requirements.
- **Toggle button for unit conversion** between kilometers and miles.
- **Button to mark arrival at the next stop**, updating progress accordingly.
- **Progress tracking using a progress bar**.
- **Lazy list for journeys with more than 3 stops**, improving UI efficiency.
- **Reads stop details from a resource file**.
- **Compatible with both Android devices and emulators**.

## Implementation Details
### 1. Data Model
- `Stop`: Represents a journey stop with attributes like `stop`, `distance`, `time`, and `visaRequired`.
- `StopUIState`: Stores UI-related state, including `currentStopIndex`, `distanceCovered`, `timeCovered`, and total journey details.

### 2. ViewModel (`StopViewModel`)
- Initializes and maintains the UI state.
- Reads stop details from a raw resource file (`stops.txt`).
- Provides methods to update progress (`nextState()`) and switch units (`toggleUnit()`).

### 3. UI Components
#### `JourneyTrackerScreen`
- Displays the current stop and journey progress.
- Includes buttons for `Next Stop` and `Toggle Unit`.
- Shows progress bar and upcoming stops.
- Uses `LazyColumn` for efficient rendering of stops when the journey has more than three stops.

#### `StopItem`
- Displays details of each stop inside a `Card`.
- Shows distance, time left, and visa requirement status.

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
   git checkout compose
   ```
3. Open the project in **Android Studio**.
4. Run the app on an emulator or physical device.
