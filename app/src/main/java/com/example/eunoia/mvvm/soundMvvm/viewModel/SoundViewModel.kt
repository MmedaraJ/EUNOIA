package com.example.eunoia.mvvm.soundMvvm.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eunoia.mvvm.soundMvvm.model.SoundModel
import com.example.eunoia.mvvm.soundMvvm.repository.SoundRepository

class SoundViewModel : ViewModel() {
    var liveDataSounds: LiveData<MutableList<SoundModel>>? = null

    fun insertSound(context: Context, soundModel: SoundModel) {
        SoundRepository.insertSound(context, soundModel)
    }

    fun getAllSounds(context: Context) : LiveData<MutableList<SoundModel>>? {
        liveDataSounds = SoundRepository.getAllSounds(context)
        return liveDataSounds
    }

    fun getSoundBasedOnDisplayName(context: Context, displayName: String) : LiveData<MutableList<SoundModel>>? {
        liveDataSounds = SoundRepository.getSoundBasedOnDisplayName(context, displayName)
        return liveDataSounds
    }

    fun getSoundBasedOnID(context: Context, id: String) : LiveData<MutableList<SoundModel>>? {
        liveDataSounds = SoundRepository.getSoundBasedOnID(context, id)
        return liveDataSounds
    }
}