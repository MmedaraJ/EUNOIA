package com.example.eunoia.mvvm.presetMvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.eunoia.mvvm.Converters

@Entity(tableName = "Preset")
data class PresetModel (
    @ColumnInfo(name = "soundID")
    var soundID: Int,

    @ColumnInfo(name = "keys")
    var keys: String,

    @ColumnInfo(name = "volumes")
    @TypeConverters(Converters::class)
    var volumes: List<Int>

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}