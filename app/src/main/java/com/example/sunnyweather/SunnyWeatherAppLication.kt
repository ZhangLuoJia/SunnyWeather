package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherAppLication: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        const val TOKEN = "P3GcnGUNzBcXI8zW"
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}