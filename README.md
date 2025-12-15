
# Shops App

A modern Android application built with **Clean Architecture**, **Jetpack Compose**, **Kotlin Coroutines**, and **Flow**, demonstrating scalable architecture, lifecycle-aware state management, and a strong testing strategy.

This project was designed to be **easy to understand, easy to test, and easy to extend**, following current Android best practices.

[![Shop](https://github.com/CaioHAndradeLima/shop/actions/workflows/android.yml/badge.svg)](https://github.com/CaioHAndradeLima/shop/actions/workflows/android.yml)
![Coverage](.github/badges/jacoco.svg)

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

### Screenshots


<p float="left">
  
<img width="216" height="480" alt="Screenshot_20251214_214301" src="https://github.com/user-attachments/assets/28796c1d-a948-4dba-badd-2c7bc9e41d51" />
<img width="216" height="480" alt="Screenshot_20251214_214315" src="https://github.com/user-attachments/assets/37c77ec1-08ce-4208-94da-7282c06fa907" />

</p>

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
    name: Run UI Tests
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

---

## Local-first pattern

This project adopts a local-first strategy for observing data, combined with a background remote refresh.
The main goal is to minimize perceived loading time and provide an immediate UI response, even when network conditions are poor.
Instead of waiting for a remote request to complete, the UI always observes local data first (Room).
Remote data is fetched in parallel and persisted locally when available.
This approach provides:

1. Faster first render
2. Offline support
3. Smoother UI state transitions
4. Reduced loading states for returning users


<b>How it works</b>

The repository exposes a Flow backed by the local database.
When collection starts, a remote request is triggered in the background to refresh local data.

<details>
<summary><b>Local-first repository implementation</b></summary>

```kotlin
override fun observeShops(): Flow<List<Shop>> = localDataSource
    .observeShops()
        .map { entities -> entities.map { it.toDomain() } }
        .onStart {
            applicationScope.launch {
                val result = callApi {
                    remoteDataSource.getShops()
                }

                result.getOrNull()?.takeIf { it.isNotEmpty() }?.let { shops ->
                    localDataSource.updateShops(
                        shops.map { it.toEntity() }
                    )
                }
            }
        }.distinctUntilChanged()
```
</details>

<b>With this design, the UI:</b>

* Immediately displays cached data
* Automatically updates when fresh data arrives
* Avoids unnecessary loading indicators
* Remains resilient to network failures

## Next steps and possible improvements
  This project is intentionally kept focused, but there are several improvements planned as next steps:
  
  * Update dependencies: Keep Gradle, Kotlin, Compose, and AndroidX libraries aligned with the latest stable versions.
  * Reduce experimental annotations
  * Increase test coverage adding more edge-case tests for repositories
  * Expand UI tests for error and configuration scenarios
  * Improve coverage for navigation and state restoration
  * Performance and UX refinements improving image loading strategies
  * Add paging support if data grows
  * Introduce animation polish for transitions
  * Fix warnings in gradle build output


Source code
The complete source code is available on GitHub:
Repository:
https://github.com/CaioHAndradeLima/shop
