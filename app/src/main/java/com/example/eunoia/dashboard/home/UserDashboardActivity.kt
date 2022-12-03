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
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amazonaws.util.DateUtils
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.UserData
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.backend.UserRoutineRelationshipBackend
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.createPrayer.uploadedAudioFileLengthMilliSecondsPrayer
import com.example.eunoia.create.createPrayer.uploadedFileColorPrayer
import com.example.eunoia.create.createPrayer.uploadedFilePrayer
import com.example.eunoia.create.createPrayer.uploadedFileUriPrayer
import com.example.eunoia.create.createSelfLove.uploadedAudioFileLengthMilliSecondsSelfLove
import com.example.eunoia.create.createSelfLove.uploadedFileColorSelfLove
import com.example.eunoia.create.createSelfLove.uploadedFileSelfLove
import com.example.eunoia.create.createSelfLove.uploadedFileUriSelfLove
import com.example.eunoia.create.createSound.audioFileLengthMilliSeconds
import com.example.eunoia.create.createSound.fileColors
import com.example.eunoia.create.createSound.fileUris
import com.example.eunoia.create.createSound.selectedIndex
import com.example.eunoia.create.createSound.uploadedFiles
import com.example.eunoia.create.resetEverything
import com.example.eunoia.create.resetEverythingExceptRoutine
import com.example.eunoia.dashboard.bedtimeStory.updatePreviousUserBedtimeStoryRelationship
import com.example.eunoia.dashboard.home.BedtimeStoryForRoutine.resetBtsCDT
import com.example.eunoia.dashboard.home.SelfLoveForRoutine.resetSelfLoveCDT
import com.example.eunoia.dashboard.prayer.updatePreviousUserPrayerRelationship
import com.example.eunoia.dashboard.selfLove.updatePreviousUserSelfLoveRelationship
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserRoutineRelationshipObject
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.sign_in_process.SignInActivity
import com.example.eunoia.ui.alertDialogs.ConfirmAlertDialog
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.bottomSheets.recordAudio.recorder
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingFile
import com.example.eunoia.ui.bottomSheets.recordAudio.recordingTimeDisplay
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.*
import com.example.eunoia.utils.Timer
import com.example.eunoia.viewModels.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import java.io.DataOutputStream
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

