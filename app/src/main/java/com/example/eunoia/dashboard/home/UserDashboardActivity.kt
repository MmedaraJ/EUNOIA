package com.example.eunoia.dashboard.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amazonaws.util.DateUtils
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.createPrayer.*
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.create.createSound.*
import com.example.eunoia.create.createSound.selectedIndex
import com.example.eunoia.create.resetEverything
import com.example.eunoia.create.resetEverythingExceptRoutine
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.sign_in_process.SignInActivity
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.bottomSheets.recordAudio.recorder
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingTimeDisplay
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.MultiBottomNavApp
import com.example.eunoia.ui.navigation.generalMediaPlayerService_
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.*
import com.example.eunoia.utils.Timer
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*

var routineActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()

var routineActivitySoundUrisMapList = mutableListOf<MutableMap<String, MutableList<Uri?>>>()
var routineActivitySoundUriVolumes = mutableListOf<MutableMap<String, MutableList<Int>>>()

var routineActivityPrayerUrisMapList = mutableListOf<MutableMap<String, Uri>>()
var routineActivityBedtimeStoryUrisMapList = mutableListOf<MutableMap<String, Uri>>()
var routineActivitySelfLoveUrisMapList = mutableListOf<MutableMap<String, Uri>>()

const val START_ROUTINE = "start"
const val PAUSE_ROUTINE = "pause"
const val WAIT_FOR_ROUTINE = "wait"

private const val TAG = "UserDashboardActivity"

