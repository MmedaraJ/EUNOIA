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
private const val TAG: String = "SoundMediaPlayerService"

/**
 * This service is for playing sounds. A different service is necessary because you need to use
 * multiple media players to play sounds.
 *
 * To use:
 * 1. Reset SoundMediaPlayerService -> resetSoundMediaPlayerService()
 * 2. Set audio uris
 * 3. set volumes
 * 4. Create intent and set action to "PLAY"
 * 5. Call onStartCommand(intent, 0, 0)
 */
class SoundMediaPlayerService:
    Service(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener
{
    private var mediaPlayers = mutableListOf<MediaPlayer?>()
    private var mediaPlayersInitialized = false
    private var mediaPlayersArePlaying = false
    private var mediaPlayersAreLooping = false
    private var audioUris = mutableListOf<Uri?>()
    private var volumes = mutableListOf<Int?>()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action: String = intent.action!!
        when(action) {
            ACTION_PLAY -> {
                for(i in audioUris.indices) {
                    val mediaPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes
                                .Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        try {
                            setDataSource(
                                UserDashboardActivity.getInstanceActivity(),
                                audioUris[i]!!
                            )
                            setVolume(
                                volumes[i]!!.toFloat() / 10,
                                volumes[i]!!.toFloat() / 10
                            )
                            setWakeMode(
                                UserDashboardActivity.getInstanceActivity(),
                                PowerManager.PARTIAL_WAKE_LOCK
                            )
                            setOnPreparedListener(this@SoundMediaPlayerService)
                            prepareAsync() // prepare async to not block main thread
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace();
                        } catch (e: IllegalStateException) {
                            e.printStackTrace();
                        } catch (e: IOException) {
                            e.printStackTrace();
                        }
                    }
                    mediaPlayers.add(mediaPlayer)
                }
            }
        }
        return START_STICKY
    }

    fun pauseMediaPlayers(){
        if(mediaPlayersArePlaying) {
            for (mediaPlayer in mediaPlayers) {
                mediaPlayer!!.pause()
            }
        }
        mediaPlayersArePlaying = false
    }

    fun startMediaPlayers(){
        for(mediaPlayer in mediaPlayers) {
            mediaPlayer!!.start()
        }
        mediaPlayersArePlaying = true
    }

    fun adjustMediaPlayerVolumes(){
        if(mediaPlayersInitialized){
            for(i in mediaPlayers.indices){
                mediaPlayers[i]!!.setVolume(
                    volumes[i]!!.toFloat() / 10,
                    volumes[i]!!.toFloat() / 10
                )
            }
        }
    }

    fun toggleLoopMediaPlayers(){
        for(mediaPlayer in mediaPlayers) {
            if(mediaPlayer!!.currentPosition == mediaPlayer.duration){
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
            mediaPlayer.isLooping = !mediaPlayer.isLooping

        }
        mediaPlayersAreLooping = !mediaPlayersAreLooping
    }

    fun setAudioUris(uris: MutableList<Uri?>){
        audioUris.clear()
        for(audioUri in uris){
            audioUris.add(audioUri)
        }
    }

    fun setVolumes(vols: MutableList<Int?>){
        volumes.clear()
        for(volume in vols){
            volumes.add(volume)
        }
    }

    fun getMediaPlayers(): MutableList<MediaPlayer?>{
        return mediaPlayers
    }

    fun areMediaPlayersInitialized(): Boolean{
        return mediaPlayersInitialized
    }

    fun areMediaPlayersPlaying(): Boolean{
        return mediaPlayersArePlaying
    }

    fun areMediaPlayersLooping(): Boolean{
        return mediaPlayersAreLooping
    }

    fun resetSoundMediaPlayerService(){
        resetMediaPlayers()
        resetAudioUris()
        resetVolumes()
    }

    fun resetMediaPlayers(){
        if(mediaPlayersInitialized) {
            for (mediaPlayer in mediaPlayers) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.reset()
                mediaPlayer.release()
            }
            mediaPlayers.clear()
            mediaPlayersArePlaying = false
            mediaPlayersInitialized = false
        }
    }

    fun resetAudioUris(){
        audioUris.clear()
    }

    fun resetVolumes(){
        volumes.clear()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onPrepared(p0: MediaPlayer?) {
        startMediaPlayers()
        mediaPlayersInitialized = true
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onCompletion(p0: MediaPlayer?) {
        Log.e(TAG, "Media player completed")
    }

    override fun onDestroy() {
        super.onDestroy()
        resetSoundMediaPlayerService()
    }
}