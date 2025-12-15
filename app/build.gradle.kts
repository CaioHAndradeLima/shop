plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt.android)
}

apply(from = rootProject.file("gradle/jacoco.gradle"))

android {
    namespace = "com.example.shops"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shops"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        // keep language/api set to 1.9 for stability
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        packagingOptions {
            exclude("META-INF/LICENSE.md")
            exclude("META-INF/LICENSE-notice.md")
            excludes += "/META-INF/{LICENSE.md,LICENSE-notice.md}"

            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

dependencies {
    //clean architecture layers
    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))

    // core / platform
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.kotlin.bom))

    // lifecycle / activity
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)

    // Compose BOM + UI
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    // Hilt
    implementation(libs.hilt.android)

    // Navigation, Retrofit, Coil, Compose livedata
    implementation(libs.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.coil.compose)
    implementation(libs.retrofit.gson)
    implementation(libs.hilt.navigation.compose)

    // KAPT / annotation processors
    kapt(libs.hilt.compiler)

    // Android Test
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android)
    kaptAndroidTest(libs.hilt.compiler)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso)

    // Compose androidTest + debug
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}