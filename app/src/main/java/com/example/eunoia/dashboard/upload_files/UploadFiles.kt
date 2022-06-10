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
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.components.StandardBlueButton
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.R
import com.example.eunoia.backend.SoundBackend
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
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
                createPouringRainSound()
            }
        }
    }

    private fun createPouringRainSound(){
        /*val sound = SoundObject.Sound(
            UUID.randomUUID().toString(),
            "mmedaraj",
            "pouring rain1",
            "pouring rain1",
            "The beautiful sound of the pouring rain1",
            "The beautiful sound of the pouring rain1",
            "Routine/Sounds/Eunoia/Pouring_Rain1",
            "",
            R.drawable.pouring_rain_icon,
            180,
            true,
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
        )
        SoundBackend.createSound(sound)
        SoundObject.addSound(sound)*/
        if(ContextCompat.checkSelfPermission(
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
        }
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
                        Log.i(TAG, "Multiple files selected; ${data.clipData?.getItemAt(i)}")
                        val audioStream = audioUri?.let { contentResolver.openInputStream(it) }
                        val tempFile = File.createTempFile("audio", ".mp3")
                        copyStreamToFile(audioStream!!, tempFile)
                        soundAudioPath = tempFile.absolutePath
                        if(soundAudioPath != null){
                            SoundBackend.storeAudio(soundAudioPath!!, "Routine/Sounds/Eunoia/Pouring_Rain1")
                        }
                    }
                }
                //if one audio file is selected
                else if(data?.data != null){
                    val mp3Uri: Uri? = data.data
                    Log.i(TAG, "One file selected; ${data.data}")
                    val mediaPlayer = MediaPlayer().apply {
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
                    }
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
        intent.type = "audio/*"
        selectAudiosActivityResult.launch(intent)
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