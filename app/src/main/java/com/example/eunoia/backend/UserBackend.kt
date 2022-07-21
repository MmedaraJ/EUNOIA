package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.SoundData
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.models.UserObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object UserBackend {
    private const val TAG = "UserBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUser(user: UserObject.User, completed: (user: UserData) -> Unit){
        scope.launch{
            Amplify.API.mutate(
                ModelMutation.create(user.data),
                { response ->
                    if(response.hasData()){
                        Log.i(TAG, "Created user: ${response.data}")
                        completed(response.data)
                    }else{
                        Log.i(TAG, response.toString())
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun getUserWithUsername(username: String, completed: (user: UserData?) -> Unit){
        scope.launch {
            Amplify.API.query(
                ModelQuery.list(UserData::class.java, UserData.USERNAME.eq(username)),
                { response ->
                    if(response.hasErrors()) Log.e(TAG, response.errors.first().message)
                    else {
                        if (!response.data.items.none()) {
                            Log.i(TAG, "Successfully gotten user with username: $username ${response.data.first()}")
                            completed(response.data.first())
                        }
                        else {
                            Log.i(TAG, "No user has the username: $username ${response.data.items}")
                            completed(null)
                        }
                    }
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }

    fun updateUser(user: UserData, completed: (user: UserData) -> Unit){
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.update(user),
                { response ->
                    if(response.hasData()) {
                        Log.i(TAG, "Successfully updated user: ${response.data}")
                        completed(response.data)
                    }
                },
                {
                    Log.i(TAG, "Error while updating user: ", it)
                }
            )
        }
    }
}