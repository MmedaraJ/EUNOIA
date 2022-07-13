package com.example.eunoia.mvvm.presetMvvm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eunoia.mvvm.presetMvvm.model.PresetModel

@Dao
interface PresetDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPreset(presetModel: PresetModel)

    @Query("SELECT * FROM Preset")
    fun getAllPresets() : LiveData<MutableList<PresetModel>>

    @Query("SELECT * FROM Preset WHERE soundID =:soundID")
    fun getPresetBasedOnSoundID(soundID: Int) : LiveData<MutableList<PresetModel>>
}