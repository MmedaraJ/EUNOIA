package com.example.eunoia.dashboard.upload_files

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudioconverter.model.AudioFormat
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.components.StandardBlueButton
import com.example.eunoia.ui.theme.EUNOIATheme
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class UploadFilesActivity : ComponentActivity() {
    companion object {
        private const val TAG = "UploadFilesActivity"
        private const val SELECT_MP3 = 10
        private var sound: SoundData? = null
        private var soundAudioPath: String? = null
        private var tenSounds = mutableListOf<Uri>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EUNOIATheme {
                //A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UploadFilesUI()
                }
            }
        }
    }

    @Composable
    fun UploadFilesUI() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            StandardBlueButton(text = "Pouring Rain") {
                //createPouringRainSound()
               /* SoundBackend.listEunoiaSounds{ result ->
                    result.items.forEach { item ->
                        Log.i(TAG, "Item: ${item.key}")
                        *//*SoundBackend.retrieveAudio(item.key){ audioUri ->
                            tenSounds.add(audioUri)
                        }*//*
                    }
                }*/
                /*SoundBackend.deleteAudio("Routine/Sounds/Eunoia/Pouring_Rain")
                SoundBackend.deleteAudio("Routine/Sounds/Eunoia/Pouring_Rain1")*/
                AuthBackend.signOut()
            }
            StandardBlueButton(text = "play sounds") {
                playTenSounds()
            }
        }
    }

    private fun playTenSounds(){
        tenSounds.forEach { audioUri ->
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(applicationContext, audioUri)
                prepare()
                start()
            }
        }
    }

    private fun createPouringRainSound(){
        val sound = SoundObject.Sound(
            UUID.randomUUID().toString(),
            "eunoia",
            "pouring rain",
            "pouring rain",
            "The beautiful sound of the pouring rain",
            "The beautiful sound of the pouring rain. This is the long description.",
            "Routine/Sounds/Eunoia/Pouring_Rain/",
            "null",
            R.drawable.pouring_rain_icon,
            180,
            true,
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"),
        )
        SoundBackend.createSound(sound)
        SoundObject.addSound(sound)
        /*if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){
            selectAudios()
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                3
            )
        }*/
        /*
        SoundBackend.getSoundWithKey("9dc5354a-67f0-4ae3-bb0d-956a7a7c3561"){
            //SoundBackend.querySound()
        }*/
        /*val sounds = SoundObject.sounds().value
        val isEmpty = sounds?.isEmpty() ?: false
        if(isEmpty){
            SoundBackend.querySound()
        }*/
    }

    private val selectAudiosActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                //if multiple audio files selected
                if(data?.clipData != null){
                    val count = data.clipData?.itemCount ?: 0
                    for(i in  0 until count){
                        val audioUri: Uri? = data.clipData?.getItemAt(i)?.uri
                        //Log.i(TAG, "Multiple files selected; ${data.clipData?.getItemAt(i)}")
                        val audioStream = audioUri?.let { contentResolver.openInputStream(it) }
                        val tempFile = File.createTempFile("audio", ".aac")
                        //convertMp3ToAAC(tempFile){ aacFile ->
                        //convertAudioToAAC(tempFile.name, "convertedaac.aac")
                        copyStreamToFile(audioStream!!, tempFile)
                        soundAudioPath = tempFile.absolutePath
                        if(soundAudioPath != null){
                            SoundBackend.storeAudio(soundAudioPath!!, "Routine/Sounds/Eunoia/Pouring_Rain/${tempFile.name}")
                        }
                        //}
                    }
                }
                //if one audio file is selected
                else if(data?.data != null){
                    val audioUri: Uri? = data.data
                    //SoundBackend.storeAudioUri(audioUri, contentResolver)
                    val audioStream = audioUri?.let { contentResolver.openInputStream(it) }
                    val tempFile = File.createTempFile("audio", ".aac")
                    copyStreamToFile(audioStream!!, tempFile)
                    soundAudioPath = tempFile.absolutePath
                    if(soundAudioPath != null){
                        SoundBackend.storeAudio(soundAudioPath!!, "Routine/Sounds/Eunoia/Pouring_Rain/${tempFile.name}")
                    }
                    //Log.i(TAG, "One file selected; ${data.data}")
                    //SoundBackend.deleteAudio("Routine/Sounds/Eunoia/Pouring_Rain")
                    /*val mediaPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        if (mp3Uri != null) {
                            setDataSource(applicationContext, mp3Uri)
                        }
                        prepare()
                        start()
                    }*/
                }
            }
        }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
    }

    private fun selectAudios(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "audio/aac"
        selectAudiosActivityResult.launch(intent)
    }

    private fun storeAudio(filePath: String, key: String) {
        val file = File(filePath)

        Amplify.Storage.uploadFile(
            key,
            file,
            {Log.i(TAG, "Upload Done NIfsfls fksnfja ikd v zdv dkv dhi")},
            {Log.i(TAG, Amplify.Storage.hashCode().toString())}
        )
        //}
    }

    @Preview(
        showBackground = true,
        name = "Light mode"
    )
    @Preview(
        showBackground = true,
        name = "Dark mode",
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    fun WelcomeActivityPreview() {
        EUNOIATheme {
            UploadFilesUI()
        }
    }
}