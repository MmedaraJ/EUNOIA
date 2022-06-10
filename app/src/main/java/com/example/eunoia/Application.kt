package com.example.eunoia

import android.app.Application
import android.content.Intent
import android.util.Log
import com.amplifyframework.core.Amplify
import com.example.eunoia.backend.Backend
import com.example.eunoia.models.UserData

class Eunoia : Application() {
    override fun onCreate() {
        super.onCreate()
        // initialize Amplify when application is starting
        Backend.initialize(applicationContext)
    }
}