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
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.Mischka
import com.example.eunoia.ui.theme.PeriwinkleGray
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
fun openBottomSheet(scope: CoroutineScope, state: ModalBottomSheetState) {
    scope.launch{
        state.show()
    }
}

@Composable
fun BottomSheetAllControls(globalViewModel: GlobalViewModel){
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        val soundPlaying = bottomSheetSoundControl(globalViewModel)
        val bedtimeStoryPlaying = bottomSheetSoundControl(globalViewModel)
        val selfLovePlaying = bottomSheetSoundControl(globalViewModel)

        if(!(soundPlaying && bedtimeStoryPlaying && selfLovePlaying)){
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