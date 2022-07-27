package com.example.eunoia.dashboard.upload_files

import android.app.Activity
import android.content.Intent
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundApprovalStatus
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.models.*
import com.example.eunoia.sign_in_process.SignInActivity
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
        private var soundAudioPath: String? = null
        private var tenSounds = mutableListOf<Uri>()
        private val _currentUser = MutableLiveData<UserData>()
        var currentUser: LiveData<UserData> = _currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //createEunoiaUser()
        //setSignedInUser()
        //observeCurrentUserChanged()
        observeIsSignedOut()
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

    fun setCurrentUser(newValue: UserData) {
        _currentUser.postValue(newValue)
    }

    private fun observeCurrentUserChanged(){
        currentUser.observe(this) { currentUser ->
            Log.i(TAG, "currentUser changed : $currentUser")
            if (currentUser.username != null) {
                UserObject.setSignedInUser(UserObject.User.from(currentUser))
                Log.i(TAG, "Setting signed in user $currentUser")
            }
        }
    }

    private fun setSignedInUser(){
        UserBackend.getUserWithUsername(Amplify.Auth.currentUser.username){
            setCurrentUser(it!!)
        }
    }

    private fun observeIsSignedOut(){
        AuthBackend.isSignedOut.observe(this) { isSignedOut ->
            // update UI
            Log.i(TAG, "isSignedOut changed : $isSignedOut")
            if (isSignedOut) {
                if (AuthBackend.isSignedOut.value!!) {
                    Log.d(TAG, AuthBackend.isSignedOut.value.toString())
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Log.d(TAG, AuthBackend.isSignedOut.value.toString())
                }
            } else {
                Log.d(TAG, isSignedOut.toString())
            }
        }
    }

    private fun createEunoiaUser(){
        //if(currentUser == null) {
            if (Amplify.Auth.currentUser.username == "eunoia") {
                val user = UserObject.User(
                    UUID.randomUUID().toString(),
                    "eunoia",
                    "Eunoia",
                    "Eunoia",
                    "",
                    "mmedarajosiah@gmail.com",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    true,
                    "rookie"
                )
                UserBackend.createUser(user){

                }
                UserObject.setSignedInUser(user)
                Log.i(TAG, UserObject.signedInUser().value.toString())
            }
        //}
    }

    @Composable
    fun UploadFilesUI() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            StandardBlueButton(text = "Pouring Rain") {
                //createEunoiaUser()
                createPouringRainSound()
                //AuthBackend.signOut()
            }
            StandardBlueButton(text = "play sounds") {
                AuthBackend.signOut()
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

    private fun createPresetNameAndVolumesMapData(presetData: PresetData){
        val originalVolumes = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            "original_volumes",
            listOf(5, 5, 5, 5, 5, 5, 5, 5, 5, 5),
            presetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(originalVolumes){}
        val currentVolumes = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            "current_volumes",
            listOf(5, 5, 5, 5, 5, 5, 5, 5, 5, 5),
            presetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(currentVolumes){}

        val preset1 = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            "preset1",
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            presetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(preset1){}

        val preset2 = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            "preset2",
            listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1),
            presetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(preset2){}

        val preset3 = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            "preset3",
            listOf(10, 9, 8, 7, 6, 1, 2, 3, 4, 5),
            presetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(preset3){}

        val preset4 = PresetNameAndVolumesMapObject.PresetNameAndVolumesMap(
            "preset4",
            listOf(10, 8, 6, 4, 2, 1, 2, 4, 6, 8, 10),
            presetData
        )
        PresetNameAndVolumesMapBackend.createPresetNameAndVolumesMap(preset4){}
    }

    private fun createSoundPreset(soundData: SoundData){
        val preset = PresetObject.Preset(
            UUID.randomUUID().toString(),
            soundData
        )
        PresetBackend.createPreset(preset){
            createPresetNameAndVolumesMapData(it)
        }
    }

    private fun createPouringRainSound(){
        val sound = SoundObject.Sound(
            UUID.randomUUID().toString(),
            UserObject.signedInUser().value!!,
            "pouring rain",
            "pouring rain",
            "The beautiful sound of the pouring rain",
            "The beautiful sound of the pouring rain. This is the long description.",
            "Routine/Sounds/Eunoia/Pouring_Rain/",
            R.drawable.pouring_rain_icon,
            180,
            true,
            listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"),
            SoundApprovalStatus.APPROVED
        )
        SoundBackend.createSound(sound){
            createUserSound(it)
            createSoundPreset(it)
        }

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
    }

    private fun createUserSound(soundData: SoundData){
        val userSoundModel = UserSoundObject.UserSoundModel(
            UUID.randomUUID().toString(),
            SoundObject.Sound.from(soundData),
            UserObject.signedInUser().value!!
        )
        UserSoundBackend.createUserSound(userSoundModel){

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