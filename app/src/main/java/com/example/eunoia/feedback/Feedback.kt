package com.example.eunoia.feedback

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*

@Composable
fun FeedbackUI(navController: NavController, context: Context) {
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            introTitle,
            rating_bar,
            all_text_ratings,
            dropdown,
            long_text,
            contact,
            endSpace
        ) = createRefs()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            ProfilePictureHeader(
                {},
                { navController.navigate(Screen.Settings.screen_route) })
        }
        Column(
            modifier = Modifier
                .constrainAs(introTitle) {
                    top.linkTo(header.bottom, margin = 20.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            NormalText(
                text = "Tell us how we are doing",
                color = MaterialTheme.colors.primary,
                20,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(rating_bar) {
                    top.linkTo(introTitle.bottom, margin = 30.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            RatingBar(rating = 0)
        }
        Column(
            modifier = Modifier
                .constrainAs(all_text_ratings) {
                    top.linkTo(rating_bar.bottom, margin = 30.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            AllTextRatings()
        }
        Column(
            modifier = Modifier
                .constrainAs(dropdown) {
                    top.linkTo(all_text_ratings.bottom, margin = 30.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            DropdownMenu()
        }
        Column(
            modifier = Modifier
                .constrainAs(long_text) {
                    top.linkTo(dropdown.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            bigOutlinedTextInput(
                100,
                "We are happy to hear from you.\nDo you have any ideas or suggestions for us?",
                RatingDropdownBackground,
                RatingDropdownText,
                RatingDropdownBackground,
                RatingDropdownText,
                RatingDropdownText,
                15
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(contact) {
                    top.linkTo(long_text.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            ContactText()
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(contact.bottom, margin = 30.dp)
                }
        ){
            Spacer(modifier = Modifier.height(32.dp))
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
fun Preview() {
    EUNOIATheme {
        FeedbackUI(rememberNavController(), LocalContext.current)
    }
}