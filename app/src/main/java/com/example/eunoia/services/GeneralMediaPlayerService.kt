package com.example.eunoia.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.example.eunoia.dashboard.home.UserDashboardActivity
import java.io.IOException

private const val ACTION_PLAY: String = "PLAY"
private const val TAG: String = "GeneralMediaPlayerService"

class GeneralMediaPlayerService:
    Service(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener
{
    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayerInitialized = false
    private var mediaPlayerIsPlaying = false
    private var audioUri: Uri? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action: String = intent.action!!
        when(action) {
            ACTION_PLAY -> {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    try {
                        setDataSource(UserDashboardActivity.getInstanceActivity(), audioUri!!)
                        setVolume(
                            (10).toFloat() / 10,
                            (10).toFloat() / 10
                        )
                        setWakeMode(UserDashboardActivity.getInstanceActivity(), PowerManager.PARTIAL_WAKE_LOCK)
                        setOnPreparedListener(this@GeneralMediaPlayerService)
                        prepareAsync() // prepare async to not block main thread
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace();
                    } catch (e: IllegalStateException) {
                        e.printStackTrace();
                    } catch (e: IOException) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return START_STICKY
    }

    fun pauseMediaPlayer(){
        if(mediaPlayerIsPlaying) {
            mediaPlayer!!.pause()
            mediaPlayerIsPlaying = false
        }
    }

    fun startMediaPlayer(){
        mediaPlayer!!.start()
        mediaPlayerIsPlaying = true
    }

    fun isMediaPlayerInitialized(): Boolean{
        return mediaPlayerInitialized
    }

    fun isMediaPlayerPlaying(): Boolean{
        return mediaPlayerIsPlaying
    }

    fun setAudioUri(uri: Uri){
        audioUri = uri
    }

    fun getMediaPlayer(): MediaPlayer?{
        return mediaPlayer
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer!!.start()
        mediaPlayerIsPlaying = true
        //mediaPlayer.seekTo(0)
        mediaPlayerInitialized = true
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        Log.e(TAG, "Media player error")
        return true
    }

    override fun onCompletion(mediaPlayer: MediaPlayer?) {
        /*mediaPlayer!!.pause()
        mediaPlayer.seekTo(0)*/
        Log.i(TAG, "Media player completed")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayerInitialized) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayerIsPlaying = false
            mediaPlayerInitialized = false
            audioUri = null
        }
    }
}