package com.example.eunoia.dashboard.routine

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.RoutineData
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
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
            routineData.playingOrder.forEach { element ->
                var text = element
                if(element == "bedtimeStory"){
                    text = "bedtime\nstory"
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
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
                                .gradientBackground(
                                    listOf(
                                        SoftPeach,
                                        Snuff,
                                    ),
                                    angle = 180f
                                )
                                .clickable { }
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