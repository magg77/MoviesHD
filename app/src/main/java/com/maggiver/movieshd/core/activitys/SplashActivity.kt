package com.maggiver.movieshd.core.activitys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maggiver.movieshd.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen: SplashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashScreen.setKeepOnScreenCondition{false}
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            runBlocking {
                delay(1000)
            }
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}