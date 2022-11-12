package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.BedtimeStoryChapterObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object BedtimeStoryChapterBackend {
    private const val TAG = "BedtimeStoryChapterBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val mainScope = CoroutineScope(Job() + Dispatchers.Main)

    fun createBedtimeStoryInfoChapter(
        bedtimeStoryChapter: BedtimeStoryChapterObject.BedtimeStoryChapter,
        completed: (bedtimeStoryChapterData: BedtimeStoryInfoChapterData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(bedtimeStoryChapter.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create bedtimeStoryChapter ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created bedtimeStoryChapter with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun queryBedtimeStoryChapterBasedOnBedtimeStory(
        bedtimeStoryData: BedtimeStoryInfoData,
        completed: (bedtimeStoryChapters: MutableList<BedtimeStoryInfoChapterData>) -> Unit
    ) {
        val result = mutableListOf<BedtimeStoryInfoChapterData>()
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(
                    BedtimeStoryInfoChapterData::class.java,
                    BedtimeStoryInfoChapterData.BEDTIME_STORY_INFO.eq(bedtimeStoryData.id)
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (bedtimeStory in response.data) {
                                if(bedtimeStory != null) {
                                    Log.i(TAG, bedtimeStory.toString())
                                    result.add(bedtimeStory)
                                }
                            }
                            mainScope.launch {
                                //val mutableResult = result.toMutableList()
                                result.sortBy {
                                    it.chapterNumber
                                }
                                Log.i(TAG, "Result sorted = $result")
                                completed(result)
                            }
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun queryBedtimeStoryChapterBasedOnId(
        bedtimeStoryInfoChapterDataId: String,
        completed: (bedtimeStoryChapters: List<BedtimeStoryInfoChapterData>) -> Unit
    ) {
        val result = mutableListOf<BedtimeStoryInfoChapterData>()
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(
                    BedtimeStoryInfoChapterData::class.java,
                    BedtimeStoryInfoChapterData.ID.eq(bedtimeStoryInfoChapterDataId)
                ),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            Log.i(TAG, "{response.data} ${response.data}")
                            for (chapter in response.data) {
                                if(chapter != null) {
                                    Log.i(TAG, chapter.toString())
                                    result.add(chapter)
                                }else{
                                    Log.i(TAG, "chapter.toString() is null")
                                }
                            }
                            mainScope.launch {
                                Log.i(TAG, "Completed = $result")
                                completed(result)
                            }
                        }else{
                            Log.i(TAG, "{response.no.data} $response")
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun updateChapter(
        bedtimeStoryInfoChapterData: BedtimeStoryInfoChapterData,
        completed: (bedtimeStoryInfoChapterData: BedtimeStoryInfoChapterData) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(bedtimeStoryInfoChapterData),
                { response ->
                    Log.i(TAG, "Updated $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from update BedtimeStoryInfoChapterData ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Updated BedtimeStoryInfoChapterData with id: " + response.data.id)
                        mainScope.launch {
                            completed(response.data)
                        }
                    }
                },
                { error -> Log.e(TAG, "Update failed", error) }
            )
        }
    }

    fun deleteChapter(
        bedtimeStoryInfoChapterData: BedtimeStoryInfoChapterData,
        completed: (successful: Boolean) -> Unit
    ){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.delete(bedtimeStoryInfoChapterData),
                { response ->
                    Log.i(TAG, "Deleted $response")
                    mainScope.launch {
                        completed(true)
                    }
                },
                { error ->
                    Log.e(TAG, "Deletion failed", error)
                    mainScope.launch {
                        completed(false)
                    }
                }
            )
        }
    }
}