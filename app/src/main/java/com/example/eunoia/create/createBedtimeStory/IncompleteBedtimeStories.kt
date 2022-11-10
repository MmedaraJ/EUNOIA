package com.example.eunoia.create.createBedtimeStory

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.backend.BedtimeStoryBackend
import com.example.eunoia.lifecycle.CustomLifecycleEventListener
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

var userBedtimeStories = mutableListOf<BedtimeStoryInfoData?>()
private var TAG = "Incomplete Bedtime Story"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncompleteBedtimeStoriesUI(
    navController: NavController,
    globalViewModel: GlobalViewModel,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    clearPagesList()
    clearBedtimeStoryChaptersList()
    clearPageRecordingsList()
    var numberOfBedtimeStories by rememberSaveable { mutableStateOf(0) }
    BedtimeStoryBackend.queryIncompleteBedtimeStoryBasedOnUser(globalViewModel_!!.currentUser!!) {
        for (i in userBedtimeStories.size until it.size) {
            userBedtimeStories.add(it[i]!!)
        }
        numberOfBedtimeStories = userBedtimeStories.size
    }
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            stories,
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
                    navController.popBackStack()
                },
                {
                    globalViewModel_!!.bottomSheetOpenFor = "controls"
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
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = "Your incomplete bedtime stories",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(stories) {
                    top.linkTo(title.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            if(numberOfBedtimeStories > 0){
                for(story in userBedtimeStories){
                    Column(
                        modifier = Modifier
                            .clickable {
                                navigateToRecordBedtimeStory(
                                    navController,
                                    story!!
                                )
                            }
                    ) {
                        NormalText(
                            text = story!!.displayName,
                            color = Black,
                            fontSize = 16,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

fun resetIncompleteBedtimeStoriesVariables(){
    userBedtimeStories.clear()
}