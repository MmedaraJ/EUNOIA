package com.example.eunoia.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.eunoia.dashboard.sound.SimpleFlowRow
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.SoftPeach

@Composable
fun Elements(navController: NavController){
    val allElements = listOf(
        "sound",
        "prayer",
        "bedtime story",
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
                            if(element == "sound"){
                                navController.navigate(Screen.NameSound.screen_route)
                            }
                        }
                ) {
                    Card(
                        modifier = Modifier
                            .height(179.dp)
                            .fillMaxWidth(0.45F),
                        shape = MaterialTheme.shapes.large,
                        backgroundColor = SoftPeach,
                        elevation = 2.dp
                    ) {
                        ConstraintLayout {
                            val (box) = createRefs()
                            Column(
                                modifier = Modifier
                                    .constrainAs(box) {
                                        top.linkTo(parent.top, margin = 0.dp)
                                        end.linkTo(parent.end, margin = 0.dp)
                                        start.linkTo(parent.start, margin = 0.dp)
                                        bottom.linkTo(parent.bottom, margin = 0.dp)
                                    }
                            ){
                                NormalText(
                                    text = element,
                                    color = Black,
                                    fontSize = 16,
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