package com.example.eunoia.feedback

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.R
import com.example.eunoia.ui.components.AlignedLightText
import com.example.eunoia.ui.components.AnImage
import com.example.eunoia.ui.components.LightText
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.*

@Composable
fun RatingBar(modifier: Modifier = Modifier, rating: Int){
    var ratingState by remember{ mutableStateOf(rating) }
    //var selected by remember{ mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        for(i in 1..5){
            Icon(
                painter = painterResource(R.drawable.rating_star),
                contentDescription = "rating star",
                modifier = modifier
                    .size(36.dp)
                    .clickable {
                        ratingState = i
                        Log.i("Rating", ratingState.toString())
                    },
                tint = if (i <= ratingState) ratingStarSelected else ratingStarUnselected
            )
        }
    }
}

@Composable
fun TextFeedback(text: String, lambda: () -> Unit){
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 8.dp)
        .wrapContentHeight()
        .clickable {
            clicked = !clicked
            lambda()
        }
        .wrapContentWidth()

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }

    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = textRatingBackground,
        elevation = 2.dp,
    ){
        ConstraintLayout(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 12.dp,
                end = 12.dp
            )
        ) {
            val (text_rating) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(text_rating) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = text,
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
        }
    }
}

@Composable
fun AllTextRatings(){
    ConstraintLayout(
    ) {
        val (
            rating1,
            rating2,
            rating3,
            rating4,
            rating5,
            rating6,
        ) = createRefs()
        Column(
            modifier = Modifier.constrainAs(rating1){
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
            }
        ){
            TextFeedback("Easy to use"){}
        }
        Column(
            modifier = Modifier.constrainAs(rating2){
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(rating1.end, margin = 8.dp)
                end.linkTo(rating3.start, margin = 8.dp)
            }
        ){
            TextFeedback("Good price"){}
        }
        Column(
            modifier = Modifier.constrainAs(rating3){
                top.linkTo(parent.top, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        ){
            TextFeedback("Useful"){}
        }
        Column(
            modifier = Modifier.constrainAs(rating4){
                top.linkTo(rating1.bottom, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
            }
        ){
            TextFeedback("Responsive"){}
        }
        Column(
            modifier = Modifier.constrainAs(rating5){
                top.linkTo(rating2.bottom, margin = 0.dp)
                start.linkTo(rating4.end, margin = 0.dp)
                end.linkTo(rating6.start, margin = 0.dp)
            }
        ){
            TextFeedback("Beauty"){}
        }
        Column(
            modifier = Modifier
                .constrainAs(rating6){
                    top.linkTo(rating3.bottom, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            TextFeedback("Effective"){}
        }
    }
}

@Composable
fun DropdownMenu(){
    var expanded by remember { mutableStateOf(false)}
    val topics = listOf(
        "Topic1",
        "Topic2",
        "Topic3",
        "Topic4",
        "Topic5"
    )
    var selectedIndex by remember { mutableStateOf(-1) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (
            select,
            dropdown,
        ) = createRefs()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { expanded = true }
                )
                .constrainAs(select) {
                    top.linkTo(parent.top, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
            shape = MaterialTheme.shapes.small,
        ){
            Column(
                modifier = Modifier
                    .background(RatingDropdownBackground)
            ){
                if(selectedIndex > -1){
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 12.dp,
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        NormalText(
                            text = topics[selectedIndex],
                            color = RatingDropdownText,
                            fontSize = 15,
                            xOffset = 0,
                            yOffset = 0
                        )
                        AnImage(
                            R.drawable.dropdown_icon,
                            "dropdown icon",
                            14.dp,
                            8.dp,
                            0,
                            0
                        ) {expanded = true }
                    }
                }else{
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 12.dp,
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        LightText(
                            "Select a topic",
                            color = RatingDropdownText,
                            fontSize = 15,
                            xOffset = 0,
                            yOffset = 0
                        )
                        AnImage(
                            R.drawable.dropdown_icon,
                            "dropdown icon",
                            14.dp,
                            8.dp,
                            0,
                            0
                        ) {expanded = true }
                    }
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(RatingDropdownBackground)
                .constrainAs(dropdown) {
                    top.linkTo(select.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
                .wrapContentHeight(),
        ) {
            topics.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    }
                ) {
                    NormalText(
                        text = s,
                        color = RatingDropdownText,
                        fontSize = 15,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
    }
}

@Composable
fun Email(){
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        val (
            email,
            line,
        ) = createRefs()
        Column(
            modifier = Modifier.constrainAs(line){
                top.linkTo(email.bottom, margin = -10.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        ){
            AnImage(
                R.drawable.pink_line,
                "email line",
                150.dp,
                9.dp,
                0,
                0
            ) {}
        }
        Column(
            modifier = Modifier.constrainAs(email){
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        ){
            LightText(
                "eunoia@gmail.com",
                color = MaterialTheme.colors.primary,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
        }
    }
}

@Composable
fun ContactText(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row {
            LightText(
                "Feel free to contact us at",
                color = MaterialTheme.colors.primary,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
            Spacer(modifier = Modifier.width(2.dp))
            Email()
        }
        LightText(
            "if you have any questions or ideas for us.",
            color = MaterialTheme.colors.primary,
            fontSize = 15,
            xOffset = 0,
            yOffset = 0
        )
        Column(
            modifier = Modifier
        ){
            AlignedLightText(
                "Send a message on our social media channels for a 1-on-1 conversation.",
                color = MaterialTheme.colors.primary,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
        ){
            AlignedLightText(
                "We would love to connect with you.",
                color = MaterialTheme.colors.primary,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
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
        ContactText()
    }
}