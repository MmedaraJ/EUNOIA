package com.example.eunoia.dashboard.upload_files

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
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
import com.amplifyframework.datastore.generated.model.*
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.models.*
import com.example.eunoia.sign_in_process.SignInActivity
import com.example.eunoia.ui.components.StandardBlueButton
import com.example.eunoia.ui.navigation.globalViewModel
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
        lateinit var componentActivity: ComponentActivity
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
                    Amplify.Auth.currentUser.userId,
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

    private fun createUserPreset(presetData: SoundPresetData){
        val userSoundPresetModel = UserSoundPresetObject.UserSoundPresetModel(
            UUID.randomUUID().toString(),
            UserObject.signedInUser().value!!,
            SoundPresetObject.SoundPreset.from(presetData),
        )
        UserSoundPresetBackend.createUserSoundPreset(userSoundPresetModel){

        }
    }

    private fun createSoundPreset(soundData: SoundData){
        val originalVolumes = SoundPresetObject.SoundPreset(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            globalViewModel!!.currentUser!!.id,
            "original_volumes",
            listOf(5, 5, 5, 5, 5, 5, 5, 5, 5, 5),
            soundData.id,
            SoundPresetPublicityStatus.PUBLIC
        )
        SoundPresetBackend.createSoundPreset(originalVolumes){
            createUserPreset(it)
        }

        val preset1 = SoundPresetObject.SoundPreset(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            globalViewModel!!.currentUser!!.id,
            "preset1",
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            soundData.id,
            SoundPresetPublicityStatus.PUBLIC
        )
        SoundPresetBackend.createSoundPreset(preset1){
            createUserPreset(it)
        }

        val preset2 = SoundPresetObject.SoundPreset(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            globalViewModel!!.currentUser!!.id,
            "preset2",
            listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1),
            soundData.id,
            SoundPresetPublicityStatus.PUBLIC
        )
        SoundPresetBackend.createSoundPreset(preset2){
            createUserPreset(it)
        }

        val preset3 = SoundPresetObject.SoundPreset(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            globalViewModel!!.currentUser!!.id,
            "preset3",
            listOf(10, 9, 8, 7, 6, 1, 2, 3, 4, 5),
            soundData.id,
            SoundPresetPublicityStatus.PUBLIC
        )
        SoundPresetBackend.createSoundPreset(preset3){
            createUserPreset(it)
        }

        val preset4 = SoundPresetObject.SoundPreset(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel!!.currentUser!!),
            globalViewModel!!.currentUser!!.id,
            "preset4",
            listOf(10, 8, 6, 4, 2, 1, 2, 4, 6, 8),
            soundData.id,
            SoundPresetPublicityStatus.PUBLIC
        )
        SoundPresetBackend.createSoundPreset(preset4){
            createUserPreset(it)
        }
    }

    private fun createPouringRainSound(){
        val sound = SoundObject.Sound(
            UUID.randomUUID().toString(),
            UserObject.signedInUser().value!!,
            UserObject.signedInUser().value!!.id,
            "pouring rain",
            "pouring rain",
            "The beautiful sound of the pouring rain",
            "The beautiful sound of the pouring rain. This is the long description.",
            "Routine/Sounds/Eunoia/Pouring_Rain/",
            R.drawable.pouring_rain_icon,
            (0xFFEBBA9A).toInt(),
            180L,
            true,
            listOf("curry"),
            listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"),
            SoundApprovalStatus.APPROVED
        )
        SoundBackend.createSound(sound){
            createUserSound(it)
            createSoundPreset(it)
        }
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

    fun selectAudio(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/mp3"
        selectAudioActivityResult.launch(intent)
    }

    private val selectAudioActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                val audioUri: Uri? = data!!.data
                //SoundBackend.storeAudioUri(audioUri, contentResolver)
                val audioStream = audioUri?.let { contentResolver.openInputStream(it) }
                val tempFile = File.createTempFile("audio", ".aac")
                copyStreamToFile(audioStream!!, tempFile)
                soundAudioPath = tempFile.absolutePath
                if(soundAudioPath != null){
                    SoundBackend.storeAudio(soundAudioPath!!, "Routine/Sounds/Eunoia/Pouring_Rain/${tempFile.name}"){}
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