package com.example.eunoia.backend

import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.*
import com.example.eunoia.models.PresetObject
import com.example.eunoia.models.UserObject
import com.example.eunoia.models.UserPresetObject
import com.example.eunoia.ui.navigation.globalViewModel_
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

object UserPresetBackend {
    private const val TAG = "UserPresetBackend"
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun createUserPreset(
        userPresetModel: UserPresetObject.UserPresetModel,
        completed: (userPreset: UserPreset) -> Unit
    ) {
        scope.launch {
            Amplify.API.mutate(
                ModelMutation.create(userPresetModel.data),
                { response ->
                    Log.i(TAG, "Created $response")
                    if (response.hasErrors()) {
                        Log.e(TAG, "Error from create userPresetModel ${response.errors.first().message}")
                    } else {
                        Log.i(TAG, "Created userPresetModel with id: " + response.data.id)
                        completed(response.data)
                    }
                },
                { error -> Log.e(TAG, "Create failed", error) }
            )
        }
    }

    fun createUserPresetObject(presetData: PresetData, completed: (userPreset: UserPreset) -> Unit){
        val userPresetModel = UserPresetObject.UserPresetModel(
            UUID.randomUUID().toString(),
            UserObject.User.from(globalViewModel_!!.currentUser!!),
            PresetObject.Preset.from(presetData),
        )
        createUserPreset(userPresetModel){
            completed(it)
        }
    }

    /*fun queryUserPresetBasedOnUserAndSound(
        userData: UserData,
        soundData: SoundData,
        completed: (userPreset: List<UserPreset?>) -> Unit
    ) {
        scope.launch {
            val userPresetList = mutableListOf<UserPreset?>()
            Amplify.API.query(
                ModelQuery.list(
                    UserPreset::class.java,
                    UserPreset.USER_DATA.eq(userData.id)
                        .and(UserPreset.PRESET_DATA.eq(so))
                ),
                { response ->
                    if(response.hasData()) {
                        for (userPresetData in response.data) {
                            if(userPresetData.PresetData.approvalStatus == PresetApprovalStatus.APPROVED){
                                Log.i(TAG, userPresetData.toString())
                                userPresetList.add(userPresetData)
                            }
                        }
                    }
                    completed(userPresetList)
                },
                { error -> Log.e(TAG, "Query failure", error) }
            )
        }
    }*/
}