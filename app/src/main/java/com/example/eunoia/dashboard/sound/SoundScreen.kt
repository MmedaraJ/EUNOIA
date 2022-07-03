package com.example.eunoia.dashboard.sound

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.backend.SoundBackend
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.EUNOIATheme

private const val TAG = "SoundScreen"

@Composable
fun SoundScreen(navController: NavController, soundDisplayName: String, context: Context){
    var sound: SoundData? by rememberSaveable{ mutableStateOf(null) }
    SoundBackend.querySoundBasedOnDisplayName(soundDisplayName, LocalContext.current){
        sound = it
    }
    if(sound == null){
        ConstraintLayout{
            val (progressBar) = createRefs()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CircularProgressIndicator()
            }
        }
    }else{
        Log.i(TAG, "$sound")
        val scrollState = rememberScrollState()
        var showTapText by rememberSaveable{ mutableStateOf(true) }
        var manualTopMargin by rememberSaveable{ mutableStateOf(-260) }
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            val (
                header,
                mixer,
                manual,
                word_of_mouth_text,
                user_comment,
                tip,
                other_user_feedback,
                endSpace
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(header) {
                        top.linkTo(parent.top, margin = 40.dp)
                    }
                    .fillMaxWidth()
            ) {
                BackArrowHeader({ navController.popBackStack() }, {navController.navigate(Screen.Settings.screen_route)})
            }
            Column(
                modifier = Modifier
                    .constrainAs(manual) {
                        top.linkTo(mixer.bottom, margin = manualTopMargin.dp)
                    }
                    .wrapContentHeight()
            ) {
                ControlPanelManual(showTapText){
                    showTapText = !showTapText
                    manualTopMargin = if (manualTopMargin == 6) -260 else 6
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(mixer) {
                        top.linkTo(header.bottom, margin = 50.dp)
                    }
            ) {
                val sliderPositions = arrayOf(
                    remember { mutableStateOf(sound!!.currentVolumes[0].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[1].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[2].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[3].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[4].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[5].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[6].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[7].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[8].toFloat()) },
                    remember { mutableStateOf(sound!!.currentVolumes[9].toFloat()) }
                )
                sound?.let { Mixer(it, context, sliderPositions) }
            }
            Column(
                modifier = Modifier
                    .constrainAs(word_of_mouth_text) {
                        top.linkTo(manual.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                StarSurroundedText("Word-of-mouth")
            }
            Column(
                modifier = Modifier
                    .constrainAs(user_comment) {
                        top.linkTo(word_of_mouth_text.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                if(sound?.ownerUsername == Amplify.Auth.currentUser.username) {
                    bigOutlinedTextInput(
                        100,
                        "Share how you feel about this sound",
                        MaterialTheme.colors.primaryVariant,
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onPrimary,
                        Black,
                        13
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(tip) {
                        top.linkTo(user_comment.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                Tip()
            }
            Column(
                modifier = Modifier
                    .constrainAs(other_user_feedback) {
                        top.linkTo(tip.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                OtherUsersFeedback("Listening to this with train and railroad noise, and its great " +
                        "for getting my ADHD brain to sit on the proverbial track and stay on it. It's so " +
                        "helpful as I try and write up my PhD thesis. I would not be able to do it without " +
                        "this app for sure. Thank you so much!"){}
            }
            Column(
                modifier = Modifier
                    .constrainAs(endSpace) {
                        top.linkTo(tip.bottom, margin = 20.dp)
                    }
            ){
                Spacer(modifier = Modifier.height(32.dp))
            }
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
fun Preview() {
    EUNOIATheme {
        SoundScreen(rememberNavController(), "pouring_rain", LocalContext.current)
    }
}