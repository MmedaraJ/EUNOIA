package com.example.eunoia.ui.alertDialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eunoia.R
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.navigation.*
import com.example.eunoia.ui.theme.BeautyBush
import com.example.eunoia.ui.theme.Black
import com.example.eunoia.ui.theme.EUNOIATheme

@Composable
fun ConfirmAlertDialog(
    text: String,
    yes: () -> Unit,
    no: () -> Unit
){
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                openConfirmDeletePageDialogBox = false
                openConfirmDeleteChapterDialogBox = false
                openConfirmDeleteSelfLoveDialogBox = false
                openRoutineIsCurrentlyPlayingDialogBox = false
                openConfirmDeleteBedtimeStoryDialogBox = false
            },
            text = {
                Column{
                    AlignedNormalText(
                        text = text,
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            },
            backgroundColor = BeautyBush,
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Button(
                        onClick = {
                            openDialog.value = false
                            openConfirmDeletePageDialogBox = false
                            openConfirmDeleteChapterDialogBox = false
                            openConfirmDeleteSelfLoveDialogBox = false
                            openRoutineIsCurrentlyPlayingDialogBox = false
                            openConfirmDeleteBedtimeStoryDialogBox = false
                            yes()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = BeautyBush),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp)
                    ){
                        NormalText(
                            text = "Yes",
                            color = Black,
                            fontSize = 13,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                    Button(
                        onClick = {
                            openDialog.value = false
                            openConfirmDeletePageDialogBox = false
                            openConfirmDeleteChapterDialogBox = false
                            openConfirmDeleteSelfLoveDialogBox = false
                            openRoutineIsCurrentlyPlayingDialogBox = false
                            openConfirmDeleteBedtimeStoryDialogBox = false
                            no()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = BeautyBush),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.ten)),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp)
                    ){
                        NormalText(
                            text = "No",
                            color = Black,
                            fontSize = 13,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(0.dp)
        )
    }
}

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
        ConfirmAlertDialog(
            "Are you sure you want to stop your routine?",
            {},
            {}
        )
    }
}