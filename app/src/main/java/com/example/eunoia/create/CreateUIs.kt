package com.example.eunoia.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
            allElements.forEach { element ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
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