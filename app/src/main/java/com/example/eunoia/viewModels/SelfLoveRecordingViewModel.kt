package com.example.eunoia.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.eunoia.create.createSelfLove.*
import com.example.eunoia.create.resetEverything
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelfLoveRecordingViewModel @Inject constructor() : ViewModel() {
    fun onStart(
        soundMediaPlayerService: SoundMediaPlayerService,
        generalMediaPlayerService: GeneralMediaPlayerService,
        context: Context
    ) {
        resetEverything(
            soundMediaPlayerService,
            generalMediaPlayerService,
            context
        ){}
    }

    fun onStop(generalMediaPlayerService: GeneralMediaPlayerService) {
        resetAllSelfLoveRecordUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        resetSelfLoveRecordingCDT()

        deActivateSelfLoveRecordingControls(0)
        deActivateSelfLoveRecordingControls(1)
        activateSelfLoveRecordingControls(2)
        deActivateSelfLoveRecordingControls(3)
        deActivateSelfLoveRecordingControls(4)
    }
}