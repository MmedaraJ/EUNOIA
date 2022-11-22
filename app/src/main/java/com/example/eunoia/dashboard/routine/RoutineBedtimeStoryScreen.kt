package com.example.eunoia.dashboard.routine

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.UserRoutineRelationship
import com.amplifyframework.datastore.generated.model.UserRoutineRelationshipBedtimeStoryInfo
import com.example.eunoia.dashboard.sound.navigateBack
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.theme.Black
import kotlinx.coroutines.CoroutineScope

var routineBedtimeStoryList by mutableStateOf<MutableList<UserRoutineRelationshipBedtimeStoryInfo?>?>(null)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoutineBedtimeStoryScreen(
    navController: NavController,
    context: Context,
    userRoutineRelationship: UserRoutineRelationship,
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
            routineName,
            categoryTitle,
            elements
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
                    globalViewModel!!.bottomSheetOpenFor = "controls"
                    openBottomSheet(scope, state)
                },
                {
                }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(routineName) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AlignedNormalText(
                text = "[${userRoutineRelationship.userRoutineRelationshipRoutine.displayName}]",
                color = Black,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(categoryTitle) {
                    top.linkTo(routineName.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if(routineBedtimeStoryList != null) {
                val BedtimeStoryText = if(routineBedtimeStoryList!!.size > 1) "BedtimeStorys" else "BedtimeStory"
                AlignedNormalText(
                    text = "${routineBedtimeStoryList!!.size} $BedtimeStoryText",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(elements) {
                    top.linkTo(categoryTitle.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
            //.wrapContentHeight()
        ) {
            RoutineBedtimeStoryElements(navController, userRoutineRelationship)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}