package com.example.eunoia.dashboard.sound

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.EUNOIATheme

@Composable
fun EunoiaSoundScreen(navController: NavController, soundText: String){
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
            Mixer(soundText)
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
        EunoiaSoundScreen(rememberNavController(), "pouring_rain")
    }
}