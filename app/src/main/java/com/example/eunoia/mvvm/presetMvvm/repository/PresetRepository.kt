package com.example.eunoia.mvvm.presetMvvm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.eunoia.mvvm.presetMvvm.database.PresetDatabase
import com.example.eunoia.mvvm.presetMvvm.model.PresetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PresetRepository {
    companion object {
        private val scope = CoroutineScope(Job() + Dispatchers.IO)
        var presetDatabase: PresetDatabase? = null
        var presetModel: LiveData<PresetModel>? = null
        var presetModels: LiveData<MutableList<PresetModel>>? = null

        fun initializeDB(context: Context) : PresetDatabase {
            return PresetDatabase.getDatabaseClient(context)
        }

        fun insertPreset(context: Context, presetModel: PresetModel) {
            presetDatabase = initializeDB(context)
            scope.launch {
                presetDatabase!!.presetDao().insertPreset(presetModel)
            }
        }

        fun getAllPresets(context: Context): LiveData<MutableList<PresetModel>> {
            presetDatabase = initializeDB(context)
            presetModels = presetDatabase!!.presetDao().getAllPresets()
            return presetModels as LiveData<MutableList<PresetModel>>
        }

        fun getPresetBasedOnSoundID(context: Context, soundID: Int): LiveData<MutableList<PresetModel>> {
            presetDatabase = initializeDB(context)
            presetModels = presetDatabase!!.presetDao().getPresetBasedOnSoundID(soundID)
            return presetModels as LiveData<MutableList<PresetModel>>
        }
    }
}