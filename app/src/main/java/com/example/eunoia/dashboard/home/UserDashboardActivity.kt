package com.example.eunoia.dashboard.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.core.net.toUri
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.R
import com.example.eunoia.backend.*
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.createPrayer.*
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.create.createSound.*
import com.example.eunoia.create.createSound.selectedIndex
import com.example.eunoia.dashboard.prayer.resetOtherGeneralMediaPlayerUsersExceptPrayer
import com.example.eunoia.dashboard.sound.*
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.sign_in_process.SignInActivity
import com.example.eunoia.sign_in_process.SignUpConfirmationCodeActivity
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.bottomSheets.prayer.activatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.prayer.deActivatePrayerGlobalControlButton
import com.example.eunoia.ui.bottomSheets.recordAudio.recorder
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingTimeDisplay
import com.example.eunoia.ui.bottomSheets.sound.resetGlobalControlButtons
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.MultiBottomNavApp
import com.example.eunoia.ui.navigation.generalMediaPlayerService_
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.ref.WeakReference

var routineActivityPlayButtonTexts = mutableListOf<MutableState<String>?>()

var routineActivitySoundUrisMapList = mutableListOf<MutableMap<String, MutableList<Uri?>>>()
var routineActivitySoundUriVolumes = mutableListOf<MutableMap<String, MutableList<Int>>>()

var routineActivityPrayerUrisMapList = mutableListOf<MutableMap<String, Uri>>()
var routineActivityBedtimeStoryUris = mutableListOf<MutableState<Uri>?>()
var routineActivitySelfLoveUris = mutableListOf<MutableState<Uri>?>()

var playingSoundNow: SoundData? = null

private const val START_ROUTINE = "start"
private const val PAUSE_ROUTINE = "pause"
private const val WAIT_FOR_ROUTINE = "wait"

private const val TAG = "UserDashboardActivity"

