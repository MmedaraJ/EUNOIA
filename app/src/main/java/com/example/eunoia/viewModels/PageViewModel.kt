package com.example.eunoia.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.eunoia.create.createBedtimeStory.*
import com.example.eunoia.create.resetEverything
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor() : ViewModel() {
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
        resetAllPageRecordBedtimeStoryUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        resetRecordingCDT()

        deActivatePageControls(0)
        deActivatePageControls(1)
        activatePageControls(2)
        deActivatePageControls(3)
        deActivatePageControls(4)
        deActivatePageControls(5)
    }
}