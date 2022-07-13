package com.example.eunoia.mvvm.presetMvvm.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eunoia.mvvm.presetMvvm.model.PresetModel
import com.example.eunoia.mvvm.presetMvvm.repository.PresetRepository

class PresetViewModel : ViewModel() {
    var liveDataPresets: LiveData<MutableList<PresetModel>>? = null

    fun insertPreset(context: Context, presetModel: PresetModel) {
        PresetRepository.insertPreset(context, presetModel)
    }

    fun getAllPresets(context: Context) : LiveData<MutableList<PresetModel>>? {
        liveDataPresets = PresetRepository.getAllPresets(context)
        return liveDataPresets
    }

    fun getPresetBasedOnSoundID(context: Context, soundID: Int) : LiveData<MutableList<PresetModel>>? {
        liveDataPresets = PresetRepository.getPresetBasedOnSoundID(context, soundID)
        return liveDataPresets
    }
}