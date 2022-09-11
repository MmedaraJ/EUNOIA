package com.example.eunoia.dashboard.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.backend.UserRoutineBackend
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.createPrayer.*
import com.example.eunoia.create.createSound.*
import com.example.eunoia.create.createSound.selectedIndex
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.sign_in_process.SignInActivity
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.MultiBottomNavApp
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.ui.theme.Grey
import com.example.eunoia.ui.theme.Peach
import com.example.eunoia.ui.theme.White
import com.example.eunoia.utils.Timer
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*

class UserDashboardActivity : ComponentActivity(), Timer.OnTimerTickListener {
    private val _currentUser = MutableLiveData<UserData>(null)
    var currentUser: LiveData<UserData> = _currentUser

    companion object{
        val TAG = "UserDashboardActivity"
        lateinit var weakActivity: WeakReference<UserDashboardActivity>

        fun getInstanceActivity(): UserDashboardActivity{
            return weakActivity.get()!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeCurrentUserChanged()
        setSignedInUser()
        observeIsSignedOut()
        weakActivity = WeakReference<UserDashboardActivity>(this@UserDashboardActivity)
        setContent {
            MultiBottomNavApp()
        }
    }

    fun selectAudio(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.type = "audio/aac"
        selectAudioActivityResult.launch(intent)
    }

    private val selectAudioActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                if(data?.data != null){
                    val audioUri: Uri? = data.data
                    fileUris[selectedIndex]!!.value = audioUri!!
                    Log.i(TAG, "Audio Uri ==>> $audioUri")
                    val audioStream = audioUri.let {
                        Log.i(TAG, "$it")
                        contentResolver.openInputStream(it)
                    }
                    val tempFile = File.createTempFile("audio", ".aac")
                    copyStreamToFile(audioStream!!, tempFile)
                    val mdt = MediaMetadataRetriever()
                    mdt.setDataSource(tempFile.absolutePath)
                    var durationStr = mdt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    audioFileLengthMilliSeconds[selectedIndex]!!.value = durationStr!!.toLong()
                    uploadedFiles[selectedIndex]!!.value = tempFile
                    fileColors[selectedIndex]!!.value = Peach
                    Log.i(TAG, "Names 3 ==>> ${uploadedFiles[selectedIndex]!!.value.absolutePath}")
                    Log.i(TAG, "Names 4 ==>> ${uploadedFiles[selectedIndex]!!.value.path}")
                }
            }
        }

    fun selectAudioBedtimeStory(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/aac"
        selectAudioPrayerActivityResult.launch(intent)
    }

    private val selectAudioBedtimeStoryActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                if(data?.data != null){
                    val audioUri: Uri? = data.data
                    fileUriBedtimeStory.value = audioUri!!
                    Log.i(TAG, "Audio Uri ==>> $audioUri")
                    val audioStream = audioUri.let {
                        Log.i(TAG, "$it")
                        contentResolver.openInputStream(it)
                    }
                    val tempFile = File.createTempFile("audio", ".aac")
                    copyStreamToFile(audioStream!!, tempFile)
                    val mdt = MediaMetadataRetriever()
                    mdt.setDataSource(tempFile.absolutePath)
                    var durationStr = mdt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    audioFileLengthMilliSecondsBedtimeStory.value = durationStr!!.toLong()
                    uploadedFileBedtimeStory.value = tempFile
                    fileColorBedtimeStory.value = Peach
                }
            }
        }

    fun selectAudioPrayer(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/aac"
        selectAudioPrayerActivityResult.launch(intent)
    }

    private val selectAudioPrayerActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                if(data?.data != null){
                    val audioUri: Uri? = data.data
                    uploadedFileUriPrayer.value = audioUri!!
                    Log.i(TAG, "Audio Uri ==>> $audioUri")
                    val audioStream = audioUri.let {
                        contentResolver.openInputStream(it)
                    }
                    val tempFile = File.createTempFile("audio", ".aac")
                    copyStreamToFile(audioStream!!, tempFile)
                    val mdt = MediaMetadataRetriever()
                    mdt.setDataSource(tempFile.absolutePath)
                    val durationStr = mdt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    uploadedAudioFileLengthMilliSecondsPrayer.value = durationStr!!.toLong()
                    uploadedFilePrayer.value = tempFile
                    uploadedFileColorPrayer.value = Peach
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

    fun setCurrentUser(newValue: UserData) {
        _currentUser.postValue(newValue)
        Log.i(TAG, "currentUser changed : $_currentUser")
    }

    private fun observeCurrentUserChanged(){
        currentUser.observe(this) { currentUser ->
            Log.i(TAG, "currentUser changed : $currentUser")
            if (currentUser != null) {
                UserObject.setSignedInUser(UserObject.User.from(currentUser))
            }
        }
    }

    private fun setSignedInUser(){
        UserBackend.getUserWithUsername(Amplify.Auth.currentUser.username){
            if(it != null)
            setCurrentUser(it)
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

    override fun onTimerTick(durationString: String, durationMilliSeconds: Long) {
        recordingTimeDisplay.value = durationString
        addAmplitude(
            RecordedAudioData(
                recorder!!.maxAmplitude.toFloat(),
                durationMilliSeconds,
                recordingFile!!.length().toInt()
            )
        )
    }

    override fun justTick(durationString: String, durationMilliSeconds: Long) {
        recordingTimeDisplay.value = durationString
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserDashboardActivityUI(
    navController: NavHostController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
) {
    globalViewModel_!!.navController = navController
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    //if(globalViewModel_!!.currentUsersRoutines == null) {
        Log.i(UserDashboardActivity.TAG, "User ${globalViewModel_!!.currentUser}")
        globalViewModel_!!.currentUser?.let {
            UserRoutineBackend.queryUserRoutineBasedOnUser(it) { userRoutines ->
                globalViewModel_!!.currentUsersRoutines = userRoutines.toMutableList()
            }
        }
    //}
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            introTitle,
            options,
            favorite_routine_title,
            emptyRoutine,
            articles_title,
            articles,
            endSpace
        ) = createRefs()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ){
            ProfilePictureHeader(
                {
                    globalViewModel_!!.bottomSheetOpenFor = "recordAudio"
                    openBottomSheet(scope, state)},
                {
                    globalViewModel_!!.bottomSheetOpenFor = "controls"
                    openBottomSheet(scope, state)

                },
                {navController.navigate(Screen.Settings.screen_route)}
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(introTitle){
                    top.linkTo(header.bottom, margin = 20.dp)
                }
        ){
            NormalText(
                text = stringResource(id = R.string.explore),
                color = Grey,
                10,
                xOffset = 6,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier.constrainAs(options){
                top.linkTo(introTitle.bottom, margin = 8.dp)
            }
        ) {
            OptionsList(context, navController)
        }
        Column(
            modifier = Modifier
                .constrainAs(favorite_routine_title) {
                    top.linkTo(options.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Routines")
        }
        Column(
            modifier = Modifier
                .constrainAs(emptyRoutine) {
                    top.linkTo(favorite_routine_title.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            Log.i(UserDashboardActivity.TAG, "User Routines ==>> ${globalViewModel_!!.currentUsersRoutines}")
            if(globalViewModel_!!.currentUsersRoutines != null){
                if(globalViewModel_!!.currentUsersRoutines!!.size > 0)
                {
                    for(routine in globalViewModel_!!.currentUsersRoutines!!){
                        Routine(routine!!.routineData){
                            navigateToRoutineScreen(navController, routine.routineData)
                        }
                    }
                }else{
                    RoutineListWhenEmpty()
                }
            }else{
                RoutineListWhenEmpty()
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(articles_title) {
                    top.linkTo(emptyRoutine.bottom, margin = -10.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Did You Know")
        }
        Column(
            modifier = Modifier
                .constrainAs(articles) {
                    top.linkTo(articles_title.bottom, margin = 18.dp)
                }
                .padding(bottom = 24.dp)
        ){
            ArticlesList(navController)
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(articles.bottom, margin = 20.dp)
                }
        ){
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun OptionsList(context: Context, navController: NavHostController){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(displayName = "sleep", icon = R.drawable.sleep_icon, 71, 71, false, 0, 0, {}){ AuthBackend.signOut() }
        OptionItem(displayName = "music", icon = R.drawable.music_icon, 71, 71, false, 0, 0, {}){}
        OptionItem(displayName = "meditate", icon = R.drawable.meditate_icon, 71, 71, false, 0, 0, {}){something()}
        OptionItem(displayName = "sound", icon = R.drawable.sound_icon, 71, 71, false, 0, 0, {}){toSoundActivity(context, navController)}
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(displayName = "self-love", icon = R.drawable.self_love_icon, 71, 71, false, 0, 0, {}){something()}
        OptionItem(displayName = "stretch", icon = R.drawable.stretch_icon, 71, 71, false, 0, 0, {}){something()}
        OptionItem(displayName = "slumber\n  party", icon = R.drawable.slumber_party_icon, 71, 71, false, 0, 0, {}){something()}
        OptionItem(displayName = "bedtime\n  story", icon = R.drawable.bedtime_story_icon, 71, 71, false, 0, 0, {}){something()}
    }
}

@Composable
fun OptionItem(
    displayName: String,
    icon: Int,
    width: Int,
    height: Int,
    pro: Boolean,
    xOffset: Int,
    yOffset: Int,
    start: (displayName: String) -> Unit,
    lambda: (displayName: String) -> Unit
){
    start(displayName)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 15.dp)
            .clickable { lambda(displayName) }
    ){
        Box(
        ){
            Card(
                modifier = Modifier
                    .size(width.dp, height.dp)
                    //.fillMaxWidth(0.2F),
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                backgroundColor = White,
                elevation = 8.dp
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "$displayName icon",
                    modifier = Modifier
                        .size(width = 25.64.dp, height = 25.64.dp)
                        .padding(20.dp)
                )
            }
            if(pro) {
                Card(
                    modifier = Modifier
                        .size(45.dp, 22.dp)
                        .offset(xOffset.dp, yOffset.dp),
                    shape = MaterialTheme.shapes.small,
                    backgroundColor = Color.Black,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        MorgeNormalText(
                            text = "PRO",
                            color = Color.White,
                            fontSize = 12,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ){
            NormalText(
                text = displayName,
                color = MaterialTheme.colors.primary,
                fontSize = 9,
                xOffset = 0,
                yOffset = 0
            )
        }
    }
}

@Composable
private fun RoutineListWhenEmpty(){
    Column(
    ) {
        EmptyRoutine {
            something()
        }
        SurpriseMeRoutine {
            something()
        }
    }
}

@Composable
private fun ArticlesList(navController: NavController){
    Column(
    ) {
        Article(
            title = "the danger of sleeping pills",
            summary = "Sleeping pills are not meant to be taken daily.",
            icon = R.drawable.danger_of_sleeping_pills_icon
        ) {
            navController.navigate(Screen.Article.screen_route)
        }
        Article(
            title = "benefits of a goodnight sleep",
            summary = "Your skincare routine ends with a goodnight sleep.",
            icon = R.drawable.benefits_of_goodnight_sleep_icon
        ) {
            something()
        }
        Article(
            title = "how to be extra creative & productive?",
            summary = "Your day starts right after a goodnight sleep.",
            icon = R.drawable.extra_creative_and_productive_icon
        ) {
            something()
        }
    }
}

private fun toSoundActivity(context: Context, navController: NavHostController){
    navController.navigate(Screen.Sound.screen_route)
}

private fun something(){

}

fun navigateToRoutineScreen(navController: NavController, routineData: RoutineData){
    navController.navigate("${Screen.RoutineScreen.screen_route}/routine=${RoutineObject.Routine.from(routineData)}")
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
fun UserDashboardActivityPreview() {
    EUNOIATheme {
        //UserDashboardActivityUI()
    }
}