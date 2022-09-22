package com.example.eunoia.models

import com.amplifyframework.datastore.generated.model.CommentData
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData

object CommentObject{
    data class Comment(
        val id: String,
        val commentOwner: UserData,
        val comment: String,
        val sound: SoundData,
        val preset: PresetData,
    ){
        override fun toString(): String = comment
        //return an API CommentData from this CommentObject
        val data: CommentData
            get() = CommentData.builder()
                .commentOwner(this.commentOwner)
                .comment(this.comment)
                .sound(this.sound)
                .preset(this.preset)
                .id(this.id)
                .build()

        companion object{
            fun from(commentData: CommentData): Comment{
                val result = Comment(
                    commentData.id,
                    commentData.commentOwner,
                    commentData.comment,
                    commentData.sound,
                    commentData.preset,
                )
                return result
            }
        }
    }
}