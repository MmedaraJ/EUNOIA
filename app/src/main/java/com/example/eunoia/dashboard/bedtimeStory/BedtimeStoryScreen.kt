package com.example.eunoia.dashboard.bedtimeStory

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoData
import com.example.eunoia.R
import com.example.eunoia.dashboard.sound.gradientBackground
import com.example.eunoia.dashboard.sound.navigateBack
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.formatMilliSecond
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BedtimeStoryScreen(
    navController: NavController,
    bedtimeStoryInfoData: BedtimeStoryInfoData,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            playBox,
            controls,
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .constrainAs(playBox) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            CircularSlider(
                thumbColor = Snuff,
                progressColor = Black,
                backgroundColor = PeriwinkleGray.copy(alpha = 0.5F),
                modifier = Modifier.size(320.dp),
            )
            Card(
                elevation = 8.dp,
                modifier = Modifier.clip(CircleShape)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(223.51.dp)
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
                    val playTimeString = formatMilliSecond(bedtimeStoryInfoData.fullPlayTime.toLong())
                    LightText(
                        text = playTimeString,
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
        Column (
            modifier = Modifier
                .constrainAs(controls) {
                    top.linkTo(playBox.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .fillMaxWidth()
        ){
            PurpleBackgroundControls(
                titleString = bedtimeStoryInfoData.displayName,
                secondaryTitle = "By: J.K Rowling"
            ) {

            }
        }
        Column (
            modifier = Modifier
                .constrainAs(description) {
                    top.linkTo(controls.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .fillMaxWidth()
        ){
            PurpleBackgroundInfo(
                "Description",
                bedtimeStoryInfoData.description
            ){}
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
    /*EUNOIATheme {
        BedtimeStoryScreen(
            rememberNavController(),
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }*/
}