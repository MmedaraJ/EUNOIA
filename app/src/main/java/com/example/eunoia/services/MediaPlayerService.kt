package com.example.eunoia.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.net.toUri
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.ui.bottomSheets.*
import java.io.IOException

private const val ACTION_PLAY: String = "PLAY"
private const val TAG: String = "MediaPlayerService"

class MediaPlayerService: Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    private var mMediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action: String = intent.action!!
        when(action) {
            ACTION_PLAY -> {
                audioRecordedMediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    try {
                        setDataSource(UserDashboardActivity.getInstanceActivity(), recordingFile!!.absolutePath.toUri())
                        Log.i(TAG, "Recoding file stuff: ${recordingFile!!.toUri()}")
                        setVolume(
                            (10).toFloat() / 10,
                            (10).toFloat() / 10
                        )
                        setWakeMode(UserDashboardActivity.getInstanceActivity(), PowerManager.PARTIAL_WAKE_LOCK)
                        setOnPreparedListener(this@MediaPlayerService)
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

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        audioMediaPlayerInitialized.value = true
        mediaPlayer!!.seekTo(0)
        recordingTimeDisplay.value = timer.setDuration(0)
        startAudioRecordedMediaPlayer()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        Log.e(TAG, "Media player error")
        return true
    }

    override fun onCompletion(mediaPlayer: MediaPlayer?) {
        mediaPlayer!!.seekTo(0)
        timer.stopTicking()
        recordingTimeDisplay.value = timer.setDuration(0)
        audioMediaPlayerIsPlaying.value = false
    }
}