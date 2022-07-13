package com.example.eunoia.mvvm.soundMvvm.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.eunoia.mvvm.Converters

@Entity(tableName = "Sound")
data class SoundModel (
    @ColumnInfo(name = "originalOwnerUsername")
    var originalOwnerUsername: String,

    @ColumnInfo(name = "currentOwnerUsername")
    var currentOwnerUsername: String,

    @ColumnInfo(name = "originalName")
    var originalName: String,

    @ColumnInfo(name = "displayName")
    var displayName: String,

    @ColumnInfo(name = "shortDescription")
    var shortDescription: String,

    @ColumnInfo(name = "longDescription")
    var longDescription: String,

    @ColumnInfo(name = "icon")
    var icon: Int,

    @ColumnInfo(name = "fullPlayTime")
    var fullPlayTime: Int,

    @ColumnInfo(name = "visibleToOthers")
    var visibleToOthers: Boolean,

    @ColumnInfo(name = "audioNames")
    @TypeConverters(Converters::class)
    var audioNames: List<String>,

    @ColumnInfo(name = "uris")
    @TypeConverters(Converters::class)
    var audioUris: List<String>

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}