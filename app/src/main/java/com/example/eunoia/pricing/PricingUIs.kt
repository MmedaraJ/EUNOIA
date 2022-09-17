package com.example.eunoia.pricing

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.eunoia.R
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.*

@Composable
fun PricingCompleteTitle(){
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        val (
            title_text,
            line,
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
                400.0,
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
            AlignedNormalText(
                "Pricing Plans\nthat benefit you.",
                color = MaterialTheme.colors.primary,
                fontSize = 25,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier.constrainAs(title_icon_right){
                top.linkTo(title_text.top, margin = 0.dp)
                bottom.linkTo(title_text.bottom, margin = 10.dp)
                start.linkTo(title_text.end, margin = 0.dp)
                end.linkTo(parent.end, margin = 40.dp)
            }
        ) {
            AnImage(
                R.drawable.pricing_plans_icon,
                "pricing plans icon",
                90.0,
                70.0,
                0,
                0,
                LocalContext.current
            ) {}
        }
    }
}

@Composable
fun Rookie(lambda: () -> Unit) {
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .fillMaxWidth(0.45F)
        .clickable {
            clicked = !clicked
            lambda()
        }

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = CarouselPink,
        elevation = 8.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(12.dp)
        ) {
            val (
                title,
                sub_title,
                price,
                info1,
                info2,
                subscribe_button
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "Rookie",
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(sub_title) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Free for everyone",
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(price) {
                        top.linkTo(sub_title.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                MorgeNormalText(
                    text = "$0/mo",
                    color = Black,
                    fontSize = 25,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info1) {
                        top.linkTo(price.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "1 Slumber Party (2 members)",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info2) {
                        top.linkTo(info1.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "10+ Pre-made White Noise",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(subscribe_button) {
                        top.linkTo(info2.bottom, margin = 40.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CustomizableButton(
                    text = "subscribe",
                    height = 43,
                    fontSize = 12,
                    textColor = Black,
                    backgroundColor = OldLace,
                    corner = 10,
                    borderStroke = 0.5,
                    borderColor = Black,
                    textType = "normal",
                    maxWidthFraction = 1F
                ) {}
            }
        }
    }
}

@Composable
fun MasterSix(lambda: () -> Unit) {
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .fillMaxWidth(0.45F)
        .clickable {
            clicked = !clicked
            lambda()
        }

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = CarouselPink,
        elevation = 8.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(12.dp)
        ) {
            val (
                title,
                sub_title,
                price,
                info1,
                info2,
                info3,
                info4,
                subscribe_button
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "Master",
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(sub_title) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "A little spice",
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(price) {
                        top.linkTo(sub_title.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                MorgeNormalText(
                    text = "$6/mo",
                    color = Black,
                    fontSize = 25,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info1) {
                        top.linkTo(price.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "3 Slumber Parties (4 members)",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info2) {
                        top.linkTo(info1.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Unlimited White Noise",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info3) {
                        top.linkTo(info2.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "8-hour Countdown Clock",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info4) {
                        top.linkTo(info3.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Weekly Report",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(subscribe_button) {
                        top.linkTo(info2.bottom, margin = 40.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
                    .widthIn()
            ) {
                CustomizableButton(
                    text = "subscribe",
                    height = 43,
                    fontSize = 12,
                    textColor = Black,
                    backgroundColor = OldLace,
                    corner = 10,
                    borderStroke = 0.5,
                    borderColor = Black,
                    textType = "normal",
                    maxWidthFraction = 1F
                ) {}
            }
        }
    }
}

@Composable
fun MasterNine(lambda: () -> Unit) {
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .fillMaxWidth(0.45F)
        .clickable {
            clicked = !clicked
            lambda()
        }

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = CarouselPink,
        elevation = 8.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(12.dp)
        ) {
            val (
                title,
                sub_title,
                price,
                info1,
                info2,
                info3,
                info4,
                subscribe_button
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "Master",
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(sub_title) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Limit who?!",
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(price) {
                        top.linkTo(sub_title.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                MorgeNormalText(
                    text = "$9/mo",
                    color = Black,
                    fontSize = 25,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info1) {
                        top.linkTo(price.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Unlimited Slumber Parties",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info2) {
                        top.linkTo(info1.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Create your White Noise",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info3) {
                        top.linkTo(info2.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Customize Countdown Alarm",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info4) {
                        top.linkTo(info3.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Detailed Statistics",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(subscribe_button) {
                        top.linkTo(info2.bottom, margin = 40.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CustomizableButton(
                    text = "subscribe",
                    height = 43,
                    fontSize = 12,
                    textColor = Black,
                    backgroundColor = OldLace,
                    corner = 10,
                    borderStroke = 0.5,
                    borderColor = Black,
                    textType = "normal",
                    maxWidthFraction = 1F
                ) {}
            }
        }
    }
}

@Composable
fun Contributor(lambda: () -> Unit) {
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .fillMaxWidth(0.45F)
        .clickable {
            clicked = !clicked
            lambda()
        }

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = CarouselPink,
        elevation = 8.dp,
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(12.dp)
        ) {
            val (
                title,
                sub_title,
                price,
                info1,
                info2,
                info3,
                info4,
                popular,
                subscribe_button
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "Contributor",
                    color = Black,
                    fontSize = 16,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(sub_title) {
                        top.linkTo(title.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Help improve Eunoia -",
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(price) {
                        top.linkTo(sub_title.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                MorgeNormalText(
                    text = "$12/Qrt",
                    color = Black,
                    fontSize = 25,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(ClassicRose)
                    .constrainAs(popular) {
                        top.linkTo(price.top, margin = 0.dp)
                        bottom.linkTo(price.bottom, margin = 0.dp)
                        start.linkTo(price.end, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
                contentAlignment = Alignment.Center
            ){
                MorgeNormalText(
                    text = "popular",
                    color = Black,
                    fontSize = 12,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info1) {
                        top.linkTo(price.bottom, margin = 6.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Unlimited Slumber Parties",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info2) {
                        top.linkTo(info1.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Create your White Noise",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info3) {
                        top.linkTo(info2.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Customize Countdown Alarm",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(info4) {
                        top.linkTo(info3.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Detailed Statistics",
                    color = Black,
                    fontSize = 9,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(subscribe_button) {
                        top.linkTo(info2.bottom, margin = 40.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                CustomizableButton(
                    text = "subscribe",
                    height = 43,
                    fontSize = 12,
                    textColor = Black,
                    backgroundColor = ClassicRose,
                    corner = 10,
                    borderStroke = 0.5,
                    borderColor = Black,
                    textType = "normal",
                    maxWidthFraction = 1F
                ) {}
            }
        }
    }
}


@Composable
fun AllPricingUIs(navController: NavController){
    ConstraintLayout{
        val (
            prices,
        ) = createRefs()
        SimpleFlowRow(
            verticalGap = 12.dp,
            horizontalGap = 12.dp,
            alignment = Alignment.Start,
            modifier = Modifier
                .constrainAs(prices) {
                    top.linkTo(parent.top, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
        ) {
            Rookie{}
            MasterSix{}
            MasterNine{}
            Contributor{}
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
        MasterNine{}
    }
}