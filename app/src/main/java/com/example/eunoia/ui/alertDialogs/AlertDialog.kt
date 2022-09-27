package com.example.eunoia.ui.alertDialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eunoia.create.createBedtimeStory.openBedtimeStoryNameTakenDialogBox
import com.example.eunoia.create.createBedtimeStory.openTooManyIncompleteBedtimeStoryDialogBox
import com.example.eunoia.dashboard.sound.openUserAlreadyHasSoundDialogBox
import com.example.eunoia.ui.bottomSheets.sound.openRoutineAlreadyHasSoundDialogBox
import com.example.eunoia.ui.bottomSheets.sound.openSavedElementDialogBox
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.*

private var alertMessage = ""
private var openDialogBox = mutableStateOf(false)

@Composable
fun AlertDialogBox(text: String){
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                openDialogBox.value = false
                openSavedElementDialogBox = false
                openUserAlreadyHasSoundDialogBox = false
                openRoutineAlreadyHasSoundDialogBox = false
                openBedtimeStoryNameTakenDialogBox = false
                openTooManyIncompleteBedtimeStoryDialogBox = false
            },
            text = {
                Column{
                    NormalText(
                        text = text,
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            },
            backgroundColor = BeautyBush,
            buttons = {},
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(0.dp)
        )
    }
}