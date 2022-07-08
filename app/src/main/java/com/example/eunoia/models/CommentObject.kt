package com.example.eunoia.models

import com.amplifyframework.datastore.generated.model.CommentData
import com.amplifyframework.datastore.generated.model.SoundData

object CommentObject{
    private const val TAG = "CommentObject"

    data class Comment(
        val id: String,
        val sound: SoundData,
        val comment: String
    ){
        override fun toString(): String = comment
        //return an API CommentData from this CommentObject
        val data: CommentData
            get() = CommentData.builder()
                .sound(this.sound)
                .comment(this.comment)
                .id(this.id)
                .build()

        companion object{
            fun from(commentData: CommentData): Comment{
                val result = Comment(
                    commentData.id,
                    commentData.sound,
                    commentData.comment
                )
                return result
            }
        }
    }
}