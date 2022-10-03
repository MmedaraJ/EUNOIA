package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.ChapterPageData
import com.amplifyframework.datastore.generated.model.ModelSortDirection
import com.example.eunoia.models.ChapterPageObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object ChapterPageBackend {
    private const val TAG = "ChapterPageBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createChapterPage(
        chapterPage: ChapterPageObject.ChapterPage,
        completed: (chapterPageData: ChapterPageData) -> Unit
    ) {
        scope.launch {
            Log.i(TAG, "Chapter page ==>> ${chapterPage.bedtimeStoryChapter.bedtimeStory.bedtimeStoryOwner}")
            Amplify.API.mutate(
                ModelMutation.create(chapterPage.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create chapterPage ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created chapterPage with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun updateChapterPage(
        chapterPage: ChapterPageData,
        completed: (chapterPageData: ChapterPageData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(chapterPage),
                { response ->
                    Log.i(TAG, "Updated $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from update chapterPage ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Updated chapterPage with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Update failed", error) }
            )
        }
    }

    fun queryChapterPageBasedOnChapter(
        bedtimeStoryChapterData: BedtimeStoryInfoChapterData,
        completed: (chaptersPages: List<ChapterPageData>) -> Unit
    ) {
        val result = mutableListOf<ChapterPageData>()
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(
                    ChapterPageData::class.java,
                    ChapterPageData.BEDTIME_STORY_INFO_CHAPTER.eq(bedtimeStoryChapterData.id),
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (page in response.data) {
                                if(page != null) {
                                    Log.i(TAG, page.toString())
                                    result.add(page)
                                }
                            }
                            completed(result)
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}