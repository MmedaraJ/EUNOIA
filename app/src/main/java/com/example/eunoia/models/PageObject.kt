package com.example.eunoia.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.amplifyframework.datastore.generated.model.PageData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

object PageObject {
    @Parcelize
    data class Page(
        val id: String,
        val displayName: String,
        val pageNumber: Int,
        val audioKeysS3: List<String>,
        val audioNames: List<String>,
        //val bedtimeStoryChapter: @RawValue BedtimeStoryChapterObject.BedtimeStoryChapter,
        val bedtimeStoryChapterId: String?,
    ):Parcelable{
        override fun toString(): String {
            return Uri.encode(Gson().toJson(this))
        }

        val data: PageData
            get() = PageData.builder()
                .displayName(this.displayName)
                .pageNumber(this.pageNumber)
                .audioKeysS3(this.audioKeysS3)
                .audioNames(this.audioNames)
                //.bedtimeStoryInfoChapter(this.bedtimeStoryChapter.data)
                .bedtimeStoryInfoChapterId(this.bedtimeStoryChapterId)
                .id(this.id)
                .build()

        companion object{
            fun from(PageData: PageData): Page {
                val result = Page(
                    PageData.id,
                    PageData.displayName,
                    PageData.pageNumber,
                    PageData.audioKeysS3,
                    PageData.audioNames,
                    //BedtimeStoryChapterObject.BedtimeStoryChapter.from(PageData.bedtimeStoryInfoChapter),
                    PageData.bedtimeStoryInfoChapterId,
                )
                return result
            }
        }
    }

    class PageType : NavType<Page>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Page? {
            return bundle.getParcelable(key)
        }
        override fun parseValue(value: String): Page {
            return Gson().fromJson(value, Page::class.java)
        }
        override fun put(bundle: Bundle, key: String, value: Page) {
            bundle.putParcelable(key, value)
        }
    }
}