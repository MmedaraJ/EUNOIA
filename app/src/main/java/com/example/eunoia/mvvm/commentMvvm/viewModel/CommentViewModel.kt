package com.example.eunoia.mvvm.commentMvvm.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eunoia.mvvm.commentMvvm.model.CommentModel
import com.example.eunoia.mvvm.commentMvvm.repository.CommentRepository

class CommentViewModel : ViewModel() {
    var liveDataComments: LiveData<MutableList<CommentModel>>? = null

    fun insertComment(context: Context, commentModel: CommentModel) {
        CommentRepository.insertComment(context, commentModel)
    }

    fun getAllComments(context: Context) : LiveData<MutableList<CommentModel>>? {
        liveDataComments = CommentRepository.getAllComments(context)
        return liveDataComments
    }

    fun getCommentBasedOnSoundID(context: Context, soundID: Int) : LiveData<MutableList<CommentModel>>? {
        liveDataComments = CommentRepository.getCommentBasedOnSoundID(context, soundID)
        return liveDataComments
    }
}