package com.example.eunoia.ui.alertDialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.*

@Composable
fun AlertDialogBox(text: String){
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                commentCreatedDialog = false
                openSavedElementDialogBox = false
                openSoundNameTakenDialogBox = false
                openPrayerNameTakenDialogBox = false
                openPresetAlreadyExistsDialog = false
                openSelfLoveNameTakenDialogBox = false
                openUserAlreadyHasSoundDialogBox = false
                openConfirmDeletePrayerDialogBox = false
                openUserAlreadyHasPrayerDialogBox = false
                openPresetNameIsAlreadyTakenDialog = false
                openBedtimeStoryNameTakenDialogBox = false
                openUserAlreadyHasSelfLoveDialogBox = false
                openRoutineAlreadyHasSoundDialogBox = false
                openRoutineNameIsAlreadyTakenDialog = false
                openRoutineAlreadyHasPresetDialogBox = false
                openRoutineAlreadyHasPrayerDialogBox = false
                openTooManyIncompletePrayerDialogBox = false
                openRoutineAlreadyHasSelfLoveDialogBox = false
                openTooManyIncompleteSelfLoveDialogBox = false
                openUserAlreadyHasBedtimeStoryDialogBox = false
                openTooManyIncompleteBedtimeStoryDialogBox = false
                openRoutineAlreadyHasBedtimeStoryDialogBox = false
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