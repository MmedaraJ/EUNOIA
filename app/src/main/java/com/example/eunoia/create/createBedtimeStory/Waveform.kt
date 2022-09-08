package com.example.eunoia.create.createBedtimeStory

import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.RectF
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.eunoia.ui.bottomSheets.*
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.theme.BeautyBush
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.ui.theme.Madang
import com.example.eunoia.ui.theme.SoftPeach

private var amplitudes = ArrayList<RecordedAudioData>()
private var spikes = ArrayList<RectF>()

private var radius = 6f
private var w = 9f
private var d = 9f
private var screenWidth = 0f
private var screenHeight = 160f
private var maxSpikes = 0
var num = mutableStateOf(0)

fun addAmplitude(recordedAudioData: RecordedAudioData){
    var norm = Math.min(recordedAudioData.amplitude.toInt()/40, 1000).toFloat()
    recordedAudioData.amplitude = norm
    amplitudes.add(recordedAudioData)

    spikes.clear()
    val amps = amplitudes.reversed()
    //maxSpikes = (width / (w + d)).toInt()
    //var amps = amplitudes.takeLast(60)
    for(i in amps.indices){
        val left = screenWidth - i * (w + d)
        val top = screenHeight / 2 - amps[i].amplitude / 2
        val right = left + w
        val bottom = top + amps[i].amplitude
        spikes.add(RectF(left, top, right, bottom))
    }
    num.value = spikes.size
}

fun clearAmplitudes(){
    amplitudes.clear()
    spikes.clear()
    num.value = spikes.size
}

fun getLastAmplitude(): RecordedAudioData{
    return amplitudes[amplitudes.size-1]
}

@Composable
fun AudioWaves(){
    ConstraintLayout(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
    ) {
        val (row) = createRefs()
        BoxWithConstraints(
            modifier = Modifier
                .height(160.dp)
                .constrainAs(row) {
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                }
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    //.background(Madang)
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .horizontalScroll(
                        state = rememberScrollState(),
                        enabled = true
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (num.value == spikes.size) {
                    spikes.forEachIndexed { index, spike ->
                        Canvas(
                            modifier = Modifier
                                .onGloballyPositioned {
                                    screenWidth = it.size.width.toFloat()
                                    screenHeight = it.size.height.toFloat()
                                }
                                .width((w + 2).dp)
                                /*.clickable {
                                    if (audioMediaPlayerInitialized.value) {
                                        Log.i(
                                            "Wave form",
                                            "Current media position 01 ${audioRecordedMediaPlayer!!.currentPosition}"
                                        )
                                        val amps = amplitudes.reversed()
                                        val seekToPos = amps[index].recFileLength
                                        Log.i("Wave form", "Seek to pos ${seekToPos}")
                                        if (seekToPos <= audioRecordedMediaPlayer!!.duration) {
                                            audioRecordedMediaPlayer!!.seekTo(seekToPos)
                                            recordingTimeDisplay.value =
                                                timer.setDuration(amps[index].milliSeconds)
                                            if(audioMediaPlayerIsPlaying.value){
                                                timer.startTicking()
                                            }
                                        }
                                        Log.i(
                                            "Wave form",
                                            "Current media position ${audioRecordedMediaPlayer!!.currentPosition}"
                                        )
                                    }
                                }*/
                        ) {
                            drawRoundRect(
                                BeautyBush,
                                Offset(spike.left, spike.top / 2),
                                Size(w, spike.bottom),
                                CornerRadius(28f, 28f)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class RecordedAudioData(
    var amplitude: Float,
    val milliSeconds: Long,
    val recFileLength: Int,
    //val currMediaPosition: Int
)

@OptIn(ExperimentalMaterialApi::class)
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
fun RecordAudioPreview() {
    EUNOIATheme {
        //AudioWaves()
    }
}