package com.example.eunoia.utils

import android.os.Handler
import android.os.Looper
import android.util.Log

class BedtimeStoryTimer(listener: OnBedtimeStoryTimerTickListener){
    interface OnBedtimeStoryTimerTickListener{
        fun onBedtimeStoryTimerTick(
            durationString: String,
            durationMilliSeconds: Long,
        )
    }

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var duration = 0L
    private var delay = 100L
    private var maxDuration = 100000L

    private val TAG = "BedtimeStoryTimer"

    init{
        runnable = Runnable {
            duration += delay
            if(duration > maxDuration){
                stop()
            }else {
                handler.postDelayed(runnable, delay)
                listener.onBedtimeStoryTimerTick(format(), duration)
            }
        }
    }

    fun start(){
        handler.postDelayed(runnable, delay)
    }

    fun pause(){
        handler.removeCallbacks(runnable)
    }

    fun stop(){
        duration = 0L
        handler.removeCallbacks(runnable)
    }

    fun getDuration(): Long{
        Log.i(TAG, "duration = $duration")
        return duration
    }

    fun setDuration(ms: Long): String{
        duration = ms
        handler.postDelayed(runnable, delay)
        handler.removeCallbacks(runnable)
        return format()
    }

    fun setMaxDuration(ms: Long){
        maxDuration = ms
    }

    fun format(): String {
        val millis = duration % 1000
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        val hours = (duration / (1000 * 60 * 60))

        return if (hours > 0)
            "%02d:%02d:%02d.%02d".format(hours, minutes, seconds, millis/10)
        else
            "%02d:%02d.%02d".format(minutes, seconds, millis/10)
    }
}