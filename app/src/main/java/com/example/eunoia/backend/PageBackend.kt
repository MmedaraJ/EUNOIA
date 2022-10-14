package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.BedtimeStoryInfoChapterData
import com.amplifyframework.datastore.generated.model.PageData
import com.example.eunoia.models.PageObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object PageBackend {
    private const val TAG = "PageBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createPage(
        page: PageObject.Page,
        completed: (pageData: PageData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(page.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create Page ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created Page with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun updatePage(
        pageData: PageData,
        completed: (pageData: PageData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(pageData),
                { response ->
                    Log.i(TAG, "Updated $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from update Page ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Updated Page with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Update failed", error) }
            )
        }
    }

    fun queryPageBasedOnChapter(
        bedtimeStoryChapterData: BedtimeStoryInfoChapterData,
        completed: (chaptersPages: List<PageData>) -> Unit
    ) {
        val result = mutableListOf<PageData>()
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(
                    PageData::class.java,
                    PageData.BEDTIME_STORY_INFO_CHAPTER_ID.eq(bedtimeStoryChapterData.id),
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
                            mainScope.launch {
                                completed(result)
                            }
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }
}