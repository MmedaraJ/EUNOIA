package com.example.eunoia.settings

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*

private const val TAG = "Settings"

@Composable
fun SettingsBlockOne(navController: NavController){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.small,
        elevation = 0.dp,
        backgroundColor = PotPourri
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp)
        ) {
            val (
                sleeping_time,
                divider1,
                wake_up_time,
                divider2,
                alarm,
                divider3,
                time,
                divider4,
                countdown,
                divider5,
                sound
            ) = createRefs()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(sleeping_time){
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                NormalText(
                    text = "Sleeping Time",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
                LightText(
                    text = "23:30",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider1){
                        top.linkTo(sleeping_time.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                Divider(color = Grey, thickness = 0.3.dp)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(wake_up_time){
                        top.linkTo(divider1.top, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                NormalText(
                    text = "Wake-up Time",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
                LightText(
                    text = "08:45",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider2){
                        top.linkTo(wake_up_time.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                Divider(color = Grey, thickness = 0.3.dp)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(alarm){
                        top.linkTo(divider2.top, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                NormalText(
                    text = "Alarm",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
                LightText(
                    text = "ON",
                    color = Black,
                    fontSize = 15,
                    xOffset = -8,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider3){
                        top.linkTo(alarm.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.wrapContentWidth()
            ){
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 70f, y = 0f),
                        end = Offset(x = canvasWidth, y = 0f),
                        color = Grey,
                        strokeWidth = 1F
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(time){
                        top.linkTo(divider3.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.fillMaxWidth()
            ){
                NormalText(
                    text = "Time",
                    color = Black,
                    fontSize = 15,
                    xOffset = 26,
                    yOffset = 0
                )
                LightText(
                    text = "08:40",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider4){
                        top.linkTo(time.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 70f, y = 0f),
                        end = Offset(x = canvasWidth, y = 0f),
                        color = Grey,
                        strokeWidth = 1F
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(countdown){
                        top.linkTo(divider4.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.EightHourCountdown.screen_route)
                    }
            ){
                NormalText(
                    text = "8-hour Countdown",
                    color = Black,
                    fontSize = 15,
                    xOffset = 26,
                    yOffset = 0
                )
                Card(
                    modifier = Modifier
                        .size(45.dp, 22.dp),
                    shape = MaterialTheme.shapes.small,
                    backgroundColor = Color.Black,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
            Column(
                modifier = Modifier
                    .constrainAs(divider5){
                        top.linkTo(countdown.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ){
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 70f, y = 0f),
                        end = Offset(x = canvasWidth, y = 0f),
                        color = Grey,
                        strokeWidth = 1F
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(sound){
                        top.linkTo(divider5.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.fillMaxWidth()
            ){
                NormalText(
                    text = "Sound",
                    color = Black,
                    fontSize = 15,
                    xOffset = 26,
                    yOffset = 0
                )
                LightText(
                    text = "Gummy",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Composable
fun SettingsBlockTwo(){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.small,
        elevation = 0.dp,
        backgroundColor = PotPourri
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp)
        ) {
            val (
                detailed_stats,
                divider1,
                weekly_report
            ) = createRefs()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(detailed_stats){
                        top.linkTo(parent.top, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                NormalText(
                    text = "Detailed Stats",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
                Card(
                    modifier = Modifier
                        .size(45.dp, 22.dp),
                    shape = MaterialTheme.shapes.small,
                    backgroundColor = Color.Black,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
            Column(
                modifier = Modifier
                    .constrainAs(divider1){
                        top.linkTo(detailed_stats.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.wrapContentWidth()
            ){
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 70f, y = 0f),
                        end = Offset(x = canvasWidth, y = 0f),
                        color = Grey,
                        strokeWidth = 1F
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(weekly_report){
                        top.linkTo(divider1.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.fillMaxWidth()
            ){
                NormalText(
                    text = "Weekly Report",
                    color = Black,
                    fontSize = 15,
                    xOffset = 26,
                    yOffset = 0
                )
                Card(
                    modifier = Modifier
                        .size(45.dp, 22.dp),
                    shape = MaterialTheme.shapes.small,
                    backgroundColor = Color.Black,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
    }
}

@Composable
fun SettingsBlockThree(){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.small,
        elevation = 0.dp,
        backgroundColor = PotPourri
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp)
        ) {
            val (
                slumber_party,
                divider1,
                members,
                divider2,
                add_member,
                divider3,
                endSpace
            ) = createRefs()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(slumber_party){
                        top.linkTo(parent.top, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                NormalText(
                    text = "Slumber Party",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
                LightText(
                    text = "My sleeping beauties",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider1){
                        top.linkTo(slumber_party.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.wrapContentWidth()
            ){
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 70f, y = 0f),
                        end = Offset(x = canvasWidth, y = 0f),
                        color = Grey,
                        strokeWidth = 1F
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(members){
                        top.linkTo(divider1.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.fillMaxWidth()
            ){
                NormalText(
                    text = "Members",
                    color = Black,
                    fontSize = 15,
                    xOffset = 26,
                    yOffset = 0
                )
                LightText(
                    text = "2 members",
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider2){
                        top.linkTo(members.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.wrapContentWidth()
            ){
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 70f, y = 0f),
                        end = Offset(x = canvasWidth, y = 0f),
                        color = Grey,
                        strokeWidth = 1F
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .constrainAs(add_member){
                        top.linkTo(divider2.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }.fillMaxWidth()
            ){
                NormalText(
                    text = "Add member",
                    color = Black,
                    fontSize = 15,
                    xOffset = 26,
                    yOffset = 0
                )
                Card(
                    modifier = Modifier
                        .size(45.dp, 22.dp),
                    shape = MaterialTheme.shapes.small,
                    backgroundColor = Color.Black,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
            Column(
                modifier = Modifier
                    .constrainAs(divider3){
                        top.linkTo(add_member.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .fillMaxWidth()
            ){
                Divider(color = Grey, thickness = 0.3.dp)
            }
            Column(
                modifier = Modifier
                    .constrainAs(endSpace) {
                        top.linkTo(divider3.bottom, margin = 0.dp)
                    }
            ){
                Spacer(modifier = Modifier.height(20.dp))
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
fun PreviewUI() {
    EUNOIATheme {
        SettingsBlockThree()
    }
}