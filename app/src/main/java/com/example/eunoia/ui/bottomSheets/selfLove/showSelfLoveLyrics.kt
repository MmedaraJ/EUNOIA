package com.example.eunoia.ui.bottomSheets.selfLove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.ui.components.AlignedLightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.selfLoveViewModel
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.OldLace
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowSelfLoveLyricsBottomSheet(
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .background(OldLace)
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        val (
            title,
            lyrics,
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            NormalText(
                text = selfLoveViewModel!!.currentSelfLoveLyricsToBeShown!!.displayName,
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(lyrics) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            for(lyric in selfLoveViewModel!!.currentSelfLoveLyricsToBeShown!!.lyrics){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ){
                    AlignedLightText(
                        text = lyric,
                        color = Black,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}