class UserDashboardActivity :
    ComponentActivity(),
    Timer.OnTimerTickListener,
    BedtimeStoryTimer.OnBedtimeStoryTimerTickListener,
    SelfLoveTimer.OnSelfLoveTimerTickListener,
    PrayerTimer.OnPrayerTimerTickListener,
    GeneralPlaytimeTimer.OnGeneralPlaytimeTimerTickListener
{
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
        if(generalMediaPlayerService_!!.isMediaPlayerInitialized()){
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
        if (generalMediaPlayerService_!!.isMediaPlayerInitialized()) {
            globalViewModel_!!.selfLoveTimeDisplay = durationString
            globalViewModel_!!.selfLoveCircularSliderClicked = false
            globalViewModel_!!.selfLoveCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            (globalViewModel_!!.currentSelfLovePlaying!!.fullPlayTime).toFloat()
                    ) * 360f
        }
    }

    override fun onPrayerTimerTick(durationString: String, durationMilliSeconds: Long) {
        if (generalMediaPlayerService_!!.isMediaPlayerInitialized()) {
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

    var retrievedRoutines by rememberSaveable{ mutableStateOf(false) }
    globalViewModel_!!.currentUser?.let {
        UserRoutineBackend.queryUserRoutineBasedOnUser(it) { userRoutines ->
            if(soundActivityUris.size < userRoutines.size) {
                for (i in userRoutines.indices) {
                    routineActivityPlayButtonTexts.add(mutableStateOf(START_ROUTINE))
                    routineActivitySoundUriVolumes.add(mutableMapOf())
                    routineActivitySoundUrisMapList.add(mutableMapOf())
                    routineActivityPrayerUrisMapList.add(mutableMapOf())
                    routineActivityBedtimeStoryUris.add(mutableStateOf("".toUri()))
                    routineActivitySelfLoveUris.add(mutableStateOf("".toUri()))
                }
            }

            globalViewModel_!!.currentUsersRoutines = userRoutines.toMutableList()
            retrievedRoutines = true
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
                globalViewModel_!!.currentUsersRoutines != null &&
                retrievedRoutines
            ){
                if(globalViewModel_!!.currentUsersRoutines!!.size > 0) {
                    for(i in globalViewModel_!!.currentUsersRoutines!!.indices){
                        setRoutineActivityPlayButtonTextsCorrectly(i)
                        RoutineCard(
                            globalViewModel_!!.currentUsersRoutines!![i]!!.routineData,
                            i,
                            { index ->
                                resetRoutineMediaPlayerServicesIfNecessary(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    index
                                )
                                resetRoutinePlayButtonTextsIfNecessary(index)
                                selectNextRoutineElement(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    index,
                                    context
                                )
                            },
                            {
                                navigateToRoutineScreen(navController, globalViewModel_!!.currentUsersRoutines!![i]!!.routineData)
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
    if (globalViewModel_!!.currentRoutinePlaying != null) {
        if (
            globalViewModel_!!.currentRoutinePlaying!!.id ==
            globalViewModel_!!.currentUsersRoutines!![i]!!.routineData.id
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
    index: Int
) {
    if(globalViewModel_!!.currentRoutinePlaying != null) {
        if (globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.id != globalViewModel_!!.currentRoutinePlaying!!.id) {
            globalViewModel_!!.currentRoutinePlayingOrderIndex = 0
            globalViewModel_!!.currentRoutinePlayingOrder = null
            globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex = 0
            globalViewModel_!!.currentRoutinePlayingRoutinePresets = null
            globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentPrayerPlayingIndex
            globalViewModel_!!.currentRoutinePlayingRoutinePrayers = null
            soundMediaPlayerService.onDestroy()
            generalMediaPlayerService.onDestroy()
        }
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
    observeGeneralMediaPlayerIsCompleted(
        soundMediaPlayerService,
        generalMediaPlayerService,
        index,
        context
    )

    globalViewModel_!!.currentRoutinePlayingOrder = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playingOrder
    globalViewModel_!!.currentRoutinePlaying = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
    Log.i(TAG, "Our routine playing orderz is ${globalViewModel_!!.currentRoutinePlayingOrder!![globalViewModel_!!.currentRoutinePlayingOrderIndex!!]}")
    when(globalViewModel_!!.currentRoutinePlayingOrder!![globalViewModel_!!.currentRoutinePlayingOrderIndex!!]){
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
            playOrPausePrayerAccordingly(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
        "bedtimeStory" -> {
            playOrPauseBedtimeStorAccordingly(
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
                    globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex = globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!! + 1
                    if(globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!! > globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!.indices.last){
                        globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex = 0
                    }

                    val routine = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.copyOfBuilder()
                        .currentPrayerPlayingIndex(globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex)
                        .build()

                    RoutineBackend.updateRoutine(routine){}

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
    globalViewModel_!!.currentRoutinePlayingOrderIndex = globalViewModel_!!.currentRoutinePlayingOrderIndex!! + 1
    if(globalViewModel_!!.currentRoutinePlayingOrderIndex!! > globalViewModel_!!.currentRoutinePlayingOrder!!.indices.last){
        //end routine
        Log.i(TAG, "Routine is endedz")
        globalViewModel_!!.currentRoutinePlayingOrderIndex = 0
        routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
    }else{
        selectNextRoutineElement(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }
}

fun playOrPausePrayerAccordingly(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    if(routineActivityPlayButtonTexts[index]!!.value == START_ROUTINE) {
        routineActivityPlayButtonTexts[index]!!.value = WAIT_FOR_ROUTINE

        if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playingOrder.contains("sound")){
            if(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.playSoundDuringPrayer){
                playSound(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }else{
                pauseSound(
                    soundMediaPlayerService,
                    index
                )
            }
        }

        playPrayer(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }else if(
        routineActivityPlayButtonTexts[index]!!.value == PAUSE_ROUTINE ||
        routineActivityPlayButtonTexts[index]!!.value == WAIT_FOR_ROUTINE
    ){
        Log.i(TAG, "Pausing prayer and sound?")
        //pause prayer and sound
        pausePrayer(
            generalMediaPlayerService,
            index,
        )

        pauseSound(
            soundMediaPlayerService,
            index
        )
        routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
    }
}

private fun pausePrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int
) {
    if(
        generalMediaPlayerService.isMediaPlayerInitialized() &&
        globalViewModel_!!.currentBedtimeStoryPlaying == null &&
        globalViewModel_!!.currentSelfLovePlaying == null
    ) {
        if(generalMediaPlayerService.isMediaPlayerPlaying()) {
            generalMediaPlayerService.pauseMediaPlayer()
            globalViewModel_!!.prayerTimer.pause()
            globalViewModel_!!.isCurrentPrayerPlaying = false
            activatePrayerGlobalControlButton(2)
        }
    }
}

fun playPrayer(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    if(globalViewModel_!!.currentRoutinePlayingRoutinePrayers != null) {
        if(
            routineActivityPrayerUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                        .prayerData.id
            ] != "".toUri()
        ) {
            if(globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!.isEmpty()) {
                getRoutinePrayers(index) {
                    retrievePrayerUris(
                        generalMediaPlayerService,
                        soundMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                retrievePrayerUris(
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            startPrayer(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }else{
        getRoutinePrayers(index) {
            retrievePrayerUris(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }
    }
}

private fun getRoutinePrayers(index: Int, completed: () -> Unit){
    getRoutinePrayersBasedOnRoutine(
        globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
    ) { routinePrayers ->
        for( routinePrayer in routinePrayers){
            routineActivityPrayerUrisMapList[index][routinePrayer!!.prayerData.id] = "".toUri()
        }
        globalViewModel_!!.currentRoutinePlayingRoutinePrayers = routinePrayers
        completed()
    }
}

fun getRoutinePrayersBasedOnRoutine(
    routineData: RoutineData,
    completed: (routinePrayerList: MutableList<RoutinePrayer?>) -> Unit
) {
    RoutinePrayerBackend.queryRoutinePrayerBasedOnRoutine(routineData) { routinePrayers ->
        completed(routinePrayers.toMutableList())
    }
}

private fun retrievePrayerUris(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    SoundBackend.retrieveAudio(
        globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
            .prayerData.audioKeyS3,
        "us-east-1:40c47e83-afe6-426d-8ebc-250f40992dc8"
        /*globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
            .prayerData.prayerOwner.amplifyAuthUserId*/
    ) {
        routineActivityPrayerUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                    .prayerData.id
        ] = it

        startPrayer(
            generalMediaPlayerService,
            soundMediaPlayerService,
            index,
            context
        )
    }
}

fun startPrayer(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
) {
    if(
        routineActivityPrayerUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                    .prayerData.id
        ] != "".toUri()
    ) {
        if(
            generalMediaPlayerService.isMediaPlayerInitialized() &&
            globalViewModel_!!.currentBedtimeStoryPlaying == null &&
            globalViewModel_!!.currentSelfLovePlaying == null
        ){
            generalMediaPlayerService.startMediaPlayer()
        }else{
            initializePrayerMediaPlayers(
                generalMediaPlayerService,
                soundMediaPlayerService,
                index,
                context
            )
        }

        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        Log.i(TAG, "Itzz was --pause-- right here")
        generalMediaPlayerService.loopMediaPlayer()
        //globalViewModel_!!.previouslyPlayedUserSoundRelationship = globalViewModel_!!.currentUsersSoundRelationships!![index]
        //globalViewModel_!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingPrayer(index)
    }
}

private fun initializePrayerMediaPlayers(
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int,
    context: Context
){
    generalMediaPlayerService.onDestroy()
    generalMediaPlayerService.setAudioUri(
        routineActivityPrayerUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                    .prayerData.id
        ]!!
    )
    val intent = Intent()
    intent.action = "PLAY"
    generalMediaPlayerService.onStartCommand(intent, 0, 0)
    generalMediaPlayerService.getMediaPlayer()!!.seekTo(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentPrayerContinuePlayingTime)
    globalViewModel_!!.prayerTimer.setMaxDuration(
        globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
            .prayerData.fullPlayTime.toLong()
    )
    globalViewModel_!!.prayerTimer.setDuration(globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.currentPrayerContinuePlayingTime.toLong())
    resetOtherGeneralMediaPlayerUsersExceptPrayer()

    startPrayerCDT(
        soundMediaPlayerService,
        generalMediaPlayerService,
        index,
        context
    )
}

fun startPrayerCDT(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    startPrayerCountDownTimer(
        context,
        globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.prayerPlayTime.toLong(),
        generalMediaPlayerService
    ){
        var continuePlayingTime = -1
        if(generalMediaPlayerService.isMediaPlayerInitialized()) {
            continuePlayingTime = generalMediaPlayerService.getMediaPlayer()!!.currentPosition
            generalMediaPlayerService.onDestroy()
        }
        deActivatePrayerGlobalControlButton(0)
        activatePrayerGlobalControlButton(2)
        globalViewModel_!!.isCurrentPrayerPlaying = false
        globalViewModel_!!.currentPrayerPlaying = null
        globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = null

        val routine = globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.copyOfBuilder()
            .currentPrayerContinuePlayingTime(continuePlayingTime)
            .build()

        //update routine with new prayer info
        RoutineBackend.updateRoutine(routine){

        }

        incrementPlayingOrderIndex(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }
}

private fun startPrayerCountDownTimer(
    context: Context,
    time: Long,
    generalMediaPlayerService: GeneralMediaPlayerService,
    completed: () -> Unit
){
    if(globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer == null){
        globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer = object : CountDownTimer(time, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "Prayer routine timer: $millisUntilFinished")
            }
            override fun onFinish() {
                completed()
                Log.i(TAG, "Timer stopped")
            }
        }
    }
    globalViewModel_!!.currentRoutinePlayingPrayerCountDownTimer!!.start()
}

private fun setGlobalPropertiesAfterPlayingPrayer(index: Int){
    globalViewModel_!!.currentPrayerPlaying =
        globalViewModel_!!.currentRoutinePlayingRoutinePrayers!![
                globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!
        ]!!.prayerData

    globalViewModel_!!.currentPrayerPlayingUri =
        routineActivityPrayerUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePrayers!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePrayersIndex!!]!!
                    .prayerData.id
        ]

    globalViewModel_!!.isCurrentPrayerPlaying = true
    globalViewModel_!!.isCurrentRoutinePlaying = true
    deActivatePrayerGlobalControlButton(0)
    deActivatePrayerGlobalControlButton(2)
}

fun playSound(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    if(globalViewModel_!!.currentRoutinePlayingRoutinePresets != null) {
        if(
            routineActivitySoundUrisMapList[index][
                    globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                            [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                        .soundPresetData.id
            ]!!.isEmpty()
        ) {
            if(globalViewModel_!!.currentRoutinePlayingRoutinePresets!!.isEmpty()) {
                getRoutinePresets(index) {
                    retrieveSoundUris(
                        soundMediaPlayerService,
                        generalMediaPlayerService,
                        index,
                        context
                    )
                }
            }else{
                retrieveSoundUris(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    index,
                    context
                )
            }
        }else{
            startSound(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }else{
        getRoutinePresets(index) {
            retrieveSoundUris(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }
    }
}

private fun pauseSound(
    soundMediaPlayerService: SoundMediaPlayerService,
    index: Int
) {
    if(soundMediaPlayerService.areMediaPlayersInitialized()) {
        if(soundMediaPlayerService.areMediaPlayersPlaying()) {
            globalViewModel_!!.generalPlaytimeTimer.pause()
            soundMediaPlayerService.pauseMediaPlayers()
            com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(3)
            globalViewModel_!!.isCurrentSoundPlaying = false
            Log.i(TAG, "Pausedz sound")
        }
    }
}

private fun getRoutinePresets(index: Int, completed: () -> Unit){
    getRoutinePresetsBasedOnRoutine(
        globalViewModel_!!.currentUsersRoutines!![index]!!.routineData
    ) { routinePresets ->
        if(routinePresets.isNotEmpty()) {
            for (routinePreset in routinePresets) {
                routineActivitySoundUrisMapList[index][routinePreset!!.soundPresetData.id] =
                    mutableListOf()
                routineActivitySoundUriVolumes[index][routinePreset.soundPresetData.id] =
                    routinePreset.soundPresetData.volumes
            }
            globalViewModel_!!.currentRoutinePlayingRoutinePresets = routinePresets
            completed()
        }
    }
}

fun getRoutinePresetsBasedOnRoutine(
    routineData: RoutineData,
    completed: (routinePresetList: MutableList<RoutineSoundPreset?>) -> Unit
) {
    RoutineSoundPresetBackend.queryRoutineSoundPresetBasedOnRoutine(routineData) { routinePresets ->
        completed(routinePresets.toMutableList())
    }
}

private fun retrieveSoundUris(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context
) {
    SoundBackend.querySoundBasedOnId(
        globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
            .soundPresetData.soundId
    ){
        if(it.isNotEmpty()) {
            playingSoundNow = it[0]
            SoundBackend.listS3Sounds(
                it[0]!!.audioKeyS3,
                it[0]!!.soundOwner.amplifyAuthUserId
            ) { s3List ->
                s3List.items.forEachIndexed { i, item ->
                    SoundBackend.retrieveAudio(
                        item.key,
                        it[0]!!.soundOwner.amplifyAuthUserId
                    ) { uri ->
                        routineActivitySoundUrisMapList[index][
                                globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                                        [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                                    .soundPresetData.id
                        ]!!.add(uri)
                        if (i == s3List.items.indices.last) {
                            startSound(
                                soundMediaPlayerService,
                                generalMediaPlayerService,
                                index,
                                context
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun startSound(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context,
) {
    if(
        !routineActivitySoundUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                    .soundPresetData.id
        ].isNullOrEmpty()
    ) {
        if(soundMediaPlayerService.areMediaPlayersInitialized()){
            soundMediaPlayerService.startMediaPlayers()
        }else{
            initializeMediaPlayers(
                soundMediaPlayerService,
                generalMediaPlayerService,
                index,
                context
            )
        }

        routineActivityPlayButtonTexts[index]!!.value = PAUSE_ROUTINE
        soundMediaPlayerService.loopMediaPlayers()
        //globalViewModel_!!.previouslyPlayedUserSoundRelationship = globalViewModel_!!.currentUsersSoundRelationships!![index]
        //globalViewModel_!!.generalPlaytimeTimer.start()
        setGlobalPropertiesAfterPlayingSound(index, context)
    }
}

private fun initializeMediaPlayers(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context,
) {
    /*updatePreviousUserSoundRelationship {
        updateCurrentUserSoundRelationshipUsageTimeStamp(index) {}
        globalViewModel_!!.previouslyPlayedUserSoundRelationship = null
    }*/
    soundMediaPlayerService.onDestroy()
    soundMediaPlayerService.setAudioUris(
        routineActivitySoundUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                    .soundPresetData.id
        ]!!
    )
    soundMediaPlayerService.setVolumes(
        routineActivitySoundUriVolumes[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                    .soundPresetData.id
        ]!!
    )
    val intent = Intent()
    intent.action = "PLAY"
    soundMediaPlayerService.onStartCommand(intent, 0, 0)
    resetAll(context, soundMediaPlayerService)
    resetGlobalControlButtons()
    startSoundCDT(
        soundMediaPlayerService,
        generalMediaPlayerService,
        index,
        context
    )
}

private fun startSoundCDT(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    index: Int,
    context: Context,
){
    startSoundCountDownTimer(
        context,
        globalViewModel_!!.currentUsersRoutines!![index]!!.routineData.eachSoundPlayTime.toLong(),
        soundMediaPlayerService
    ){
        if(soundMediaPlayerService.areMediaPlayersInitialized()) {
            soundMediaPlayerService.onDestroy()
        }
        resetGlobalControlButtons()
        globalViewModel_!!.isCurrentSoundPlaying = false
        globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = null

        globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex = globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!! + 1
        if(globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!! > globalViewModel_!!.currentRoutinePlayingRoutinePresets!!.indices.last){
            globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex = 0
        }
        playSound(
            soundMediaPlayerService,
            generalMediaPlayerService,
            index,
            context
        )
    }
}

private fun startSoundCountDownTimer(
    context: Context,
    time: Long,
    soundMediaPlayerService: SoundMediaPlayerService,
    completed: () -> Unit
){
    if(globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer == null){
        globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer = object : CountDownTimer(time, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "Sound has been going for $millisUntilFinished")
            }
            override fun onFinish() {
                completed()
                Log.i(TAG, "sound Timer stopped")
            }
        }
    }
    globalViewModel_!!.currentRoutinePlayingSoundCountDownTimer!!.start()
}

private fun setGlobalPropertiesAfterPlayingSound(index: Int, context: Context) {
    globalViewModel_!!.currentSoundPlaying = playingSoundNow

    globalViewModel_!!.currentSoundPlayingPreset =
        globalViewModel_!!.currentRoutinePlayingRoutinePresets!![
                globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!
        ]!!.soundPresetData

    globalViewModel_!!.currentSoundPlayingSliderPositions.clear()

    globalViewModel_!!.soundSliderVolumes =
        globalViewModel_!!.currentRoutinePlayingRoutinePresets!![
                globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!
        ]!!.soundPresetData.volumes

    for (volume in globalViewModel_!!.soundSliderVolumes!!) {
        globalViewModel_!!.currentSoundPlayingSliderPositions.add(
            mutableStateOf(volume.toFloat())
        )
    }

    globalViewModel_!!.currentSoundPlayingUris =
        routineActivitySoundUrisMapList[index][
                globalViewModel_!!.currentRoutinePlayingRoutinePresets!!
                        [globalViewModel_!!.currentRoutinePlayingRoutinePresetsIndex!!]!!
                    .soundPresetData.id
        ]

    globalViewModel_!!.currentSoundPlayingContext = context
    globalViewModel_!!.isCurrentSoundPlaying = true
    globalViewModel_!!.isCurrentRoutinePlaying = true
    com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(3)
    com.example.eunoia.ui.bottomSheets.sound.deActivateGlobalControlButton(1)
    com.example.eunoia.ui.bottomSheets.sound.activateGlobalControlButton(0)
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