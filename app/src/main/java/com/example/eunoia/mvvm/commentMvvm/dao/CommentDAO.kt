package com.example.eunoia.mvvm.commentMvvm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eunoia.mvvm.commentMvvm.model.CommentModel

@Dao
interface CommentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComment(commentModel: CommentModel)

    @Query("SELECT * FROM Comment")
    fun getAllComments() : LiveData<MutableList<CommentModel>>

    @Query("SELECT * FROM Comment WHERE soundID =:soundID")
    fun getCommentBasedOnSoundID(soundID: Int) : LiveData<MutableList<CommentModel>>
}