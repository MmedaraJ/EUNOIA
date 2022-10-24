package com.example.eunoia

import android.app.Application
import android.util.Log
import com.example.eunoia.backend.AuthBackend

class Eunoia : Application() {
    private val TAG = "Eunoia Application"

    override fun onCreate() {
        super.onCreate()
        // initialize Amplify when application is starting
        AuthBackend.initialize(applicationContext, this)
    }
}