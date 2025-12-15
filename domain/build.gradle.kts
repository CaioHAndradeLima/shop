plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt.android) apply false
}
apply(from = rootProject.file("gradle/jacoco.gradle"))

android {
    namespace = "com.example.domain"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":common"))

    //flow/coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Hilt
    implementation(libs.hilt.android)

    //Unit testing
    testImplementation(libs.test.junit5.api)
    testRuntimeOnly(libs.test.junit5.engine)

    // Coroutines & Flow
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)

    // Mocking
    testImplementation(libs.test.mockk)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
