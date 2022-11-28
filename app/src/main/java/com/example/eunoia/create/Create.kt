package com.example.eunoia.create

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.create.createBedtimeStory.resetAllBedtimeStoryCreationObjects
import com.example.eunoia.dashboard.bedtimeStory.*
import com.example.eunoia.dashboard.home.*
import com.example.eunoia.dashboard.prayer.*
import com.example.eunoia.dashboard.selfLove.*
import com.example.eunoia.dashboard.sound.updatePreviousUserSoundRelationship
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*
import com.example.eunoia.utils.getCurrentlyPlayingTime
import com.example.eunoia.viewModels.*
import kotlinx.coroutines.CoroutineScope

var createSoundViewModel: CreateSoundViewModel? = null

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    globalViewModel!!.navController = navController
    createSoundViewModel =  viewModel()
    openSavedElementDialogBox = false
    resetAllBedtimeStoryCreationObjects()

    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            intro,
            elements,
            endSpace
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            ProfilePictureHeader(
                {},
                {
                    com.example.eunoia.ui.navigation.globalViewModel!!.bottomSheetOpenFor = "controls"
                    openBottomSheet(scope, state)

                },
                {
                    //navController.navigate(Screen.Settings.screen_route)
                }
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            NormalText(
                text = "Create your own element",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(intro) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            AlignedLightText(
                text = "Add to a routine or share with others if you want. " +
                        "The more people use your element, the more money you earn.",
                color = Black,
                fontSize = 13,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(elements) {
                    top.linkTo(intro.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .wrapContentHeight()
        ) {
            Elements(navController)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

fun resetEverything(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    completed: () -> Unit
){
    updatePreviousUserSoundRelationship {
        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
                    updatePreviousUserRoutineRelationship {
                        soundMediaPlayerService.onDestroy()
                        generalMediaPlayerService.onDestroy()
                        com.example.eunoia.dashboard.sound.resetAll(
                            context,
                            soundMediaPlayerService
                        )
                        resetSelfLoveGlobalProperties()
                        resetPrayerGlobalProperties()
                        resetBedtimeStoryGlobalProperties()
                        resetRoutineGlobalProperties()
                        completed()
                    }
                }
            }
        }
    }
}

fun resetEverythingExceptRoutine(
    soundMediaPlayerService: SoundMediaPlayerService,
    generalMediaPlayerService: GeneralMediaPlayerService,
    context: Context,
    completed: () -> Unit
){
    val continuePlayingTime = getCurrentlyPlayingTime(generalMediaPlayerService)
    updatePreviousUserSoundRelationship {
        updatePreviousUserBedtimeStoryRelationship(generalMediaPlayerService) {
            updatePreviousUserPrayerRelationship {
                updatePreviousUserSelfLoveRelationship(generalMediaPlayerService) {
                    soundMediaPlayerService.onDestroy()
                    generalMediaPlayerService.onDestroy()
                    com.example.eunoia.dashboard.sound.resetAll(context, soundMediaPlayerService)
                    resetSelfLoveGlobalProperties()
                    resetPrayerGlobalProperties()
                    resetBedtimeStoryGlobalProperties()
                    completed()
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
fun CreatePreview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        CreateUI(
            rememberNavController(),
            rememberCoroutineScope(),
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        )
    }
}
