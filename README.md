# Flickr Gallery App

A simple, clean Android app built with **Jetpack Compose**, **Hilt**, **Retrofit**, and an **MVI architecture**.  
The app fetches images from the Flickr API, supports searching, pagination, detail screens, loading states, and error handling with retry.


---

## Demo

Below is an example showcasing the app's core experience:

(Insert your screenshots or screen recordings here.)

---

## How to Download the App

1. Have an android device (physical or terminal)
2. Clone the repository using this command in your termal: `git clone https://github.com/yourusername/flickr-gallery-app.git`
3. Run the project in Android Studio
   
---

## Features

- **MVI Architecture** with a custom `StateHostScreen` system
- **Home Screen**
    - Search bar with live query updates
    - Grid of Flickr photos
    - Infinite scroll pagination
    - Loading, Empty, and Error-with-Retry states
- **Detail Screen**
    - Displays the selected photo full-screen
    - Back navigation
- **Error Handling**
    - Retry option on both Home & Detail screens
- **UI**
    - Material 3 components
    - Full white background
    - Status + navigation bar padding support
- **Networking**
    - Retrofit + Gson
    - Flickr REST API
- **Image Loading**
    - Coil Compose

---

## Architecture Overview

The app uses a unidirectional MVI flow:


Core layers:

- **UI layer:** Jetpack Compose screens
- **ViewModel:** Handles actions, paging, loading logic, and effects
- **Repository:** Fetches data from Flickr API
- **Domain Model:** Clean model for UI consumption
- **MVI Framework:** `ViewModelDelegate`, `StateReducer`, `EffectDispatcher`

---

## Screens

### **Home Screen**
- Search photos
- Browse paginated results
- Click photo → opens detail screen
- Handles:
    - Loading
    - Pagination loading
    - Empty results
    - Network error (retry button)

### **Detail Screen**
- Full-screen image
- Shows title (or fallback string)
- Back navigation
- White background

---

## Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Hilt (Dagger)**
- **Retrofit / Gson**
- **Coil**
- **Coroutines + Flow**
- **Custom MVI framework**
- **Navigation Compose**

---

## Running the App

1. Add your Flickr API credentials inside your `gradle.properties`:
2. Build & run the project in Android Studio (Giraffe or newer recommended).

---

## Unit Tests

A full test suite exists for the ViewModel using:

- **Coroutines Test**
- **Turbine**
- **Fake Repository**

Tests cover:

- Initial load success
- Initial load error
- Search flow
- Navigation effects
- Retry behavior

## License

This project is for educational and portfolio purposes.  
Feel free to modify and extend it for your own learning.
