package com.example.eunoia.dashboard.bedtimeStory

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.ui.theme.Black
import kotlin.math.*

private const val TAG = "Circular slider"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircularSlider(
    modifier: Modifier = Modifier,
    padding: Float = 30f,
    stroke: Float = 20f,
    cap: StrokeCap = StrokeCap.Round,
    touchStroke: Float = 50f,
    thumbColor: Color = Color.Blue,
    progressColor: Color = Color.Black,
    backgroundColor: Color = Color.LightGray,
    debug: Boolean = false,
    appliedAngleChanged: (appliedAngle: Float) -> Unit
){
    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    //var angle by remember { mutableStateOf(-270f) }
    var down  by remember { mutableStateOf(false) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var appliedAngle by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = angle.value){
        var a = angle.value
        if(clicked.value) {
            a += 270f
        }
        if(a<=0f){
            a += 360f
        }
        //a = a.coerceIn(0f,360f)
        if(a>360f){
            a -= 360f
        }
        if(a==360f){
            a = 0f
        }
        appliedAngle = a
    }

    LaunchedEffect(key1 = appliedAngle){
        appliedAngleChanged(appliedAngle)
    }

    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                width = it.size.width
                height = it.size.height
                center = Offset(width / 2f, height / 2f)
                radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke/2f
            }
            .pointerInteropFilter {
                val x = it.x
                val y = it.y
                val offset = Offset(x, y)
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val d = distance(offset, center)
                        val a = angle(center, offset)
                        if (d >= radius - touchStroke / 2f && d <= radius + touchStroke / 2f) {
                            down = true
                            clicked.value = true
                            angle.value = a + 0
                        } else {
                            down = false
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (down) {
                            clicked.value = true
                            angle.value = angle(center, offset)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        down = false
                    }
                    else -> return@pointerInteropFilter false
                }
                return@pointerInteropFilter true
            }
    ){
        drawArc(
            color = backgroundColor,
            startAngle = 270f,
            sweepAngle = 360f,
            topLeft = center - Offset(radius,radius),
            size = Size(radius*2,radius*2),
            useCenter = false,
            style = Stroke(
                width = 4f,
                cap = cap
            )
        )
        drawArc(
            color = progressColor,
            startAngle = 270f,
            sweepAngle = appliedAngle,
            topLeft = center - Offset(radius,radius),
            size = Size(radius*2,radius*2),
            useCenter = false,
            style = Stroke(
                width = 3f,
                cap = cap
            )
        )
        drawCircle(
            color = thumbColor,
            radius = 16f,
            center = center + Offset(
                radius*cos((270+appliedAngle)*PI/180f).toFloat(),
                radius*sin((270+appliedAngle)*PI/180f).toFloat()
            ),
        )
        drawArc(
            color = Black,
            startAngle = 270f,
            sweepAngle = 360f,
            topLeft = ( center + Offset(
                radius*cos((270+appliedAngle)*PI/180f).toFloat(),
                radius*sin((270+appliedAngle)*PI/180f).toFloat()
            ) )  - Offset(16f,16f),
            size = Size(16f*2,16f*2),
            useCenter = false,
            style = Stroke(
                width = 4f,
                cap = cap
            )
        )
        if(debug){
            drawRect(
                color = Color.Green,
                topLeft = Offset.Zero,
                size = Size(width.toFloat(),height.toFloat()),
                style = Stroke(
                    3f
                )
            )
            drawRect(
                color = Color.Red,
                topLeft = Offset(padding,padding),
                size = Size(width.toFloat()-padding*2,height.toFloat()-padding*2),
                style = Stroke(
                    4f
                )
            )
            drawRect(
                color = Color.Blue,
                topLeft = Offset(padding,padding),
                size = Size(width.toFloat()-padding*2,height.toFloat()-padding*2),
                style = Stroke(
                    4f
                )
            )
            drawCircle(
                color = Color.Red,
                center = center,
                radius = radius+stroke/2f,
                style = Stroke(2f)
            )
            drawCircle(
                color = Color.Red,
                center = center,
                radius = radius-stroke/2f,
                style = Stroke(2f)
            )
        }
    }
}

fun angle(center: Offset, offset: Offset): Float {
    val rad = atan2(
        center.y - offset.y,
        center.x - offset.x
    )
    val deg = Math.toDegrees(rad.toDouble())
    return deg.toFloat()
}
fun distance(first: Offset, second: Offset) : Float{
    return sqrt((first.x-second.x).square()+(first.y-second.y).square())
}
fun Float.square(): Float{
    return this*this
}