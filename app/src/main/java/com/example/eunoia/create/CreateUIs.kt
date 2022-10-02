package com.example.eunoia.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.SimpleFlowRow
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SoftPeach

@Composable
fun Elements(navController: NavController){
    val allElements = listOf(
        "sound",
        "prayer",
        "bedtime\nstory",
        "self-love",
    )

    val borders = mutableListOf<MutableState<Boolean>>()
    for(i in allElements.indices){
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
            allElements.forEachIndexed { index, element ->
                var cardModifier = Modifier
                    .clickable {
                        borders.forEach { border ->
                            border.value = false
                        }
                        borders[index].value = !borders[index].value

                        when (element) {
                            "sound" -> {
                                navController.navigate(Screen.NameSound.screen_route)
                            }
                            "prayer" -> {
                                navController.navigate(Screen.NamePrayer.screen_route)
                            }
                            "bedtime\nstory" -> {
                                navController.navigate(Screen.NameBedtimeStory.screen_route)
                            }
                            "self-love" -> {
                                navController.navigate(Screen.NameSelfLove.screen_route)
                            }
                        }
                    }

                if (borders[index].value) {
                    cardModifier = cardModifier.then(
                        Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.large)
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
                                .background(SoftPeach)
                                .weight(1f)
                                .aspectRatio(1f)
                        ){
                            AlignedNormalText(
                                text = element,
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