package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object BedtimeStoryChapterObject{
    @Parcelize
    data class BedtimeStoryChapter(
        val id: String,
        val displayName: String,
        val chapterNumber: Int,
        val bedtimeStory: @RawValue BedtimeStoryObject.BedtimeStory,
    ):Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: BedtimeStoryInfoChapterData
            get() = BedtimeStoryInfoChapterData.builder()
                .displayName(this.displayName)
                .chapterNumber(this.chapterNumber)
                .bedtimeStoryInfo(this.bedtimeStory.data)
                .id(this.id)
                .build()

        companion object{
            fun from(bedtimeStoryChapterData: BedtimeStoryInfoChapterData): BedtimeStoryChapter {
                val result = BedtimeStoryChapter(
                    bedtimeStoryChapterData.id,
                    bedtimeStoryChapterData.displayName,
                    bedtimeStoryChapterData.chapterNumber,
                    BedtimeStoryObject.BedtimeStory.from(bedtimeStoryChapterData.bedtimeStoryInfo)
                )
                return result
            }
        }
    }

    class BedtimeStoryChapterType : NavType<BedtimeStoryChapter>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): BedtimeStoryChapter? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): BedtimeStoryChapter {
            return Gson().fromJson(value, BedtimeStoryChapter::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: BedtimeStoryChapter) {
            bundle.putParcelable(key, value)
        }
    }
}