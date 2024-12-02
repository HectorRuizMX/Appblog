plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.appblog"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.appblog"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation ("androidx.core:core-ktx:1.3.2")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.2.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")

    // Firebase
    implementation ("com.google.firebase:firebase-firestore:22.0.1")
    implementation ("com.google.firebase:firebase-storage:19.2.1")
    implementation ("com.google.firebase:firebase-auth:20.0.2")

    // Firebase Crashlytics
    implementation ("com.google.firebase:firebase-crashlytics-ktx:17.4.1")
    implementation ("com.google.firebase:firebase-analytics-ktx:18.0.2")

    // Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.3")

    // Utils
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    // Viewmodel and livedata KTX
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    // Play services coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")

    //Used for StateFlow
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")

    // Lifecycle
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01")


    implementation ("com.google.android.material:material:1.11.0")


    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
}