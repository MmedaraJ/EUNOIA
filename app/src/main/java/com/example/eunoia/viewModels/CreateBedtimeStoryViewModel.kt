package com.example.eunoia.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateBedtimeStoryViewModel: ViewModel() {
    var recordedAudioS3 by mutableStateOf<String?>(null)
    var recordedAudioName by mutableStateOf<String?>(null)
}