package com.example.eunoia.backend

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.datastore.generated.model.NoteData
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.example.eunoia.models.UserData

object Backend {
    private const val TAG = "AuthQuickstart"
    private const val BACKEND_TAG = "Backend"
    private val _isSignedUp = MutableLiveData(false)
    var isSignedUp: LiveData<Boolean> = _isSignedUp
    private val _isSignedIn = MutableLiveData(false)
    var isSignedIn: LiveData<Boolean> = _isSignedIn
    private val _signUpConfirmed = MutableLiveData(false)
    var signUpConfirmed: LiveData<Boolean> = _signUpConfirmed
    private val _resetPassword = MutableLiveData(false)
    var resetPassword: LiveData<Boolean> = _resetPassword
    private val _confirmResetPassword = MutableLiveData(false)
    var confirmResetPassword: LiveData<Boolean> = _confirmResetPassword
    private val _resendCode = MutableLiveData(false)
    var resendCode: LiveData<Boolean> = _resendCode
    private val _imageStoredOnS3 = MutableLiveData(false)
    var imageStoredOnS3: LiveData<Boolean> = _imageStoredOnS3
    val signInError = mutableStateOf("")
    val signUpError = mutableStateOf("")
    val signUpConfirmationError = mutableStateOf("")
    val resetPasswordError = mutableStateOf("")
    val confirmResetPasswordError = mutableStateOf("")

    fun initialize(applicationContext: Context) : Backend {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(applicationContext)
            Log.i(TAG, "Initialized Amplify")
        } catch (e: AmplifyException) {
            Log.e(TAG, "Could not initialize Amplify", e)
        }

        Log.i(BACKEND_TAG, "registering hub event")

        // listen to auth event
        Amplify.Hub.subscribe(HubChannel.AUTH) { hubEvent: HubEvent<*> ->
            when (hubEvent.name) {
                InitializationStatus.SUCCEEDED.toString() -> {
                    Log.i(BACKEND_TAG, "Amplify successfully initialized")
                }
                InitializationStatus.FAILED.toString() -> {
                    Log.i(BACKEND_TAG, "Amplify initialization failed")
                }
                else -> {
                    when (AuthChannelEventName.valueOf(hubEvent.name)) {
                        AuthChannelEventName.SIGNED_IN -> {
                            updateUserData(true)
                            Log.i(BACKEND_TAG, "HUB : SIGNED_IN")
                        }
                        AuthChannelEventName.SIGNED_OUT -> {
                            updateUserData(false)
                            Log.i(BACKEND_TAG, "HUB : SIGNED_OUT")
                        }
                        else -> Log.i(BACKEND_TAG, """HUB EVENT:${hubEvent.name}""")
                    }
                }
            }
        }

        Log.i(BACKEND_TAG, "retrieving session status")

