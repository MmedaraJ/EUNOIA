package com.example.eunoia.mvvm.soundMvvm.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eunoia.mvvm.soundMvvm.model.SoundModel

val TAG = "SoundDao"
@Dao
interface SoundDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSound(soundModel: SoundModel){
        Log.i(TAG, "Insert soound")
    }

    @Query("SELECT * FROM Sound")
    fun getAllSounds() : LiveData<MutableList<SoundModel>>

    @Query("SELECT * FROM Sound WHERE displayName =:displayName")
    fun getSoundBasedOnDisplayName(displayName: String) : LiveData<MutableList<SoundModel>>

    @Query("SELECT * FROM Sound WHERE id =:id")
    fun getSoundBasedOnID(id: String) : LiveData<MutableList<SoundModel>>
}