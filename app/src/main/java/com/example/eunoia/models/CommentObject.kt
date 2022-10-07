package com.example.eunoia.models

import android.os.Parcelable
import com.amplifyframework.datastore.generated.model.CommentData
import com.amplifyframework.datastore.generated.model.PresetData
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object CommentObject{
    @Parcelize
    data class Comment(
        val id: String,
        val commentOwner: @RawValue UserObject.User?,
        val commentOwnerId: String?,
        val comment: String,
        val sound: @RawValue SoundObject.Sound?,
        val soundId: String?,
        val preset: @RawValue SoundPresetObject.SoundPreset?,
        val presetId: String?,
    ): Parcelable {
        override fun toString(): String = comment
        //return an API CommentData from this CommentObject
        val data: CommentData
            get() = CommentData.builder()
                .commentOwner(this.commentOwner!!.data)
                .comment(this.comment)
                .sound(this.sound!!.data)
                .preset(this.preset!!.data)
                .commentOwnerId(this.commentOwnerId)
                .soundId(this.soundId)
                .presetId(this.presetId)
                .id(this.id)
                .build()

        companion object{
            fun from(commentData: CommentData): Comment{
                val result = Comment(
                    commentData.id,
                    UserObject.User.from(commentData.commentOwner),
                    commentData.commentOwnerId,
                    commentData.comment,
                    SoundObject.Sound.from(commentData.sound),
                    commentData.soundId,
                    SoundPresetObject.SoundPreset.from(commentData.preset),
                    commentData.presetId,
                )
                return result
            }
        }
    }
}