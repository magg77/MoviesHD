plugins {
    /*alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)*/
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.devtools.ksp")
    id("kotlin-parcelize")

    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization") version "2.0.21"

    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.maggiver.movieshd"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.maggiver.movieshd"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            manifestPlaceholders += mapOf()
            manifestPlaceholders["cleartextTrafficPermitted"] = true
            resValue("string", "app_name", "MoviesHD")
            buildConfigField("String", "API_MOVIESHD_APP_PROD", "\"${rootProject.extra["apiUrlBaseProd"]}\"")

            //signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug"){
            applicationIdSuffix = ".Debug"
            isDebuggable = true

            manifestPlaceholders += mapOf()
            resValue("string", "app_name", "MoviesHD[DEBUG]")
            buildConfigField("String", "API_MOVIESHD_APP_DEBUG", "\"${rootProject.extra["apiUrlBaseDev"]}\"")
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
        viewBinding = true
    }
}

dependencies {

    //  core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.constraintlayout)

    //  jetpack observable
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //  jetpack navigation  Views/Fragments integration
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Feature module support for Fragments
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    //  coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutinesAndroid"]}")

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.activity)

    //    hilt di
    implementation(libs.hilt.android)
    ksp("com.google.dagger:hilt-compiler:${rootProject.extra["hiltVersion"]}")

    //client retrofit network
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation (libs.okhttp)
    implementation(libs.logging.interceptor) //log interceptor

    //  design style
    implementation(libs.material)
    //  splash-screen
    implementation(libs.androidx.core.splashscreen)
    //  show-images
    implementation(libs.glide)

    //  test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}