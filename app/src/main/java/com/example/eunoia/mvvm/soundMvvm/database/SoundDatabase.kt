package com.example.eunoia.mvvm.soundMvvm.database

import android.content.Context
import androidx.room.*
import com.example.eunoia.mvvm.Converters
import com.example.eunoia.mvvm.soundMvvm.dao.SoundDAO
import com.example.eunoia.mvvm.soundMvvm.model.SoundModel

@Database(entities = [SoundModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SoundDatabase : RoomDatabase() {
    abstract fun soundDao() : SoundDAO

    companion object {
        @Volatile
        private var INSTANCE: SoundDatabase? = null

        fun getDatabaseClient(context: Context) : SoundDatabase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, SoundDatabase::class.java, "SOUND_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }
        }
    }
}