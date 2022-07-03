package com.example.eunoia

import android.app.Application
import com.example.eunoia.backend.AuthBackend

class Eunoia : Application() {
    private val TAG = "Eunoia Application"
    override fun onCreate() {
        super.onCreate()
        // initialize Amplify when application is starting
        AuthBackend.initialize(applicationContext)
        /*AndroidAudioConverter.load(this, object : ILoadCallback{
            override fun onSuccess() {
                Log.i(TAG, "Great")
            }
            override fun onFailure(error: Exception?) {
                Log.i(TAG, "FFmpeg is not supported by device")
            }
        })*/
    }
}