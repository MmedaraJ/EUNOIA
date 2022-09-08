package com.example.eunoia.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RecordAudioViewModel: ViewModel() {
    var currentRoutineElementWhoOwnsRecording by mutableStateOf<Any?>(null)
}