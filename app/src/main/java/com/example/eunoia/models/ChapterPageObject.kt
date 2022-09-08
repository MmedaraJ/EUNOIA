package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.ChapterPageData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object ChapterPageObject {
    @Parcelize
    data class ChapterPage(
        val id: String,
        val displayName: String,
        val pageNumber: Int,
        val audioKeysS3: List<String>,
        val audioNames: List<String>,
        val bedtimeStoryChapter: @RawValue BedtimeStoryChapterObject.BedtimeStoryChapter,
    ):Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: ChapterPageData
            get() = ChapterPageData.builder()
                .displayName(this.displayName)
                .pageNumber(this.pageNumber)
                .audioKeysS3(this.audioKeysS3)
                .audioNames(this.audioNames)
                .bedtimeStoryInfoChapter(this.bedtimeStoryChapter.data)
                .id(this.id)
                .build()

        companion object{
            fun from(chapterPageData: ChapterPageData): ChapterPage {
                val result = ChapterPage(
                    chapterPageData.id,
                    chapterPageData.displayName,
                    chapterPageData.pageNumber,
                    chapterPageData.audioKeysS3,
                    chapterPageData.audioNames,
                    BedtimeStoryChapterObject.BedtimeStoryChapter.from(chapterPageData.bedtimeStoryInfoChapter)
                )
                return result
            }
        }
    }

    class ChapterPageType : NavType<ChapterPage>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ChapterPage? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): ChapterPage {
            return Gson().fromJson(value, ChapterPage::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: ChapterPage) {
            bundle.putParcelable(key, value)
        }
    }
}