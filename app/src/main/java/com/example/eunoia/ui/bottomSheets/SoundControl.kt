package com.example.eunoia.ui.bottomSheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.dashboard.sound.Controls
import com.example.eunoia.models.SoundObject
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.Mischka
import com.example.eunoia.ui.theme.PeriwinkleGray
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun bottomSheetSoundControl(
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
): Boolean{
    var showing = false
    if(globalViewModel.currentSoundPlaying != null &&
        globalViewModel.currentSoundPlayingPreset != null &&
        globalViewModel.currentSoundPlayingContext != null) {
        showing = true
        Card(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(115.dp)
                .fillMaxWidth()
                .clickable {
                    if(globalViewModel_!!.navController != null){
                        closeBottomSheet(scope, state)
                        globalViewModel_!!.navController!!.navigate(
                            "${Screen.SoundScreen.screen_route}/sound=${SoundObject.Sound.from(globalViewModel.currentSoundPlaying!!)}"
                        )
                    }
                },
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PeriwinkleGray.copy(alpha = 0.4F),
                                Color(0xFFCBCBE8).copy(alpha = 0.4F),
                                Mischka.copy(alpha = 0.4F)
                            ),
                            center = Offset.Unspecified,
                            radius = Float.POSITIVE_INFINITY,
                            tileMode = TileMode.Clamp
                        ),
                    ),
            ) {
                val (
                    title,
                    mode,
                    controls
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        }
                ) {
                    NormalText(
                        text = globalViewModel.currentSoundPlaying!!.displayName,
                        color = Black,
                        fontSize = 12,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(mode) {
                            top.linkTo(title.bottom, margin = 8.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        }
                ) {
                    LightText(
                        text = "Mode: play on the background always",
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(controls) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 32.dp)
                            start.linkTo(parent.start, margin = 32.dp)
                        }
                ) {
                    Controls(
                        globalViewModel.currentSoundPlaying!!,
                        globalViewModel.currentSoundPlayingPreset!!,
                        globalViewModel.currentSoundPlayingContext!!,
                        false,
                        scope,
                        state
                    )
                }
            }
        }
    }
    return showing
}