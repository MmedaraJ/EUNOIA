package com.example.eunoia.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExtraBoldText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h1,
        fontSize = fontSize.sp,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun BoldText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h2,
        fontSize = fontSize.sp,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun NormalText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h3,
        fontSize = fontSize.sp,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun AlignedNormalText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h3,
        fontSize = fontSize.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun LightText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h4,
        fontSize = fontSize.sp,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun AlignedLightText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h4,
        fontSize = fontSize.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ExtraLightText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.h5,
        fontSize = fontSize.sp,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun MorgeNormalText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.body1,
        fontSize = fontSize.sp,
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ClickableExtraBoldText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    var enabled by remember { mutableStateOf(true) }
    ClickableText(
        text = AnnotatedString(text),
        style = (MaterialTheme.typography).h1.merge(
            TextStyle(
                textDecoration = TextDecoration.Underline,
                color = color,
                fontSize = fontSize.sp,
            )
        ),
        onClick = {
            lambda()
            if(enabled){
                enabled = false
            }
        },
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ClickableBoldText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    var enabled by remember { mutableStateOf(true) }
    ClickableText(
        text = AnnotatedString(text),
        style = (MaterialTheme.typography).h2.merge(
            TextStyle(
                textDecoration = TextDecoration.Underline,
                color = color,
                fontSize = fontSize.sp,
            )
        ),
        onClick = {
            lambda()
            if(enabled){
                enabled = false
            }
        },
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ClickableNormalText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    var enabled by remember { mutableStateOf(true) }
    ClickableText(
        text = AnnotatedString(text),
        style = (MaterialTheme.typography).h3.merge(
            TextStyle(
                textDecoration = TextDecoration.Underline,
                color = color,
                fontSize = fontSize.sp,
            )
        ),
        onClick = {
            lambda()
            if(enabled){
                enabled = false
            }
        },
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ClickableLightText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    var enabled by remember { mutableStateOf(true) }
    ClickableText(
        text = AnnotatedString(text),
        style = (MaterialTheme.typography).h4.merge(
            TextStyle(
                textDecoration = TextDecoration.Underline,
                color = color,
                fontSize = fontSize.sp,
            )
        ),
        onClick = {
            lambda()
            if(enabled){
                enabled = false
            }
        },
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ClickableExtraLightText(text: String, color: Color, fontSize: Int, xOffset: Int, yOffset: Int, lambda: () -> Unit){
    var enabled by remember { mutableStateOf(true) }
    ClickableText(
        text = AnnotatedString(text),
        style = (MaterialTheme.typography).h5.merge(
            TextStyle(
                textDecoration = TextDecoration.Underline,
                color = color,
                fontSize = fontSize.sp,
            )
        ),
        onClick = {
            lambda()
            if(enabled){
                enabled = false
            }
        },
        modifier = Modifier
            .offset(x = xOffset.dp, y = yOffset.dp)
    )
}

@Composable
fun ErrorTextSize12(text: String){
    Text(
        text = text,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.body1
    )
}