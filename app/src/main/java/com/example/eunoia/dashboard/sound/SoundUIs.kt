package com.example.eunoia.dashboard.sound

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.R
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.theme.*
import java.lang.Math.*
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "Mixer"

@Composable
fun Mixer(original_sound_name: String){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(MixerBackground1, MixerBackground2),
                        center = Offset.Unspecified,
                        radius = Float.POSITIVE_INFINITY,
                        tileMode = TileMode.Clamp
                    ),
                ),
        ) {
            val (
                title,
                sliders,
                controls
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "[$original_sound_name]",
                    color = com.example.eunoia.ui.theme.Black,
                    fontSize = 18,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(sliders) {
                        top.linkTo(title.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                Sliders()
            }
            Column(
                modifier = Modifier
                    .constrainAs(controls) {
                        top.linkTo(sliders.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
            ) {
                Controls()
            }
        }
    }
}

@Composable
fun Sliders(){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        var volumes = arrayOf(0f, 4f, 8f, 3f, 5f, 7f, 6f, 9f, 2f, 3f)
        var sliderPositions = arrayOf(
            remember { mutableStateOf(volumes[0]) },
            remember { mutableStateOf(volumes[1]) },
            remember { mutableStateOf(volumes[2]) },
            remember { mutableStateOf(volumes[3]) },
            remember { mutableStateOf(volumes[4]) },
            remember { mutableStateOf(volumes[5]) },
            remember { mutableStateOf(volumes[6]) },
            remember { mutableStateOf(volumes[7]) },
            remember { mutableStateOf(volumes[8]) },
            remember { mutableStateOf(volumes[9]) }
        )
        var colors = arrayOf(
            Mixer1,
            Mixer2,
            Mixer3,
            Mixer4,
            Mixer5,
            Mixer6,
            Mixer7,
            Mixer8,
            Mixer9,
            Mixer10,
        )
        var labels = arrayOf(
            "Mixer1",
            "Mixer2",
            "Mixer3",
            "Mixer4",
            "Mixer5",
            "Mixer6",
            "Mixer7",
            "Mixer8",
            "Mixer9",
            "Mixer10",
        )
        sliderPositions.forEachIndexed {index, sliderPosition ->
            ConstraintLayout(
                modifier = Modifier.size(32.dp, 200.dp)
            ) {
                val (
                    mixer,
                    label
                ) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(mixer) {
                            top.linkTo(parent.top, margin = 0.dp)
                        }
                        .fillMaxSize()
                        .size(32.dp, 190.dp)
                ) {
                    val ripple = rememberRipple(
                        bounded = true,
                        color = colors[index]
                    )
                    var tapped by remember { mutableStateOf(false) }
                    val interactionSource = remember { MutableInteractionSource() }
                    Slider(
                        value = sliderPosition.value,
                        valueRange = 0f..10f,
                        onValueChange = {sliderPositions[index].value = it},
                        steps = 10,
                        onValueChangeFinished = { Log.i(TAG, "Value Changed") },
                        colors = SliderDefaults.colors(
                            thumbColor = colors[index],
                            activeTrackColor = colors[index],
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent,
                            inactiveTrackColor = colors[index],
                        ),
                        modifier = Modifier
                            .size(32.dp, 190.dp)
                            .graphicsLayer {
                                rotationZ = 270f
                                transformOrigin = TransformOrigin(0f, 0f)
                            }
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    Constraints(
                                        minWidth = constraints.minHeight,
                                        maxWidth = constraints.maxHeight,
                                        minHeight = constraints.minWidth,
                                        maxHeight = constraints.maxHeight,
                                    )
                                )
                                layout(placeable.height, placeable.width) {
                                    placeable.place(-placeable.width, 0)
                                }
                            }
                            .indication(interactionSource, LocalIndication.current)
                            .pointerInput(Unit) {
                                detectTapGestures(onPress = { offset ->
                                    tapped = true
                                    val press = PressInteraction.Press(offset)
                                    interactionSource.emit(press)
                                    tryAwaitRelease()
                                    interactionSource.emit(PressInteraction.Release(press))
                                    tapped = false
                                })
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(label) {
                            top.linkTo(mixer.bottom, margin = 10.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                ) {
                    NormalText(
                        text = labels[index],
                        color = com.example.eunoia.ui.theme.Black,
                        fontSize = 5,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
    }
}

@Composable
fun Controls(){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        val icons = arrayOf(
            R.drawable.replay_sound_icon,
            R.drawable.reset_sliders_icon,
            R.drawable.sound_timer_icon,
            R.drawable.play_icon,
            R.drawable.increase_levels_icon,
            R.drawable.decrease_levels_icon,
            R.drawable.meditation_bell_icon,
        )
        val addIcon = R.drawable.add_sound_icon
        icons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .gradientBackground(listOf(Control1, Control2), angle = 45f)
                    .border(BorderStroke(0.5.dp, Color.Black), RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                AnImage(
                    icon,
                    "icon",
                    12.dp,
                    12.dp,
                    0,
                    0
                ) {}
            }
        }
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Black),
            contentAlignment = Alignment.Center
        ) {
            AnImage(
                addIcon,
                "icon",
                10.dp,
                10.dp,
                0,
                0
            ) {}
        }
    }
}

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) = this.then(
    Modifier.drawBehind {
        val angleRad = angle / 180f * PI
        val x = cos(angleRad).toFloat() //Fractional x
        val y = sin(angleRad).toFloat() //Fractional y

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width, size.height),
                end = exactOffset
            ),
            size = size
        )
    }
)

