package com.example.eunoia.mvvm.commentMvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Comment")
data class CommentModel (
    @ColumnInfo(name = "soundID")
    var soundID: Int,

    @ColumnInfo(name = "commentText")
    var commentText: String

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}