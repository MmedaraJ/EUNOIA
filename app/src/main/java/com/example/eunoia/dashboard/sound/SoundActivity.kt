package com.example.eunoia.dashboard.sound

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme
import kotlinx.coroutines.*

val scope = CoroutineScope(Job() + Dispatchers.IO)

@Composable
fun SoundActivityUI(navController: NavController, context: Context) {
    val scrollState = rememberScrollState()
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
            BackArrowHeader({navController.popBackStack()}, {navController.navigate(Screen.Settings.screen_route)})
        }
        Column(
            modifier = Modifier
                .constrainAs(introTitle){
                    top.linkTo(header.bottom, margin = 20.dp)
                }
        ){
            NormalText(
                text = "Sound",
                color = Color.Black,
                13,
                xOffset = 6,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier.constrainAs(options){
                top.linkTo(introTitle.bottom, margin = 8.dp)
            }
        ) {
            OptionsList(navController, context)
        }
        Column(
            modifier = Modifier
                .constrainAs(favorite_routine_title) {
                    top.linkTo(options.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Sounds")
        }
        Column(
            modifier = Modifier
                .constrainAs(emptyRoutine) {
                    top.linkTo(favorite_routine_title.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            UserSoundList()
        }
        Column(
            modifier = Modifier
                .constrainAs(articles_title) {
                    top.linkTo(emptyRoutine.bottom, margin = 0.dp)
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
            ArticlesList()
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
private fun OptionsList(navController: NavController, context: Context){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(name = "pouring rain", icon = R.drawable.pouring_rain_icon, 71, 71, false, 0, 0){ toPouringRain(navController) }
        OptionItem(name = "coffee house", icon = R.drawable.coffee_house_icon, 71, 71, true, 35, -10){ something() }
        OptionItem(name = "library", icon = R.drawable.library_icon, 71, 71, false, 0, 0){ something() }
        OptionItem(name = "baking", icon = R.drawable.baking_icon, 71, 71, false, 0, 0){ something() }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(name = "beach waves", icon = R.drawable.beach_waves_icon, 71, 71, false, 0, 0){ something() }
        OptionItem(name = "next door", icon = R.drawable.next_door_icon, 71, 71, false, 0, 0){ something() }
        OptionItem(name = "keyboard", icon = R.drawable.keyboard_icon, 71, 71, true, 35, -10){ something() }
        OptionItem(name = "train track", icon = R.drawable.train_track_icon, 71, 71, true, 35, -10){ something() }
    }
}

@Composable
private fun UserSoundList(){
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
private fun ArticlesList(){
    Column(
    ) {
        Article(
            title = "the danger of sleeping pills",
            summary = "Sleeping pills are not meant to be taken daily.",
            icon = R.drawable.danger_of_sleeping_pills_icon
        ) {
            something()
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

private fun toPouringRain(navController: NavController){
    //navController.navigate(Screen.Sound.screen_route)
    /*Log.i("SoundActivityUI", "1. Get Pouring rain")
    retrieveAllEunoiaRoutineSoundMp3For("Pouring_Rain"){
        Log.i("SoundActivityUI", "Get Pouring rain")
    }*/
    navController.navigate(Screen.PouringRain.screen_route)
}

private fun something(){

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EUNOIATheme {
        SoundActivityUI(rememberNavController(), LocalContext.current)
    }
}