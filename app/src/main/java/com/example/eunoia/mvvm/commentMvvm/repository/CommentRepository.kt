package com.example.eunoia.mvvm.commentMvvm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.eunoia.mvvm.commentMvvm.database.CommentDatabase
import com.example.eunoia.mvvm.commentMvvm.model.CommentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CommentRepository {
    companion object {
        private val scope = CoroutineScope(Job() + Dispatchers.IO)
        var commentDatabase: CommentDatabase? = null
        var commentModel: LiveData<CommentModel>? = null
        var commentModels: LiveData<MutableList<CommentModel>>? = null

        fun initializeDB(context: Context) : CommentDatabase {
            return CommentDatabase.getDatabaseClient(context)
        }

        fun insertComment(context: Context, commentModel: CommentModel) {
            commentDatabase = initializeDB(context)
            scope.launch {
                commentDatabase!!.commentDao().insertComment(commentModel)
            }
        }

        fun getAllComments(context: Context): LiveData<MutableList<CommentModel>> {
            commentDatabase = initializeDB(context)
            commentModels = commentDatabase!!.commentDao().getAllComments()
            return commentModels as LiveData<MutableList<CommentModel>>
        }

        fun getCommentBasedOnSoundID(context: Context, soundID: Int): LiveData<MutableList<CommentModel>> {
            commentDatabase = initializeDB(context)
            commentModels = commentDatabase!!.commentDao().getCommentBasedOnSoundID(soundID)
            return commentModels as LiveData<MutableList<CommentModel>>
        }
    }
}