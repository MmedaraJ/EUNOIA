package com.example.eunoia.dashboard.routine

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.RoutineData
import com.amplifyframework.datastore.generated.model.RoutinePreset
import com.amplifyframework.datastore.generated.model.UserBedtimeStoryInfo
import com.amplifyframework.datastore.generated.model.UserRoutine
import com.example.eunoia.backend.RoutinePresetBackend
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.navigateToSoundScreen
import com.example.eunoia.models.BedtimeStoryObject
import com.example.eunoia.models.RoutineObject
import com.example.eunoia.ui.components.AlignedLightText
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.SimpleFlowRow
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.Snuff
import com.example.eunoia.ui.theme.SoftPeach

@Composable
fun RoutineElements(
    navController: NavController,
    routineData: RoutineData
){
    val borders = mutableListOf<MutableState<Boolean>>()
    for(i in routineData.playingOrder.indices){
        borders.add(remember { mutableStateOf(false) })
    }

    ConstraintLayout{
        val (
            elements,
        ) = createRefs()
        SimpleFlowRow(
            verticalGap = 12.dp,
            horizontalGap = 12.dp,
            alignment = Alignment.Start,
            modifier = Modifier
                .constrainAs(elements) {
                    top.linkTo(parent.top, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            routineData.playingOrder.forEachIndexed {index, element ->
                var cardModifier = Modifier
                    .clickable {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value
                        //element clicked
                        routineElementClicked(
                            navController,
                            element,
                            routineData
                        )
                    }

                if (borders[index].value) {
                    cardModifier = cardModifier.then(
                        Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.large)
                    )
                }

                var text = element
                if(text == "bedtimeStory"){
                    text = "bedtime\nstory"
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = cardModifier
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.45F),
                        shape = MaterialTheme.shapes.large,
                        elevation = 2.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .gradientBackground(
                                    listOf(
                                        SoftPeach,
                                        Snuff,
                                    ),
                                    angle = 180f
                                )
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            AlignedNormalText(
                                text = text,
                                color = Black,
                                fontSize = 13,
                                xOffset = 0,
                                yOffset = 0
                            )
                        }
                    }
                }
            }
        }
    }
}

fun routineElementClicked(
    navController: NavController,
    element: String,
    routineData: RoutineData
) {
    when(element){
        "sound" -> {
            navigateToRoutinePresetsPage(navController, routineData)
        }
    }
}

fun navigateToRoutinePresetsPage(navController: NavController, routineData: RoutineData) {
    navController.navigate("${Screen.RoutinePresetScreen.screen_route}/routineData=${RoutineObject.Routine.from(routineData)}")
}

private const val TAG = "Routine Preset Screen"

@Composable
fun RoutinePresetElements(
    navController: NavController,
    routineData: RoutineData
){
    var retrievedRoutinePresets by rememberSaveable{ mutableStateOf(false) }

    RoutinePresetBackend.queryRoutinePresetBasedOnRoutine(routineData) { routinePresets ->
        routinePresetList = routinePresets.toMutableList()
        retrievedRoutinePresets = true
    }

    if(retrievedRoutinePresets) {
        if (routinePresetList!!.isNotEmpty()) {
            val borders = mutableListOf<MutableState<Boolean>>()
            for (i in routinePresetList!!.indices) {
                borders.add(remember { mutableStateOf(false) })
            }

            ConstraintLayout {
                val (
                    elements,
                ) = createRefs()
                SimpleFlowRow(
                    verticalGap = 12.dp,
                    horizontalGap = 12.dp,
                    alignment = Alignment.Start,
                    modifier = Modifier
                        .constrainAs(elements) {
                            top.linkTo(parent.top, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                        }
                ) {
                    routinePresetList!!.forEachIndexed { index, element ->
                        var cardModifier = Modifier
                            .clickable {
                                borders.forEach { border ->
                                    border.value = false
                                }
                                borders[index].value = !borders[index].value
                                navigateToSoundScreen(navController, element!!.presetData.sound)
                            }

                        if (borders[index].value) {
                            cardModifier = cardModifier.then(
                                Modifier.border(
                                    BorderStroke(1.dp, Black),
                                    MaterialTheme.shapes.large
                                )
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = cardModifier
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.45F),
                                shape = MaterialTheme.shapes.large,
                                elevation = 2.dp
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .gradientBackground(
                                            listOf(
                                                SoftPeach,
                                                Snuff,
                                            ),
                                            angle = 180f
                                        )
                                        .weight(1f)
                                        .aspectRatio(1f)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ){
                                        AlignedNormalText(
                                            text = element!!.presetData.key,
                                            color = Black,
                                            fontSize = 13,
                                            xOffset = 0,
                                            yOffset = 0
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }else{
        //you don't have any sounds
    }
}