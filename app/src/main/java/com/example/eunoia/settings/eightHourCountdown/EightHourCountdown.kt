package com.example.eunoia.settings.eightHourCountdown

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.navigateBack
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.dashboard.bedtimeStory.CircularSlider
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.PurpleBackgroundStart
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EightHourCountdownUI(
    navController: NavController,
    context: Context,
    scope: CoroutineScope,
    state: ModalBottomSheetState,
    generalMediaPlayerService: GeneralMediaPlayerService
) {
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            startBlock,
            playBox,
            description
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            BackArrowHeader(
                {
                    navigateBack(navController)
                },
                {
                    globalViewModel_!!.bottomSheetOpenFor = "controls"
                    openBottomSheet(scope, state)
                },
                {
                }
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(startBlock) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            PurpleBackgroundStart(
                "8-hour Countdown",
                "Ringtone: By the Seaside"
            ){}
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .constrainAs(playBox) {
                    top.linkTo(startBlock.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            CircularSlider(
                thumbColor = Snuff,
                progressColor = Black,
                backgroundColor = PeriwinkleGray.copy(alpha = 0.5F),
                modifier = Modifier.size(389.73.dp),
                generalMediaPlayerService = generalMediaPlayerService
            ){}
            Card(
                elevation = 8.dp,
                modifier = Modifier.clip(CircleShape)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(270.dp)
                        .clip(CircleShape)
                        .gradientBackground(
                            listOf(
                                SoftPeach,
                                Snuff,
                            ),
                            angle = 180f
                        )
                        .border(1.dp, Black, CircleShape)
                        .clickable { }
                ) {
                    LightText(
                        text = "08:00:00",
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

@OptIn(ExperimentalMaterialApi::class)
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
fun Preview() {
    EUNOIATheme {
        EightHourCountdownUI(
            rememberNavController(),
            LocalContext.current,
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
            GeneralMediaPlayerService()
        )
    }
}