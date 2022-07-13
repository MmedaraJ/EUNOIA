package com.example.eunoia.mvvm.presetMvvm.database

import android.content.Context
import androidx.room.*
import com.example.eunoia.mvvm.Converters
import com.example.eunoia.mvvm.presetMvvm.dao.PresetDAO
import com.example.eunoia.mvvm.presetMvvm.model.PresetModel

@Database(entities = [PresetModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PresetDatabase : RoomDatabase() {
    abstract fun presetDao() : PresetDAO

    companion object {
        @Volatile
        private var INSTANCE: PresetDatabase? = null

        fun getDatabaseClient(context: Context) : PresetDatabase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, PresetDatabase::class.java, "PRESET_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }
        }
    }
}