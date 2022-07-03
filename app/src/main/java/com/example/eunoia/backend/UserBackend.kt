package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.example.eunoia.models.UserObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object UserBackend {
    private const val TAG = "UserBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUser(user: UserObject.User){
        scope.launch{
            Amplify.API.mutate(
                ModelMutation.create(user.data),
                { response ->
                    Log.i(TAG, "Created user $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, response.errors.first().message)
                    } else {
                        Log.i(TAG, "Created user with id: " + response.data.id)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }
}