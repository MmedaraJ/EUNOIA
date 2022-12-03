package com.example.eunoia

import android.app.Application
import android.util.Log
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.eunoia.backend.AuthBackend
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Eunoia : Application() {
    private val TAG = "Eunoia Application"
    companion object {
        lateinit var instance: Eunoia private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // initialize Amplify when application is starting
        AuthBackend.initialize(applicationContext, this)
        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(applicationContext))
        }
    }
}