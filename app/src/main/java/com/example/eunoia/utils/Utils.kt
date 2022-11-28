package com.example.eunoia.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.services.GeneralMediaPlayerService

private const val TAG = "Utils"

fun formatMilliSecond(milliseconds: Long): String{
    var finalTimerString = ""
    var secondsString = ""

    // Convert total duration into time
    val hours = (milliseconds / (1000 * 60 * 60)).toInt()
    val minutes = ((milliseconds % (1000 * 60 * 60)) / (1000 * 60)).toInt()
    val seconds = ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()

    // Add hours if there
    if(hours > 0) finalTimerString = "$hours :"
    // Prepending 0 to seconds if it is one digit
    secondsString = if (seconds < 10) "0$seconds" else "$seconds"
    finalTimerString = "$finalTimerString $minutes : $secondsString"

    return finalTimerString
}

fun timerFormatMS(milliseconds: Long): String{
    val millis = milliseconds % 1000
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    val hours = (milliseconds / (1000 * 60 * 60))

    return if (hours > 0)
        "%02d:%02d:%02d.%02d".format(hours, minutes, seconds, millis/10)
    else
        "%02d:%02d.%02d".format(minutes, seconds, millis/10)
}

fun displayRoutineNameForBottomSheet(userRoutineRelationship: UserRoutineRelationship): String{
    var goodDisplayName = ""
    if(userRoutineRelationship.userRoutineRelationshipRoutine.displayName != null){
        val displayName = userRoutineRelationship.userRoutineRelationshipRoutine.displayName
        var textSize = displayName.length
        var i = 0
        val maxLength = 12

        while(textSize > 0) {
            var limit = i+maxLength+1
            if(limit > displayName.length){
                limit = displayName.length
            }

            if(displayName.substring(i, limit).isNotEmpty()) {
                goodDisplayName = goodDisplayName + displayName.substring(i, limit) + "\n"
            }

            i += limit
            if(i > displayName.length){
                i = displayName.length
            }
            textSize -= maxLength
        }
    }
    return goodDisplayName
}

fun getRandomString(length: Int) : String {
    Log.i(TAG, "Getting random String")
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun retrieveUriDuration(
    pathStr: String,
    context: Context
): Int {
    val uri = Uri.parse(pathStr)
    val mmr = MediaMetadataRetriever()
    mmr.setDataSource(context, uri)
    val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    return durationStr!!.toInt()
}

fun getCurrentlyPlayingTime(
    generalMediaPlayerService: GeneralMediaPlayerService
): Int{
    var result = 0
    if(generalMediaPlayerService.isMediaPlayerInitialized()){
        if(
            generalMediaPlayerService.getMediaPlayer()!!.currentPosition <=
            generalMediaPlayerService.getMediaPlayer()!!.duration
        ) {
            result = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
            generalMediaPlayerService.onDestroy()
            Log.i(TAG, "Resultz = $result")
        }
    }
    return result
}