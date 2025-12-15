
# Shops App

A modern Android application built with **Clean Architecture**, **Jetpack Compose**, **Kotlin Coroutines**, and **Flow**, demonstrating scalable architecture, lifecycle-aware state management, and a strong testing strategy.

This project was designed to be **easy to understand, easy to test, and easy to extend**, following current Android best practices.

---

## Overview

The **Shops App** displays a list of shops and allows users to view shop details.

It demonstrates:

- Clean Architecture with clear module boundaries  
- Local-first and remote-first data strategies  
- Lifecycle-aware UI state management  
- Modern navigation with Jetpack Compose  
- Comprehensive unit and UI testing  
- CI/CD automation with GitHub Actions  

The app runs on **Android emulators and physical devices** without additional setup.

---

## Screenshots & Demo

<!-- Replace the placeholders below with your real assets -->

### Screenshots
<p float="left">
  <img src="docs/screenshot_list.png" width="250" />
  <img src="docs/screenshot_details.png" width="250" />
</p>

### Demo Video
🎥 [Watch short demo video](docs/demo.mp4)

---

## Architecture

The project follows **Clean Architecture** and is organized into **independent Gradle modules**:

```
app/
data/
domain/
presentation/
common/
common-ui/
```

### Why this architecture?

- Clear separation of concerns  
- Testability at every layer  
- Framework-independent business logic  
- Scalable for larger teams and features  

---

## Modules Overview

### Domain

Pure Kotlin module with **no Android dependencies**.

**Responsibilities:**
- Business rules
- Entities
- Use cases
- Error and result modeling

---

### Data

Handles all **data sources and implementations**.

**Responsibilities:**
- Remote API access
- Local database access (Room)
- DTO ↔ Entity ↔ Domain mapping
- Repository implementations

---

### Presentation

Contains **UI and state management** using Jetpack Compose.

**Responsibilities:**
- UI rendering
- ViewModels
- UI state & events
- Error mapping for UI

Stateful and stateless composables are explicitly separated to improve testability.

---

### app

Application entry point and navigation.

---

## State Management

The app uses **Kotlin Flow** and **StateFlow** for state handling.

<details>
<summary><b>ViewModel example</b></summary>

```kotlin
@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopUseCase: ShopUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _shopState = MutableStateFlow<ShopState>(ShopState.Loading)
    val shopState = _shopState.asStateFlow()

    private val shopId: String =
        requireNotNull(savedStateHandle["id"]) {
            "Shop id is required"
        }

    init {
        loadShop()
    }

    fun on(event: ShopEvent) {
        when (event) {
            ShopEvent.Retry -> loadShop()
        }
    }

    private fun loadShop() {
        shopUseCase(shopId)
            .onEach { result ->
                _shopState.value = when (result) {
                    is RequestResource.Loading ->
                        ShopState.Loading

                    is RequestResource.Success ->
                        ShopState.Show(result.data)

                    is RequestResource.Error ->
                        ShopState.Error(result.failure)
                }
            }
            .launchIn(viewModelScope)
    }
}
```
</details>

---

## Testing

- Unit tests for domain, data, and ViewModels  
- Compose UI tests for stateless UI  
- JUnit 5 + Coroutines Test  

---

## CI/CD

GitHub Actions pipeline runs:

- Build
- Unit tests
- UI tests

<details>
<summary><b>CI workflow</b></summary>

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    name: Build & Test
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: 17

      - name: Set up Android SDK
        uses: android-actions/setup-android@v2

      - name: Make Gradle executable
        run: chmod +x gradlew

      - name: Build Debug
        run: ./gradlew assembleDebug

      - name: Run Unit Tests with Coverage
        run: ./gradlew clean testDebugUnitTest jacocoTestReport

      - name: Generate Coverage Badge
        uses: cicirello/jacoco-badge-generator@v2
        with:
          generate-branches-badges: false
          jacoco-csv-file: app/build/reports/jacoco/testDebugUnitTestCoverage.csv
          generate-coverage-badge: true


      - name: Upload badge to GitHub
        uses: actions/upload-artifact@v4
        with:
          name: coverage-badge
          path: .github/badges/jacoco.svg
          overwrite: true

  ui-tests:
    name: Run Espresso Tests
    runs-on: ubuntu-latest
    steps:
      - name: checkout
        uses: actions/checkout@v4

      - name: Enable KVM
        run: |
          echo 'KERNEL=="kvm", GROUP="kvm", MODE="0666", OPTIONS+="static_node=kvm"' | sudo tee /etc/udev/rules.d/99-kvm4all.rules
          sudo udevadm control --reload-rules
          sudo udevadm trigger --name-match=kvm

      - name: run tests
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 29
          script: ./gradlew jacocoAndroidReport
```
</details>

---

## Setup

1. Clone repository
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or device

