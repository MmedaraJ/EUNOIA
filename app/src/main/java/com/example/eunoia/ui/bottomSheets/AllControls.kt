package com.example.eunoia.ui.bottomSheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.eunoia.dashboard.sound.soundScreenBorderControlColors
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.bedtimeStory.bottomSheetBedtimeStoryControlPanel
import com.example.eunoia.ui.bottomSheets.prayer.bottomSheetPrayerControlPanel
import com.example.eunoia.ui.bottomSheets.selfLove.bottomSheetSelfLoveControlPanel
import com.example.eunoia.ui.bottomSheets.sound.bottomSheetSoundControlPanel
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
fun openBottomSheet(scope: CoroutineScope, state: ModalBottomSheetState) {
    scope.launch{
        state.show()
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun closeBottomSheet(scope: CoroutineScope, state: ModalBottomSheetState) {
    soundViewModel!!.soundScreenBorderControlColors[7].value = Bizarre
    bedtimeStoryViewModel!!.bedtimeStoryScreenBorderControlColors[4].value = Bizarre
    selfLoveViewModel!!.selfLoveScreenBorderControlColors[4].value = Bizarre
    prayerViewModel!!.prayerScreenBorderControlColors[4].value = Bizarre
    scope.launch{
        state.hide()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetAllControls(
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService,
    soundMediaPlayerService: SoundMediaPlayerService,
){
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        val soundPlaying = bottomSheetSoundControlPanel(
            scope,
            state,
            generalMediaPlayerService,
            soundMediaPlayerService
        )

        val bedtimeStoryPlaying = bottomSheetBedtimeStoryControlPanel(
            scope,
            state,
            generalMediaPlayerService,
            soundMediaPlayerService
        )

        val selfLovePlaying = bottomSheetSelfLoveControlPanel(
            scope,
            state,
            generalMediaPlayerService
        )

        val prayerPlaying = bottomSheetPrayerControlPanel(
            scope,
            state,
            generalMediaPlayerService
        )

        if(
            !soundPlaying &&
            !bedtimeStoryPlaying &&
            !selfLovePlaying &&
            !prayerPlaying
        ){
            Card(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(115.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                elevation = 8.dp
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    PeriwinkleGray.copy(alpha = 0.3F),
                                    Color(0xFFCBCBE8).copy(alpha = 0.4F),
                                    Mischka.copy(alpha = 0.6F),
                                ),
                                center = Offset.Unspecified,
                                radius = Float.POSITIVE_INFINITY,
                                tileMode = TileMode.Clamp
                            ),
                        ),
                ) {
                    val (
                        nothing_playing,
                    ) = createRefs()
                    Column(
                        modifier = Modifier
                            .constrainAs(nothing_playing) {
                                top.linkTo(parent.top, margin = 0.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                            }
                    ) {
                        LightText(
                            text = "Nothing is playing at the moment",
                            color = Black,
                            fontSize = 10,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        }
    }
}