@AndroidEntryPoint
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
        var weakActivity: WeakReference<UserDashboardActivity> = WeakReference(UserDashboardActivity())

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

    fun convertUrisToAudioStreams(uris: List<Uri>): File{
        val audioStreams = mutableListOf<InputStream>()
        val tempFile = File.createTempFile("audio", ".aac")
        for(uri in uris){
            val audioStream = uri.let {
                contentResolver.openInputStream(it)
            }
            Log.i(TAG, "audioStream is: $audioStream")
            audioStreams.add(audioStream!!)
        }

        Log.i(TAG, "audioStreams is: $audioStreams")
        Log.i(TAG, "audioStreams length: ${audioStreams.size}")
        copyMultipleStreamsToAFile(audioStreams, tempFile)
        return tempFile
    }

    private fun copyMultipleStreamsToAFile(inputStreams: List<InputStream>, outputFile: File) {
        val fos = FileOutputStream(outputFile)
        val dataOutputStream = DataOutputStream(fos)
        Log.i(TAG, "outputFile name: ${outputFile.name}")
        val finalByteList = mutableListOf<Byte>()
        Log.i(TAG, "finalByteList is: $finalByteList")
        Log.i(TAG, "inputStreams is: $inputStreams")
        Log.i(TAG, "dataOutputStream is: $dataOutputStream")
        inputStreams.forEach { inputStream ->
            val buffer = ByteArray(4 * 1024) // buffer size
            Log.i(TAG, "buffer is: $buffer")
            while (true) {
                val byteCount = inputStream.read(buffer)
                Log.i(TAG, "byteCount is: $byteCount")
                if (byteCount < 0) break
                buffer.forEach {
                    finalByteList.add(it)
                    //Log.i(TAG, "Byte is: $it")
                }
            }
        }
        dataOutputStream.write(finalByteList.toByteArray())
        Log.i(TAG, "finalByteList: ${finalByteList.toByteArray().size}")
        Log.i(TAG, "dataOutputStream length: ${dataOutputStream.size()}")
        Log.i(TAG, "outputFile length: ${outputFile.length()}")
        dataOutputStream.flush()
        dataOutputStream.close()
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
            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying != null
        ){
            bedtimeStoryViewModel!!.bedtimeStoryTimeDisplay = durationString
            bedtimeStoryViewModel!!.bedtimeStoryCircularSliderClicked = false
            bedtimeStoryViewModel!!.bedtimeStoryCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            bedtimeStoryViewModel!!.currentBedtimeStoryPlaying!!.fullPlayTime
                    ) * 360f
        }
        //TODO if current position of media player == duration of media player, stop media player,
        // set controls to represent pause mode
    }

    override fun onSelfLoveTimerTick(durationString: String, durationMilliSeconds: Long) {
        if (
            generalMediaPlayerService_!!.isMediaPlayerInitialized() &&
            selfLoveViewModel!!.currentSelfLovePlaying != null
        ) {
            selfLoveViewModel!!.selfLoveTimeDisplay = durationString
            selfLoveViewModel!!.selfLoveCircularSliderClicked = false
            selfLoveViewModel!!.selfLoveCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            (selfLoveViewModel!!.currentSelfLovePlaying!!.fullPlayTime).toFloat()
                    ) * 360f
        }
    }

    override fun onPrayerTimerTick(durationString: String, durationMilliSeconds: Long) {
        if (
            generalMediaPlayerService_!!.isMediaPlayerInitialized() &&
            prayerViewModel!!.currentPrayerPlaying != null
        ) {
            prayerViewModel!!.prayerTimeDisplay = durationString
            prayerViewModel!!.prayerCircularSliderClicked = false
            prayerViewModel!!.prayerCircularSliderAngle = (
                    (generalMediaPlayerService_!!.getMediaPlayer()!!.currentPosition).toFloat() /
                            (prayerViewModel!!.currentPrayerPlaying!!.fullPlayTime).toFloat()
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
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    globalViewModel: GlobalViewModel,
    bedtimeStoryViewModel: BedtimeStoryViewModel,
    soundViewModel: SoundViewModel,
    prayerViewModel: PrayerViewModel,
    selfLoveViewModel: SelfLoveViewModel,
    routineViewModel: RoutineViewModel,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService
) {
    val context = LocalContext.current

    SetUpRoutineCurrentlyPlayingAlertDialogRoutineUI(
        soundMediaPlayerService,
        generalMediaPlayerService,
        context
    )

    //globalViewModel!!.navController = navController
    val scrollState = rememberScrollState()

    var retrievedUserRoutineRelationships by rememberSaveable{ mutableStateOf(false) }
    globalViewModel!!.currentUser?.let {
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

            val itMut = userRoutineRelationships.toMutableList()
            itMut.sortByDescending{ userRoutineRelationship ->
                userRoutineRelationship!!.numberOfTimesPlayed
            }

            routineViewModel!!.currentUsersRoutineRelationships = itMut
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
                    globalViewModel!!.bottomSheetOpenFor = "controls"
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
                .constrainAs(options) {
                    top.linkTo(introTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
                .testTag("options")
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
                routineViewModel!!.currentUsersRoutineRelationships != null &&
                retrievedUserRoutineRelationships
            ){
                if(routineViewModel!!.currentUsersRoutineRelationships!!.size > 0) {
                    for(i in routineViewModel!!.currentUsersRoutineRelationships!!.indices){
                        setRoutineActivityPlayButtonTextsCorrectly(i)
                        UserRoutineRelationshipCard(
                            routineViewModel!!.currentUsersRoutineRelationships!![i]!!,
                            i,
                            { index ->
                                routineViewModel!!.routineIndex = index
                                resetCurrentlyPlayingRoutineIfNecessary(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context
                                )
                            },
                            {
                                Log.i(TAG, "globalViewModel!!.currentUsersRoutineRelationships!![i]!! = " +
                                        "${routineViewModel!!.currentUsersRoutineRelationships!![i]!!}")
                                navigateToUserRoutineRelationshipScreen(
                                    navController,
                                    routineViewModel!!.currentUsersRoutineRelationships!![i]!!
                                )
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

@Composable
private fun SetUpRoutineCurrentlyPlayingAlertDialogRoutineUI(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
){
    if(openRoutineIsCurrentlyPlayingDialogBox){
        ConfirmAlertDialog(
            "Are you sure you want to stop your routine?",
            {
                Log.i(TAG, "k - 1")
                updatePreviousUserRoutineRelationship {
                    Log.i(TAG, "k - 2")
                    updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
                        Log.i(TAG, "k - 3")
                        updatePreviousUserPrayerRelationship {
                            Log.i(TAG, "k - 4")
                            updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
                                Log.i(TAG, "k - 5")
                                resetEverything(
                                    soundMediaPlayerService,
                                    generalMediaPlayerService,
                                    context
                                ) {
                                    UserRoutineRelationshipBackend.queryUserRoutineRelationshipBasedOnId(
                                        routineViewModel!!.currentUsersRoutineRelationships!![routineViewModel!!.routineIndex]!!.id
                                    ){
                                        if(it.isNotEmpty()) {
                                            routineViewModel!!.currentUsersRoutineRelationships!![routineViewModel!!.routineIndex] =
                                                it[0]
                                        }
                                        startRoutineConfirmed(
                                            soundMediaPlayerService,
                                            generalMediaPlayerService,
                                            context
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            {
                openRoutineIsCurrentlyPlayingDialogBox = false
            }
        )
    }
}

private fun startRoutineConfirmed(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
){
    if(routineViewModel!!.currentUserRoutineRelationshipPlaying != null){
        if(routineViewModel!!.currentUserRoutineRelationshipPlaying!!.id == routineViewModel!!.currentUsersRoutineRelationships!![routineViewModel!!.routineIndex]!!.id){
            resetRoutinePlayButtonTextsIfNecessary(routineViewModel!!.routineIndex)
            selectNextRoutineElement(
                soundMediaPlayerService,
                generalMediaPlayerService,
                routineViewModel!!.routineIndex,
                context
            )
        }else{
            resetRoutineMediaPlayerServicesIfNecessary(
                soundMediaPlayerService,
                generalMediaPlayerService,
                routineViewModel!!.routineIndex,
                context
            ){
                resetRoutinePlayButtonTextsIfNecessary(routineViewModel!!.routineIndex)
                selectNextRoutineElement(
                    soundMediaPlayerService,
                    generalMediaPlayerService,
                    routineViewModel!!.routineIndex,
                    context
                )
            }
        }
    }else {
        Log.i(TAG, "gg")
        resetRoutineMediaPlayerServicesIfNecessary(
            soundMediaPlayerService,
            generalMediaPlayerService,
            routineViewModel!!.routineIndex,
            context
        ) {
            resetRoutinePlayButtonTextsIfNecessary(routineViewModel!!.routineIndex)
            selectNextRoutineElement(
                soundMediaPlayerService,
                generalMediaPlayerService,
                routineViewModel!!.routineIndex,
                context
            )
        }
    }
    openRoutineIsCurrentlyPlayingDialogBox = false
}

private fun resetCurrentlyPlayingRoutineIfNecessary(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
) {
    if(
        routineViewModel!!.currentRoutinePlaying != null &&
        routineViewModel!!.currentUserRoutineRelationshipPlaying != null &&
        routineViewModel!!.currentUserRoutineRelationshipPlaying!!.id != routineViewModel!!.currentUsersRoutineRelationships!![routineViewModel!!.routineIndex]!!.id
    ){
        openRoutineIsCurrentlyPlayingDialogBox = true
    }else{
        startRoutineConfirmed(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        )
    }
}

private fun setRoutineActivityPlayButtonTextsCorrectly(
    i: Int,
) {
    if (routineViewModel!!.currentUserRoutineRelationshipPlaying != null) {
        if (
            routineViewModel!!.currentUserRoutineRelationshipPlaying!!.id ==
            routineViewModel!!.currentUsersRoutineRelationships!![i]!!.id
        ) {
            if(routineViewModel!!.isCurrentRoutinePlaying){
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
        if(routineViewModel!!.currentUserRoutineRelationshipPlaying != null) {
            if (routineViewModel!!.currentUsersRoutineRelationships!![index]!!.id != routineViewModel!!.currentUserRoutineRelationshipPlaying!!.id) {
                routineViewModel!!.currentRoutinePlayingOrderIndex = 0
                routineViewModel!!.currentRoutinePlayingOrder = null
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = 0
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets = null
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.currentPrayerPlayingIndex
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers = null
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.currentBedtimeStoryPlayingIndex
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = null
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = routineViewModel!!.currentUsersRoutineRelationships!![index]!!.currentSelfLovePlayingIndex
                routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = null
                //countdown timers
                resetGlobalRoutineCountdownTimers()

                globalViewModel!!.resetCDT()

                soundMediaPlayerService.onDestroy()
                generalMediaPlayerService.onDestroy()
            }
        }
        completed()
    }
}

fun resetGlobalRoutineCountdownTimers() {
    if(routineViewModel!!.currentRoutinePlayingSoundCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingSoundCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingSoundCountDownTimer = null
    }

    if(routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingPrayerCountDownTimer = null
    }

    if(routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingNextPrayerCountDownTimer = null
    }

    if(routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingSelfLoveCountDownTimer = null
    }

    if(routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingNextSelfLoveCountDownTimer = null
    }

    if(routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingBedtimeStoryCountDownTimer = null
    }

    if(routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer != null) {
        routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer!!.cancel()
        routineViewModel!!.currentRoutinePlayingNextBedtimeStoryCountDownTimer = null
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
    updateRoutineOncePlayIsClicked(
        routineViewModel!!.currentRoutinePlayingOrderIndex!!,
        routineViewModel!!.currentUsersRoutineRelationships!![index]!!,
    ) {
        if(it != null) {
            routineViewModel!!.currentRoutinePlaying = it.userRoutineRelationshipRoutine
            routineViewModel!!.currentUsersRoutineRelationships!![index] = it
        }

        globalViewModel!!.routinePlaytimeTimer.start()
        routineViewModel!!.currentRoutinePlayingOrder =
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!.playingOrder
        routineViewModel!!.currentUserRoutineRelationshipPlaying =
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!
        Log.i(
            TAG,
            "Our routine playing orderz is ${routineViewModel!!.currentRoutinePlayingOrder!![routineViewModel!!.currentRoutinePlayingOrderIndex!!]}"
        )
        when (routineViewModel!!.currentRoutinePlayingOrder!![routineViewModel!!.currentRoutinePlayingOrderIndex!!]) {
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

fun updateRoutineOncePlayIsClicked(
    playingOrderIndex: Int,
    updatedUserRoutineRelationship: UserRoutineRelationship,
    completed: (updatedUserRoutineRelationship: UserRoutineRelationship?) -> Unit
) {
    if (playingOrderIndex == 0) {
        updatePreviousUserRoutineRelationship() {
            updateRecentlyPlayedUserRoutineRelationshipWithUserRoutineRelationship(
                updatedUserRoutineRelationship,
            ) {
                completed(it)
            }
        }
    }else{
        completed(null)
    }
}

fun updatePreviousUserRoutineRelationship(
    completed: (updatedUserRoutineRelationship: UserRoutineRelationship?) -> Unit
) {
    if(routineViewModel!!.previouslyPlayedUserRoutineRelationship != null){
        val playTime = globalViewModel!!.routinePlaytimeTimer.getDuration()
        globalViewModel!!.routinePlaytimeTimer.stop()

        val totalPlayTime = routineViewModel!!.previouslyPlayedUserRoutineRelationship!!.totalPlayTime + playTime

        var usagePlayTimes = routineViewModel!!.previouslyPlayedUserRoutineRelationship!!.usagePlayTimes
        if(usagePlayTimes != null) {
            usagePlayTimes.add(playTime.toInt())
        }else{
            usagePlayTimes = listOf(playTime.toInt())
        }

        val numberOfTimesPlayed = routineViewModel!!.previouslyPlayedUserRoutineRelationship!!.numberOfTimesPlayed

        if(totalPlayTime > 0){
            val userRoutineRelationship = routineViewModel!!.previouslyPlayedUserRoutineRelationship!!.copyOfBuilder()
                .numberOfTimesPlayed(numberOfTimesPlayed)
                .totalPlayTime(totalPlayTime.toInt())
                .usagePlayTimes(usagePlayTimes)
                .build()

            UserRoutineRelationshipBackend.updateUserRoutineRelationship(userRoutineRelationship){
                routineViewModel!!.previouslyPlayedUserRoutineRelationship = null
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
        routineViewModel!!.previouslyPlayedUserRoutineRelationship = it
        completed(it)
    }
}

fun updateRecentlyPlayedUserRoutineRelationshipWithRoutine(
    routineData: RoutineData,
    completed: (userRoutineRelationship: UserRoutineRelationship) -> Unit
){
    routineViewModel!!.currentRoutinePlaying = routineData
    UserRoutineRelationshipBackend.queryUserRoutineRelationshipBasedOnUserAndRoutine(
        globalViewModel!!.currentUser!!,
        routineData
    ) { userRoutineRelationship ->
        if(userRoutineRelationship.isNotEmpty()) {
            updateCurrentUserRoutineRelationshipUsageTimeStamp(userRoutineRelationship[0]!!) {
                routineViewModel!!.previouslyPlayedUserRoutineRelationship = it
                completed(it)
            }
        }else{
            UserRoutineRelationshipBackend.createUserRoutineRelationshipObject(
                routineData
            ){ newUserRoutineRelationship ->
                updateCurrentUserRoutineRelationshipUsageTimeStamp(newUserRoutineRelationship) {
                    routineViewModel!!.previouslyPlayedUserRoutineRelationship = it
                    completed(it)
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
    Log.i(TAG, "plahing order index before increment is ${routineViewModel!!.currentRoutinePlayingOrderIndex}")
    routineViewModel!!.currentRoutinePlayingOrderIndex = routineViewModel!!.currentRoutinePlayingOrderIndex!! + 1
    if(routineViewModel!!.currentRoutinePlayingOrderIndex!! > routineViewModel!!.currentRoutinePlayingOrder!!.indices.last){
        updateUserRoutineRelationshipWhenRoutineIsDonePlaying(
            routineViewModel!!.currentUsersRoutineRelationships!![index]!!,
        ){
            Log.i(TAG, "Routine is endedz")
            generalMediaPlayerService.onDestroy()
            globalViewModel!!.resetCDT()
            resetBtsCDT()
            resetSelfLoveCDT()
            routineViewModel!!.currentUsersRoutineRelationships!![index] = it
            routineViewModel!!.currentRoutinePlayingOrderIndex = 0
            routineActivityPlayButtonTexts[index]!!.value = START_ROUTINE
            routineViewModel!!.currentRoutinePlaying = null
            routineViewModel!!.isCurrentRoutinePlaying = false
            routineViewModel!!.currentUserRoutineRelationshipPlaying = null
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
    userRoutineRelationship: UserRoutineRelationship,
    completed: (updatedUserRoutineRelationship: UserRoutineRelationship?) -> Unit
) {
    val playTime = globalViewModel!!.routinePlaytimeTimer.getDuration()
    globalViewModel!!.routinePlaytimeTimer.stop()

    val totalPlayTime = userRoutineRelationship.totalPlayTime + playTime

    var usagePlayTimes = userRoutineRelationship.usagePlayTimes
    if(usagePlayTimes != null) {
        usagePlayTimes.add(playTime.toInt())
    }else{
        usagePlayTimes = listOf(playTime.toInt())
    }

    val numberOfTimesPlayed = userRoutineRelationship.numberOfTimesPlayed

    if(totalPlayTime > 0){
        val newUserRoutineRelationship = userRoutineRelationship.copyOfBuilder()
            .numberOfTimesPlayed(numberOfTimesPlayed)
            .totalPlayTime(totalPlayTime.toInt())
            .usagePlayTimes(usagePlayTimes)
            .build()

        UserRoutineRelationshipBackend.updateUserRoutineRelationship(newUserRoutineRelationship){
            routineViewModel!!.previouslyPlayedUserRoutineRelationship = null
            completed(it)
        }
    }
}

fun resetRoutineGlobalProperties(){
    resetGlobalRoutineCountdownTimers()
    routineViewModel!!.currentRoutinePlaying = null
    routineViewModel!!.currentUserRoutineRelationshipPlaying = null
    routineViewModel!!.isCurrentRoutinePlaying = false

    routineViewModel!!.currentRoutinePlayingOrderIndex = 0
    routineViewModel!!.currentRoutinePlayingOrder = null
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresetsIndex = 0
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPresets = null
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayersIndex = 0
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipPrayers = null
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStoriesIndex = 0
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipBedtimeStories = null
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLovesIndex = 0
    routineViewModel!!.currentRoutinePlayingUserRoutineRelationshipSelfLoves = null
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

fun navigateToUserRoutineRelationshipScreen(navController: NavController, userRoutineRelationship: UserRoutineRelationship){
    Log.i(TAG, "userRoutineRelationshiz = $userRoutineRelationship")
    navController.navigate("${Screen.UserRoutineRelationshipScreen.screen_route}/userRoutineRelationship=${UserRoutineRelationshipObject.UserRoutineRelationshipModel.from(userRoutineRelationship)}")
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