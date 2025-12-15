plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.presentation"
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
    //clean Architecture
    implementation(project(":common"))
    implementation(project(":common-ui"))
    implementation(project(":domain"))

    //Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)

    //lifecycle
    implementation(libs.lifecycle.runtime.compose)

    //Compose preview
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    //Unit testing
    testImplementation(libs.test.junit5.api)
    testRuntimeOnly(libs.test.junit5.engine)

    // Coroutines & Flow
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)

    // Mocking
    testImplementation(libs.test.mockk)

    // Compose androidTest + debug
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.compose.ui.test.manifest)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
