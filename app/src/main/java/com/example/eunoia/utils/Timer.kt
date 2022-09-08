package com.example.eunoia.utils

import android.os.Handler
import android.os.Looper

class Timer(listener: OnTimerTickListener){
    interface OnTimerTickListener{
        fun onTimerTick(
            durationString: String,
            durationMilliSeconds: Long,
        )

        fun justTick(
            durationString: String,
            durationMilliSeconds: Long
        )
    }

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private lateinit var runnableTick: Runnable

    private var duration = 0L
    private var delay = 100L

    init{
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable, delay)
            listener.onTimerTick(format(), duration)
        }

        runnableTick = Runnable {
            duration += delay
            handler.postDelayed(runnableTick, delay)
            listener.justTick(format(), duration)
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

    fun setDuration(ms: Long): String{
        duration = ms
        handler.removeCallbacks(runnable)
        return format()
    }

    fun startTicking(){
        handler.postDelayed(runnableTick, delay)
    }

    fun stopTicking(){
        handler.removeCallbacks(runnableTick)
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