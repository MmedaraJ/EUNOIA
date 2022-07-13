package com.example.eunoia.mvvm.soundMvvm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.eunoia.mvvm.soundMvvm.model.SoundModel
import com.example.eunoia.mvvm.soundMvvm.database.SoundDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SoundRepository {
    companion object {
        private val scope = CoroutineScope(Job() + Dispatchers.IO)
        var soundDatabase: SoundDatabase? = null
        var soundModel: LiveData<SoundModel>? = null
        var soundModels: LiveData<MutableList<SoundModel>>? = null

        fun initializeDB(context: Context) : SoundDatabase {
            return SoundDatabase.getDatabaseClient(context)
        }

        fun insertSound(context: Context, soundModel: SoundModel) {
            soundDatabase = initializeDB(context)
            scope.launch {
                soundDatabase!!.soundDao().insertSound(soundModel)
            }
        }

        fun getAllSounds(context: Context): LiveData<MutableList<SoundModel>> {
            soundDatabase = initializeDB(context)
            soundModels = soundDatabase!!.soundDao().getAllSounds()
            return soundModels as LiveData<MutableList<SoundModel>>
        }

        fun getSoundBasedOnDisplayName(context: Context, displayName: String): LiveData<MutableList<SoundModel>> {
            soundDatabase = initializeDB(context)
            soundModels = soundDatabase!!.soundDao().getSoundBasedOnDisplayName(displayName)
            return soundModels as LiveData<MutableList<SoundModel>>
        }

        fun getSoundBasedOnID(context: Context, id: String): LiveData<MutableList<SoundModel>> {
            soundDatabase = initializeDB(context)
            soundModels = soundDatabase!!.soundDao().getSoundBasedOnID(id)
            return soundModels as LiveData<MutableList<SoundModel>>
        }
    }
}