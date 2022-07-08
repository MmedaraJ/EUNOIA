package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.CommentData
import com.amplifyframework.datastore.generated.model.SoundData
import com.example.eunoia.models.CommentObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object CommentBackend {
    private const val TAG = "CommentBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createComment(comment: CommentObject.Comment, completed: (commentData: CommentData) -> Unit){
        scope.launch{
            Amplify.API.mutate(
                ModelMutation.create(comment.data),
                { response ->
                    Log.i(TAG, "Created comment $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, response.errors.first().message)
                    } else {
                        Log.i(TAG, "Created comment with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun queryCommentBasedOnSound(sound: SoundData, completed: (comment: CommentData) -> Unit) {
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(CommentData::class.java, CommentData.SOUND.eq(sound.id)),
                { response ->
                    if(response.hasErrors()){
                        Log.e(TAG, response.errors.first().message)
                    }
                    else{
                        if(response.hasData()) {
                            for (commentData in response.data) {
                                Log.i(TAG, commentData.toString())
                                completed(commentData)
                                break
                            }
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun getCommentWithID(id: String, completed: (comment: CommentData) -> Unit){
        scope.launch {
            Amplify.API.query(
                ModelQuery.get(CommentData::class.java, id),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Query results = ${(response.data as CommentData).id}")
                        completed(response.data)
                    }
                },
                { Log.e(TAG, "Query failed", it) }
            )
        }
    }
}