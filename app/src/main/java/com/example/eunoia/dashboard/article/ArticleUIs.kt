package com.example.eunoia.dashboard.article

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.R
import com.example.eunoia.ui.components.AnImage
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.EUNOIATheme

@Composable
fun CompleteTitle(){
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        val (
            title_text,
            line,
            title_icon_left,
            title_icon_right
        ) = createRefs()
        Column(
            modifier = Modifier.constrainAs(line){
                top.linkTo(title_text.bottom, margin = (-16).dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        ){
            AnImage(
                R.drawable.pink_line,
                "pink line",
                300.0,
                15.0,
                0,
                0,
                LocalContext.current
            ) {}
        }
        Column(
            modifier = Modifier.constrainAs(title_text){
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        ){
            NormalText(
                "Why do we sleep?",
                color = MaterialTheme.colors.primary,
                fontSize = 25,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier.constrainAs(title_icon_left){
                top.linkTo(title_text.top, margin = 0.dp)
                bottom.linkTo(title_text.bottom, margin = 0.dp)
                end.linkTo(title_text.start, margin = 2.dp)
                start.linkTo(parent.start, margin = 0.dp)
            }
        ) {
            AnImage(
                R.drawable.goodies_heart_eye,
                "goodies heart eye icon",
                70.0,
                45.0,
                0,
                0,
                LocalContext.current
            ) {}
        }
        Column(
            modifier = Modifier.constrainAs(title_icon_right){
                top.linkTo(title_text.top, margin = 0.dp)
                bottom.linkTo(title_text.bottom, margin = 0.dp)
                start.linkTo(title_text.end, margin = 2.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        ) {
            AnImage(
                R.drawable.goodies_heart_eye,
                "goodies heart eye icon",
                70.0,
                50.0,
                0,
                0,
                LocalContext.current
            ) {}
        }
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
fun PreviewUI() {
    EUNOIATheme {
        CompleteTitle()
    }
}