class UserDashboardActivity :
    ComponentActivity(),
    Timer.OnTimerTickListener,
    BedtimeStoryTimer.OnBedtimeStoryTimerTickListener,
    SelfLoveTimer.OnSelfLoveTimerTickListener,
    PrayerTimer.OnPrayerTimerTickListener,
    GeneralPlaytimeTimer.OnGeneralPlaytimeTimerTickListener,
    SoundPlaytimeTimer.OnSoundPlaytimeTimerTickListener,
    RoutinePlaytimeTimer.OnRoutinePlaytimeTimerTickListener
{
    private val _currentUser = MutableLiveData<UserData>(null)
    var currentUser: LiveData<UserData> = _currentUser
   // private const var TAG = "UserDashboardActivity"

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
        selectAudioBedtimeStoryActivityResult.launch(intent)
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

    fun selectAudioSelfLove(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/aac"
        selectAudioSelfLoveActivityResult.launch(intent)
    }

    private val selectAudioSelfLoveActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data: Intent? = result.data
                if(data?.data != null){
                    val audioUri: Uri? = data.data
                    uploadedFileUriSelfLove.value = audioUri!!
                    Log.i(TAG, "Audio Uri ==>> $audioUri")
                    val audioStream = audioUri.let {
                        contentResolver.openInputStream(it)
                    }
                    val tempFile = File.createTempFile("audio", ".aac")
                    copyStreamToFile(audioStream!!, tempFile)
                    val mdt = MediaMetadataRetriever()
                    mdt.setDataSource(tempFile.absolutePath)
                    val durationStr = mdt.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    uploadedAudioFileLengthMilliSecondsSelfLove.value = durationStr!!.toLong()
                    uploadedFileSelfLove.value = tempFile
                    uploadedFileColorSelfLove.value = Peach
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

    override fun onBedtimeStoryTimerTick(durationString: String, durationMilliSeconds: Long) {
        if(
            generalMediaPlayerService_!!.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying != null
        ){
            globalViewModel_!!.bedtimeStoryTimeDisplay = durationString
            globalViewModel_!!.bedtimeStoryCircularSliderClicked = false
            globalViewModel_!!.bedtimeStoryCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            (globalViewModel_!!.currentBedtimeStoryPlaying!!.fullPlayTime).toFloat()
                    ) * 360f
        }
        //TODO if current position of media player == duration of media player, stop media player,
        // set controls to represent pause mode
    }

    override fun onSelfLoveTimerTick(durationString: String, durationMilliSeconds: Long) {
        if (
            generalMediaPlayerService_!!.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentSelfLovePlaying != null
        ) {
            globalViewModel_!!.selfLoveTimeDisplay = durationString
            globalViewModel_!!.selfLoveCircularSliderClicked = false
            globalViewModel_!!.selfLoveCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            (globalViewModel_!!.currentSelfLovePlaying!!.fullPlayTime).toFloat()
                    ) * 360f
        }
    }

    override fun onPrayerTimerTick(durationString: String, durationMilliSeconds: Long) {
        if (
            generalMediaPlayerService_!!.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentPrayerPlaying != null
        ) {
            globalViewModel_!!.prayerTimeDisplay = durationString
            globalViewModel_!!.prayerCircularSliderClicked = false
            globalViewModel_!!.prayerCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            (globalViewModel_!!.currentPrayerPlaying!!.fullPlayTime).toFloat()
                    ) * 360f
        }
    }

    override fun onGeneralPlaytimeTimerTick(durationString: String, durationMilliSeconds: Long) {
        //Log.i(TAG, "duration: $durationString")
    }

    override fun onRoutinePlaytimeTimerTick(durationString: String, durationMilliSeconds: Long) {

    }

    override fun onSoundPlaytimeTimerTick(durationString: String, durationMilliSeconds: Long) {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserDashboardActivityUI(
    navController: NavHostController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService
) {
    globalViewModel_!!.navController = navController
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var retrievedUserRoutineRelationships by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserRoutineRelationshipBackend.queryUserRoutineRelationshipBasedOnUser(it) { userRoutineRelationships ->
            if(routineActivitySoundUrisMapList.size < userRoutineRelationships.size) {
                for (i in userRoutineRelationships.indices) {
                    routineActivityPlayButtonTexts.add(mutableStateOf(START_ROUTINE))
                    routineActivitySoundUriVolumes.add(mutableMapOf())
                    routineActivitySoundUrisMapList.add(mutableMapOf())
                    routineActivityPrayerUrisMapList.add(mutableMapOf())
                    routineActivityBedtimeStoryUrisMapList.add(mutableMapOf())
                    routineActivitySelfLoveUrisMapList.add(mutableMapOf())
                }
            }

            globalViewModel_!!.currentUsersRoutineRelationships = userRoutineRelationships.toMutableList()
            retrievedUserRoutineRelationships = true
        }
    }

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

                },
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
            modifier = Modifier
                .constrainAs(options){
                    top.linkTo(introTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            OptionItem(
                allElements,
                allIcons,
                allPros
            ) {
                when (it) {
                    "sleep" -> {
                        AuthBackend.signOut()
                    }
                    "sound" -> {
                        toSoundActivity(navController)
                    }
                    "prayer" -> {
                        toPrayerActivity(navController)
                    }
                    "bedtime\nstory" -> {
                        toBedtimeStoryActivity(navController)
                    }
                    "self-love" -> {
                        toSelfLoveActivity(navController)
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(favorite_routine_title) {
                    top.linkTo(options.bottom, margin = 16.dp)
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
            if(
                globalViewModel_!!.currentUsersRoutineRelationships != null &&
                retrievedUserRoutineRelationships
            ){
                if(globalViewModel_!!.currentUsersRoutineRelationships!!.size > 0) {
                    for(i in globalViewModel_!!.currentUsersRoutineRelationships!!.indices){
                        setRoutineActivityPlayButtonTextsCorrectly(i)
                        UserRoutineRelationshipCard(
                            globalViewModel_!!.currentUsersRoutineRelationships!![i]!!,
                            i,
                            { index ->
                                resetRoutineMediaPlayerServicesIfNecessary(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    index,
                                    context
                                ){
                                    resetRoutinePlayButtonTextsIfNecessary(index)
                                    selectNextRoutineElement(
                                        soundMediaPlayerService,
                                        generalMediaPlayerService,
                                        index,
                                        context
                                    )
                                }
                            },
                            {
                                navigateToRoutineScreen(navController, globalViewModel_!!.currentUsersRoutineRelationships!![i]!!)
                            }
                        )
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

private fun setRoutineActivityPlayButtonTextsCorrectly(i: Int) {
    if (globalViewModel_!!.currentUserRoutineRelationshipPlaying != null) {
        if (
            globalViewModel_!!.currentUserRoutineRelationshipPlaying!!.id ==
            globalViewModel_!!.currentUsersRoutineRelationships!![i]!!.id
        ) {
            if(globalViewModel_!!.isCurrentRoutinePlaying){
                routineActivityPlayButtonTexts[i]!!.value = PAUSE_ROUTINE
            }else{
                routineActivityPlayButtonTexts[i]!!.value = START_ROUTINE
            }
        } else {
            routineActivityPlayButtonTexts[i]!!.value = START_ROUTINE
        }
    } else {
        routineActivityPlayButtonTexts[i]!!.value = START_ROUTINE
    }
}

private fun resetRoutineMediaPlayerServicesIfNecessary(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context,
    completed: () -> Unit
) {
    //reset other elements
    Log.i(TAG, "About to reset ecerything except routine")
    resetEverythingExceptRoutine(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    ){
        if(globalViewModel_!!.currentUserRoutineRelationshipPlaying != null) {
            if (globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.id != globalViewModel_!!.currentUserRoutineRelationshipPlaying!!.id) {
                globalViewModel_!!.currentRoutinePlayingOrderIndex = 0
                globalViewModel_!!.currentRoutinePlayingOrder = null
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = 0
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets = null
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.currentPrayerPlayingIndex
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers = null
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.currentBedtimeStoryPlayingIndex
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = null
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.currentSelfLovePlayingIndex
                globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = null
                //countdown timers
                resetGlobalRoutineCountdownTimers()

                soundMediaPlayerService.onDestroy()
                generalMediaPlayerService.onDestroy()
            }
        }
        completed()
    }
}

fun resetGlobalRoutineCountdownTimers() {
    if(globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = null
    }

    if(globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = null
    }

    if(globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingNextPrayerCountDownTimer = null
    }

    if(globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingSelfLoveCountDownTimer = null
    }

    if(globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingNextSelfLoveCountDownTimer = null
    }

    if(globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingBedtimeStoryCountDownTimer = null
    }

    if(globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer != null) {
        globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.cancel()
        globalViewModel_!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null
    }
}

private fun resetRoutinePlayButtonTextsIfNecessary(index: Int) {
    for(j in routineActivityPlayButtonTexts.indices){
        if(j != index){
            if(routineActivityPlayButtonTexts[j]!!.value != START_ROUTINE) {
                routineActivityPlayButtonTexts[j]!!.value = START_ROUTINE
            }
        }
    }
}

private fun selectNextRoutineElement(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    /*observeGeneralMediaPlayerIsCompleted(
        soundMediaPlayerService,
        generalMediaPlayerService,
        index,
        context
    )*/

    updateRoutineOncePlayIsClicked(index) {
        globalViewModel_!!.currentRoutinePlayingOrder =
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.playingOrder
        globalViewModel_!!.currentUserRoutineRelationshipPlaying =
            globalViewModel_!!.currentUsersRoutineRelationships!![index]!!
        Log.i(
            TAG,
            "Our routine playing orderz is ${globalViewModel_!!.currentRoutinePlayingOrder!![globalViewModel_!!.currentRoutinePlayingOrderIndex!!]}"
        )
        when (globalViewModel_!!.currentRoutinePlayingOrder!![globalViewModel_!!.currentRoutinePlayingOrderIndex!!]) {
            "sound" -> {
                incrementPlayingOrderIndex(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
            "sleep" -> {
                incrementPlayingOrderIndex(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
            "prayer" -> {
                PrayerForRoutine.playOrPausePrayerAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
            "bedtimeStory" -> {
                BedtimeStoryForRoutine.playOrPauseBedtimeStoryAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
            "self-love" -> {
                SelfLoveForRoutine.playOrPauseSelfLoveAccordingly(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
            else -> {
                incrementPlayingOrderIndex(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }
    }
}

fun updateRoutineOncePlayIsClicked(index: Int, completed: () -> Unit) {
    if (globalViewModel_!!.currentRoutinePlayingOrderIndex == 0) {
        updatePreviousUserRoutineRelationship {
            updateRecentlyPlayedUserRoutineRelationshipWithUserRoutineRelationship(
                globalViewModel_!!.currentUsersRoutineRelationships!![index]!!
            ) {
                globalViewModel_!!.currentRoutinePlaying = it.userRoutineRelationshipRoutine
                globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
                globalViewModel_!!.routinePlaytimeTimer.start()
                completed()
            }
        }
    }else{
        completed()
    }
}

fun updatePreviousUserRoutineRelationship(
    completed: (updatedUserRoutineRelationship: UserRoutineRelationship?) -> Unit
) {
    if(globalViewModel_!!.previouslyPlayedUserRoutineRelationship != null){
        val playTime = globalViewModel_!!.routinePlaytimeTimer.getDuration()
        globalViewModel_!!.routinePlaytimeTimer.stop()

        val totalPlayTime = globalViewModel_!!.previouslyPlayedUserRoutineRelationship!!.totalPlayTime + playTime

        var usagePlayTimes = globalViewModel_!!.previouslyPlayedUserRoutineRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = globalViewModel_!!.previouslyPlayedUserRoutineRelationship!!.numberOfTimesPlayed

        if(totalPlayTime > 0){
            val userRoutineRelationship = globalViewModel_!!.previouslyPlayedUserRoutineRelationship!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(userRoutineRelationship){
                globalViewModel_!!.previouslyPlayedUserRoutineRelationship = null
                completed(it)
            }
        }else{
            completed(null)
        }
    }else{
        completed(null)
    }
}

/**
 * Keep track of the date time a user starts listening to a routine
 */
fun updateCurrentUserRoutineRelationshipUsageTimeStamp(
    userRoutineRelationship: UserRoutineRelationship,
    completed: (updatedUserRoutineRelationship: UserRoutineRelationship) -> Unit
) {
    var usageTimeStamp = userRoutineRelationship.usageTimestamps
    val currentDateTime = DateUtils.formatISO8601Date(Date())

    if(usageTimeStamp != null) {
        usageTimeStamp.add(Temporal.DateTime(currentDateTime))
    }else{
        usageTimeStamp = listOf(Temporal.DateTime(currentDateTime))
    }

    val numberOfTimesPlayed = userRoutineRelationship.numberOfTimesPlayed + 1

    val newUserRoutineRelationship = userRoutineRelationship.copyOfBuilder()
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .usageTimestamps(usageTimeStamp)
        .build()

    UserRoutineRelationshipBackend.updateUserRoutineRelationship(newUserRoutineRelationship){
        completed(it)
    }
}

fun updateRecentlyPlayedUserRoutineRelationshipWithUserRoutineRelationship(
    userRoutineRelationship: UserRoutineRelationship,
    completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit
){
    updateCurrentUserRoutineRelationshipUsageTimeStamp(userRoutineRelationship) {
        globalViewModel_!!.previouslyPlayedUserRoutineRelationship = it
        completed(it)
    }
}

fun updateRecentlyPlayedUserRoutineRelationshipWithRoutine(
    routineData: RoutineData,
    completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit
){
    globalViewModel_!!.currentRoutinePlaying = routineData
    UserRoutineRelationshipBackend.queryUserRoutineRelationshipBasedOnUserAndRoutine(
        globalViewModel_!!.currentUser!!,
        routineData
    ) { userRoutineRelationship ->
        if(userRoutineRelationship.isNotEmpty()) {
            updateCurrentUserRoutineRelationshipUsageTimeStamp(userRoutineRelationship[0]!!) {
                globalViewModel_!!.previouslyPlayedUserRoutineRelationship = it
                completed(it)
            }
        }else{
            UserRoutineRelationshipBackend.createUserRoutineRelationshipObject(
                routineData
            ){ newUserRoutineRelationship ->
                updateCurrentUserRoutineRelationshipUsageTimeStamp(newUserRoutineRelationship) {
                    globalViewModel_!!.previouslyPlayedUserRoutineRelationship = it
                    completed(it)
                }
            }
        }
    }
}

private fun observeGeneralMediaPlayerIsCompleted(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
){
    globalViewModel_!!.generalMediaPlayerIsCompleted.observe(UserDashboardActivity.getInstanceActivity()) { generalMediaPlayerIsCompleted ->
        Log.i(TAG, "isCompleted changed : ${globalViewModel_!!.generalMediaPlayerIsCompleted.value}")
        if (generalMediaPlayerIsCompleted) {
            when(globalViewModel_!!.currentRoutinePlayingOrder!![globalViewModel_!!.currentRoutinePlayingOrderIndex!!]){
                "prayer" -> {
                    Log.i(TAG, "Prayar is completed in routine")
                    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!! + 1
                    if(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex!! > globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers!!.indices.last){
                        globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = 0
                    }

                    val routine = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                        .currentPrayerPlayingIndex(globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex)
                        .build()

                    UserRoutineRelationshipBackend.updateUserRoutineRelationship(routine){}

                    selectNextRoutineElement(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        index,
                        context
                    )
                }
                else -> {
                    Log.i(TAG, "SOmething besides prayer")
                }
            }
        }
    }
}

fun incrementPlayingOrderIndex(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
){
    Log.i(TAG, "plahing order index before increment is ${globalViewModel_!!.currentRoutinePlayingOrderIndex}")
    globalViewModel_!!.currentRoutinePlayingOrderIndex = globalViewModel_!!.currentRoutinePlayingOrderIndex!! + 1
    if(globalViewModel_!!.currentRoutinePlayingOrderIndex!! > globalViewModel_!!.currentRoutinePlayingOrder!!.indices.last){
        updateUserRoutineRelationshipWhenRoutineIsDonePlaying(index){
            Log.i(TAG, "Routine is endedz")
            globalViewModel_!!.currentUsersRoutineRelationships!![index] = it
            globalViewModel_!!.currentRoutinePlayingOrderIndex = 0
            routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
        }
    }else{
        selectNextRoutineElement(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }
}

fun updateUserRoutineRelationshipWhenRoutineIsDonePlaying(
    index: Int,
    completed: (updatedUserRoutineRelationship: UserRoutineRelationship?) -> Unit
) {
    if(globalViewModel_!!.currentUsersRoutineRelationships!![index] != null){
        val playTime = globalViewModel_!!.routinePlaytimeTimer.getDuration()
        globalViewModel_!!.routinePlaytimeTimer.stop()

        val totalPlayTime = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.totalPlayTime + playTime

        var usagePlayTimes = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.numberOfTimesPlayed

        if(totalPlayTime > 0){
            val userRoutineRelationship = globalViewModel_!!.currentUsersRoutineRelationships!![index]!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(userRoutineRelationship){
                globalViewModel_!!.previouslyPlayedUserRoutineRelationship = null
                completed(it)
            }
        }
    }
}

fun resetRoutineGlobalProperties(){
    resetGlobalRoutineCountdownTimers()
    globalViewModel_!!.currentRoutinePlaying = null
    globalViewModel_!!.currentUserRoutineRelationshipPlaying = null
    globalViewModel_!!.isCurrentRoutinePlaying = false

    globalViewModel_!!.currentRoutinePlayingOrderIndex = 0
    globalViewModel_!!.currentRoutinePlayingOrder = null
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = 0
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPresets = null
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = 0
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipPrayers = null
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = 0
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = null
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = 0
    globalViewModel_!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = null
}

private val allElements = listOf(
    "sleep",
    "prayer",
    "meditate",
    "sound",
    "self-love",
    "stretch",
    "slumber\nparty",
    "bedtime\nstory",
)

private val allIcons = listOf(
    R.drawable.sleep_icon,
    R.drawable.music_icon,
    R.drawable.meditate_icon,
    R.drawable.sound_icon,
    R.drawable.self_love_icon,
    R.drawable.stretch_icon,
    R.drawable.slumber_party_icon,
    R.drawable.bedtime_story_icon,
)

private val allPros = listOf(
    false,
    true,
    false,
    false,
    true,
    false,
    false,
    true,
)

@Composable
fun OptionItem(
    allElements: List<String>,
    allIcons: List<Int>,
    allPros: List<Boolean>,
    elementClicked: (element: String) -> Unit,
){
    val borders = mutableListOf<MutableState<Boolean>>()
    for(i in allElements.indices){
        borders.add(remember { mutableStateOf(false) })
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ){
        val (
            elements,
        ) = createRefs()
        SimpleFlowRow(
            verticalGap = 16.dp,
            horizontalGap = 12.dp,
            alignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(elements) {
                    top.linkTo(parent.top, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            allElements.forEachIndexed { index, element ->
                var cardModifier = Modifier
                    .clickable {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value
                        elementClicked(element)
                    }
                    .fillMaxWidth(0.22F)

                if (borders[index].value) {
                    cardModifier = cardModifier.then(
                        Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    //modifier = cardModifier
                ) {
                    Card(
                        modifier = cardModifier,
                        shape = MaterialTheme.shapes.small,
                        elevation = 8.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.TopEnd
                        ) {
                            if(allPros[index]) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .zIndex(2f)
                                        .graphicsLayer {
                                            //translationX = 120f
                                        }
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .fillMaxHeight(0.2f)
                                            .offset(),
                                        shape = RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = 10.dp,
                                            bottomEnd = 0.dp,
                                        ),
                                        backgroundColor = Color.Black,
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
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

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(White)
                                    .aspectRatio(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = allIcons[index]),
                                    contentDescription = "$element icon",
                                    modifier = Modifier
                                        .fillMaxSize(0.33F)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                    ){
                        AlignedNormalText(
                            text = element,
                            color = MaterialTheme.colors.primary,
                            fontSize = 9,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
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

private fun toSoundActivity(navController: NavHostController){
    navController.navigate(Screen.Sound.screen_route)
}

private fun toBedtimeStoryActivity(navController: NavHostController){
    navController.navigate(Screen.BedtimeStory.screen_route)
}

private fun toSelfLoveActivity(navController: NavHostController){
    navController.navigate(Screen.SelfLove.screen_route)
}

private fun toPrayerActivity(navController: NavHostController){
    navController.navigate(Screen.Prayer.screen_route)
}

private fun something(){

}

fun navigateToRoutineScreen(navController: NavController, userRoutineRelationship: UserRoutineRelationship){
    navController.navigate("${Screen.RoutineScreen.screen_route}/userRoutineRelationship=${UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship)}")
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