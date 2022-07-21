package com.example.eunoia.ui.bottomSheets

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.R
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.dashboard.sound.PresetsUI
import com.example.eunoia.dashboard.sound.SimpleFlowRow
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.alertDialogs.AlertDialogBox
import com.example.eunoia.ui.components.AnImage
import com.example.eunoia.ui.components.MorgeNormalText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope

var openDialogBox by mutableStateOf(false)
private const val TAG = "AddToSoundListAndRoutineBottomSheet"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToSoundListAndRoutineBottomSheet(
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    if(openDialogBox){
        AlertDialogBox("Saved!")
    }
    Card(
        modifier = Modifier
            .height(92.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(OldLace),
        ) {
            val (
                soundList,
                divider,
                routine,
                arrow
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(soundList) {
                        top.linkTo(parent.top, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(divider.top, margin = 0.dp)
                    }
                    .clickable {
                        if (globalViewModel_!!.currentSoundToBeAdded != null) {
                            if(globalViewModel_!!.currentUser != null){
                                globalViewModel_!!.currentUser!!.sounds.add(globalViewModel_!!.currentSoundToBeAdded)
                                Log.i(TAG, "Users sound list 1.. ${globalViewModel_!!.currentUser!!.sounds}")
                                UserBackend.updateUser(globalViewModel_!!.currentUser!!) {
                                    Log.i(TAG, "Users sound list ${it.sounds}")
                                    closeBottomSheet(scope, state)
                                    openDialogBox = true
                                }
                            }
                        }
                    }
            ) {
                NormalText(
                    text = "Add to your sound list",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(parent.top, margin = 0.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxWidth()){
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawLine(
                        start = Offset(x = 48f, y = 0f),
                        end = Offset(x = canvasWidth - 48f, y = 0f),
                        color = Black,
                        strokeWidth = 0.3F
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(routine) {
                        top.linkTo(divider.bottom, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
                    .clickable {

                    }
            ) {
                NormalText(
                    text = "Add to a routine",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(arrow) {
                        top.linkTo(routine.top, margin = 0.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        bottom.linkTo(routine.bottom, margin = 0.dp)
                    }
            ) {
                AnImage(
                    R.drawable.little_right_arrow,
                    "icon",
                    6.dp,
                    4.dp,
                    0,
                    0
                ) {

                }
            }
        }
    }
}

@Composable
fun SelectRoutine() {
    Column {
        NormalText(
            text = "Add to",
            color = Black,
            fontSize = 13,
            xOffset = 0,
            yOffset = 0
        )
    }
    SimpleFlowRow(
    verticalGap = 8.dp,
    horizontalGap = 8.dp,
    alignment = Alignment.CenterHorizontally,
    ) {
        val icons = listOf(
            R.drawable.baking_icon,
            R.drawable.beach_waves_icon,
            R.drawable.coffee_house_icon,
            R.drawable.keyboard_icon,
            R.drawable.library_icon,
            R.drawable.next_door_icon,
            R.drawable.pouring_rain_icon,
            R.drawable.train_track_icon
        )
        val colors = listOf(
            GoldSand,
            JungleMist,
            PeriwinkleGray,
            BeautyBush,
            WaikawaGray,
            Madang,
            SeaPink,
            Twine,
            DoveGray,
            Neptune,
        )
        icons.forEachIndexed { index, icon ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .clickable {

                    }
            ) {
                Box{
                    Card(
                        modifier = Modifier
                            .size(71.dp, 71.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        backgroundColor = White,
                        elevation = 8.dp
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = "routine icon",
                            modifier = Modifier
                                .size(width = 25.64.dp, height = 25.64.dp)
                                .padding(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                NormalText(
                    text = "routine name",
                    color = MaterialTheme.colors.primary,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}