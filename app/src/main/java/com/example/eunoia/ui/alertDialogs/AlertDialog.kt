package com.example.eunoia.ui.alertDialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eunoia.dashboard.sound.openDialogBoxHere
import com.example.eunoia.ui.bottomSheets.openDialogBox
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.*

@Composable
fun AlertDialogBox(text: String){
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                openDialogBox = false
                openDialogBoxHere = false
            },
            text = {
                Column {
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
            modifier = Modifier
                .wrapContentHeight()
                .padding(4.dp)
        )
    }
}