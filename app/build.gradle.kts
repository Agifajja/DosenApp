plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.doosen"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.doosen"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {

        implementation("androidx.compose.material3:material3:1.2.0")

        // Kotlinx Serialization
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

        // OkHttp
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

        // Hilt
        implementation("com.google.dagger:hilt-android:2.48")
        implementation(libs.firebase.crashlytics.buildtools)
        kapt("com.google.dagger:hilt-android-compiler:2.48")
        kapt("androidx.hilt:hilt-compiler:1.0.0")
        implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

        implementation("androidx.compose.material:material-icons-extended")

        // Lottie
        implementation("com.airbnb.android:lottie-compose:6.1.0")

        // DataStore
        implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.compose.material3:material3:1.2.1") // atau versi terbaru
    implementation("com.google.android.material:material:1.11.0") // jika belum ada
    implementation("com.google.accompanist:accompanist-themeadapter-material3:0.32.0")

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.navigation.runtime.android)
        implementation(libs.androidx.espresso.core)
        implementation(libs.androidx.animation.core.lint)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)


}