package com.example.eunoia.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.eunoia.create.createPrayer.activatePrayerRecordingControls
import com.example.eunoia.create.createPrayer.deActivatePrayerRecordingControls
import com.example.eunoia.create.createPrayer.resetAllPrayerRecordUIMediaPlayers
import com.example.eunoia.create.createPrayer.resetPrayerRecordingCDT
import com.example.eunoia.create.resetEverything
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerRecordingViewModel @Inject constructor() : ViewModel() {
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
        resetAllPrayerRecordUIMediaPlayers()
        generalMediaPlayerService.onDestroy()

        resetPrayerRecordingCDT()

        deActivatePrayerRecordingControls(0)
        deActivatePrayerRecordingControls(1)
        activatePrayerRecordingControls(2)
        deActivatePrayerRecordingControls(3)
        deActivatePrayerRecordingControls(4)
    }
}