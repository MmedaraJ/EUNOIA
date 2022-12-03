package com.example.eunoia.create.createPrayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.PrayerData
import com.example.eunoia.backend.PrayerBackend
import com.example.eunoia.ui.bottomSheets.openBottomSheet
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.theme.Black
import kotlinx.coroutines.CoroutineScope

var userPrayers = mutableListOf<PrayerData?>()
private var TAG = "Incomplete Prayer"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncompletePrayersUI(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    userPrayers.clear()
    var numberOfPrayers by rememberSaveable { mutableStateOf(0) }
    PrayerBackend.queryIncompletePrayerBasedOnUser(globalViewModel!!.currentUser!!) {
        for (i in userPrayers.size until it.size) {
            userPrayers.add(it[i]!!)
        }
        numberOfPrayers = userPrayers.size
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
                    globalViewModel!!.bottomSheetOpenFor = "controls"
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
                text = "Your incomplete prayers",
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
            if(numberOfPrayers > 0){
                for(prayer in userPrayers){
                    Column(
                        modifier = Modifier
                            .clickable {
                                navigateToRecordPrayer(
                                    navController,
                                    prayer!!
                                )
                            }
                    ) {
                        NormalText(
                            text = prayer!!.displayName,
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

fun resetIncompletePrayersVariables(){
    userPrayers.clear()
}