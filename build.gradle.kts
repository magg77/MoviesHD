// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersionRoot by extra("2.0.21")

    val navVersionRoot by extra("2.8.5")
    val coroutinesAndroid by extra("1.7.0")

    val recyclerView by extra("1.3.1")
    val recyclerviewSelection by extra("1.1.0")

    val hiltVersion by extra("2.51.1")

    val apiUrlBaseDev by extra("https://www.api.com./")
    val apiUrlBaseProd by extra("https://www.api.com./")

    repositories {
        google()
        mavenCentral()
    }

    dependencies {

        classpath(libs.gradle) //AGP version

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra["kotlinVersionRoot"]}")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${rootProject.extra["navVersionRoot"]}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra["hiltVersion"]}")


    }


}

plugins {
    /*alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false*/

    id("com.android.application") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20-Beta1" apply false

    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.google.dagger.hilt.android") version("2.51.1") apply false

}

allprojects {
    repositories {
        //google()
        //mavenCentral()
    }
}