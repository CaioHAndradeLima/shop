plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp)
}
apply(from = rootProject.file("gradle/jacoco.gradle"))

android {
    namespace = "com.example.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    //Clean Architecture layers
    implementation(project(":common"))
    implementation(project(":domain"))

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Request HTTPS
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Unit testing
    testImplementation(libs.test.junit5.api)
    testRuntimeOnly(libs.test.junit5.engine)

    // Coroutines & Flow
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)

    // Mocking
    testImplementation(libs.test.mockk)
}

kapt {
    correctErrorTypes = true
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