        // is user already authenticated (from a previous execution) ?
        Amplify.Auth.fetchAuthSession(
            { result ->
                Log.i(BACKEND_TAG, result.toString())
                val cognitoAuthSession = result as AWSCognitoAuthSession
                // update UI
                updateUserData(cognitoAuthSession.isSignedIn)
                when (cognitoAuthSession.identityId.type) {
                    AuthSessionResult.Type.SUCCESS ->  Log.i(BACKEND_TAG, "IdentityId: " + cognitoAuthSession.identityId.value)
                    AuthSessionResult.Type.FAILURE -> Log.i(BACKEND_TAG, "IdentityId not present because: " + cognitoAuthSession.identityId.error.toString())
                }
            },
            { error -> Log.i(BACKEND_TAG, error.toString()) }
        )
        return this
    }

    private fun updateUserData(withSignedInStatus : Boolean) {
        com.example.eunoia.models.UserData.setSignedIn(withSignedInStatus)
        /*val notes = UserData.notes().value
        val isEmpty = notes?.isEmpty() ?: false

        // query notes when signed in and we do not have Notes yet
        if (withSignedInStatus && isEmpty ) {
            this.queryNotes()
        } else {
            UserData.resetNotes()
        }*/
    }

    fun setSignedUp(newValue : Boolean) {
        _isSignedUp.postValue(newValue)
    }

    fun setSignedIn(newValue : Boolean) {
        _isSignedIn.postValue(newValue)
    }

    fun setSignUpConfirmed(newValue : Boolean) {
        _signUpConfirmed.postValue(newValue)
    }

    fun setResetPassword(newValue : Boolean) {
        _resetPassword.postValue(newValue)
    }

    fun setResendCode(newValue : Boolean) {
        _resendCode.postValue(newValue)
    }

    fun setConfirmResetPassword(newValue : Boolean) {
        _confirmResetPassword.postValue(newValue)
    }

    fun signOut() {
        Amplify.Auth.signOut(
            { Log.i(TAG, "Signed out successfully") },
            { Log.e(TAG, "Sign out failed", it) }
        )
    }

    fun signIn(username: String, password: String) {
        Amplify.Auth.signIn(
            username,
            password,
            { result ->
                if (result.isSignInComplete) {
                    Log.i(TAG, "Sign in succeeded")
                    setSignedIn(true)
                } else {
                    Log.i(TAG, result.toString())
                }
            }
        ) {
            Log.e(TAG, "Failed to sign in", it)
            if(it.message.toString() == "User not confirmed in the system."){
                resendSignUpCode(username)
            }else {
                signInError.value = it.message.toString()
            }
        }
    }

    fun signUp(first_name: String,
               last_name: String,
               email: String,
               username: String,
               password: String){
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.givenName(), first_name)
            .userAttribute(AuthUserAttributeKey.familyName(), last_name)
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        Amplify.Auth.signUp(username, password, options,
            {
                Log.i(TAG, "Sign up succeeded: $it")
                setSignedUp(true)
            },
            {
                Log.e (TAG, "Sign up failed", it)
                signUpError.value = it.message.toString()
            }
        )
    }

    fun resendSignUpCode(username: String){
        Amplify.Auth.resendSignUpCode(username,
            {
                setResendCode(true)
            },
            {
                Log.e(TAG, "Failed to resend sign up code", it)
            }
        )
    }

    fun confirmSignUp(username: String?, code: String){
        Amplify.Auth.confirmSignUp(
            username!!, code,
            { result ->
                if (result.isSignUpComplete) {
                    Log.i(TAG, "Confirm signUp succeeded")
                    setSignUpConfirmed(true)
                } else {
                    Log.i(TAG,"Confirm sign up not complete")
                }
            },
            {
                Log.e(TAG, "Failed to confirm sign up", it)
                signUpConfirmationError.value = it.message.toString()
            }
        )
    }

    fun resetPassword(username: String){
        Amplify.Auth.resetPassword(username,
            {
                Log.i("AuthQuickstart", "Password reset OK: $it")
                setResetPassword(true)
            },
            {
                Log.e("AuthQuickstart", "Password reset failed", it)
                resetPasswordError.value = it.message.toString()
            }
        )
    }

    fun confirmResetPassword(code: String, new_password: String){
        Amplify.Auth.confirmResetPassword(new_password, code,
            {
                Log.d(TAG, code)
                Log.i("AuthQuickstart", "New password confirmed")
                setConfirmResetPassword(true)
            },
            {
                Log.d(TAG, code)
                Log.e("AuthQuickstart", "Failed to confirm password reset", it)
                confirmResetPasswordError.value = it.message.toString()
            }
        )
    }

    //Notes
    fun queryNotes() {
        Log.i(TAG, "Querying notes")

        Amplify.API.query(
            ModelQuery.list(NoteData::class.java),
            { response ->
                Log.i(TAG, "Queried")
                for (noteData in response.data) {
                    Log.i(TAG, noteData.name)
                    // TODO should add all the notes at once instead of one by one (each add triggers a UI refresh)
                    UserData.addNote(UserData.Note.from(noteData))
                }
            },
            { error -> Log.e(TAG, "Query failure", error) }
        )
    }

    fun createNote(note : UserData.Note) {
        Log.i(TAG, "Creating notes")

        Amplify.API.mutate(
            ModelMutation.create(note.data),
            { response ->
                Log.i(TAG, "Created")
                if (response.hasErrors()) {
                    Log.e(TAG, response.errors.first().message)
                } else {
                    Log.i(TAG, "Created Note with id: " + response.data.id)
                }
            },
            { error -> Log.e(TAG, "Create failed", error) }
        )
    }

    fun deleteNote(note : UserData.Note?) {

        if (note == null) return

        Log.i(TAG, "Deleting note $note")

        Amplify.API.mutate(
            ModelMutation.delete(note.data),
            { response ->
                Log.i(TAG, "Deleted")
                if (response.hasErrors()) {
                    Log.e(TAG, response.errors.first().message)
                } else {
                    Log.i(TAG, "Deleted Note $response")
                }
            },
            { error -> Log.e(TAG, "Delete failed", error) }
        )
    }
}