@Composable
fun ControlPanelManual(showTap: Boolean, lambda: () -> Unit){
    var showTapColumn by rememberSaveable{ mutableStateOf(showTap) }

    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .clickable {
                showTapColumn = !showTapColumn
                lambda()
            }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = ControlPanelManualBackground,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                short_desc,
                instruction1,
                instruction2,
                tap
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    NormalText(
                        text = "[how to generate Noise Control panel]",
                        color = Black,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    NormalText(
                        text = "[how to generate Noise Control panel]",
                        color = ControlPanelManualBackground,
                        fontSize = 16,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(short_desc) {
                        top.linkTo(title.bottom, margin = 2.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    ExtraLightText(
                        text = "Noise Control panel allows you to create your own white noise and adjust " +
                                "selected white noise to your taste.",
                        color = Black,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    ExtraLightText(
                        text = "Noise Control panel allows you to create your own white noise and adjust " +
                                "selected white noise to your taste.",
                        color = ControlPanelManualBackground,
                        fontSize = 10,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(instruction1) {
                        top.linkTo(short_desc.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    AlignedLightText(
                        text = "Each slider controls a particular frequency band, from the lowest to the" +
                                " highest frequency. Adjust sliders to taste.",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    AlignedLightText(
                        text = "Each slider controls a particular frequency band, from the lowest to the" +
                                " highest frequency. Adjust sliders to taste.",
                        color = ControlPanelManualBackground,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(instruction2) {
                        top.linkTo(instruction1.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                if(!showTapColumn){
                    AlignedLightText(
                        text = "To mask undesirable noises, focus on bands sharing the same tone as the " +
                                "noise you want to cover. Doing so achieves a higher efficiency, and quieter " +
                                "masking noise levels.",
                        color = Black,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }else{
                    AlignedLightText(
                        text = "To mask undesirable noises, focus on bands sharing the same tone as the " +
                                "noise you want to cover. Doing so achieves a higher efficiency, and quieter " +
                                "masking noise levels.",
                        color = ControlPanelManualBackground,
                        fontSize = 13,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
            if(showTapColumn){
                Column(
                    modifier = Modifier
                        .constrainAs(tap) {
                            top.linkTo(instruction2.bottom, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                        }
                ) {
                    ConstraintLayout(
                    ) {
                        val (
                            tap_text,
                            up_icon
                        ) = createRefs()
                        Column(
                            modifier = Modifier
                                .constrainAs(tap_text) {
                                    top.linkTo(parent.top, margin = 16.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    bottom.linkTo(parent.bottom, margin = 0.dp)
                                }
                        ) {
                            AlignedLightText(
                                text = "Tap to read the manual",
                                color = Black,
                                fontSize = 13,
                                xOffset = 0,
                                yOffset = 0
                            )
                        }
                        Column(
                            modifier = Modifier
                                .constrainAs(up_icon) {
                                    top.linkTo(tap_text.top, margin = 0.dp)
                                    start.linkTo(tap_text.end, margin = 4.dp)
                                    bottom.linkTo(tap_text.bottom, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                        ) {
                            AnImage(
                                R.drawable.increase_levels_icon,
                                "noise control manual",
                                7.dp,
                                12.dp,
                                0,
                                0
                            ) {
                                showTapColumn = !showTapColumn
                                lambda()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Tip(){
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        backgroundColor = TipBackground,
        elevation = 8.dp
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (
                title,
                icon,
                info
            ) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = "[tip]",
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
            Column(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(title.top, margin = 0.dp)
                        bottom.linkTo(title.bottom, margin = 0.dp)
                        start.linkTo(title.end, margin = 4.dp)
                    }
            ) {
                AnImage(
                    R.drawable.tip_icon,
                    "tip",
                    9.dp,
                    11.dp,
                    0,
                    0
                ) {}
            }
            Column(
                modifier = Modifier
                    .constrainAs(info) {
                        top.linkTo(title.bottom, margin = 2.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = "Click a comment card to load the user's sound on Noise Control panel",
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
fun OtherUsersFeedback(feedback: String, lambda: () -> Unit){
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .padding(bottom = 16.dp)
        .wrapContentHeight()
        .clickable {
            clicked = !clicked
            lambda()
        }
        .fillMaxWidth()

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = OtherUsersFeedbackBackground,
        elevation = 8.dp,
    ){
        ConstraintLayout(
            modifier = Modifier.padding(16.dp)
        ) {
            val (user_feedback) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(user_feedback) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                LightText(
                    text = feedback,
                    color = Black,
                    fontSize = 13,
                    xOffset = 0,
                    yOffset = 0
                )
            }
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
        OtherUsersFeedback("geujhfkejkfekfehf"){}
    }
}