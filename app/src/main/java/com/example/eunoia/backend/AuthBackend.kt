package com.example.eunoia.backend

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.AmplifyException
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.example.eunoia.models.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object AuthBackend {
    private const val TAG = "AuthQuickstart"
    private const val BACKEND_TAG = "AuthBackend"
    private val _isSignedUp = MutableLiveData(false)
    var isSignedUp: LiveData<Boolean> = _isSignedUp
    private val _isSignedIn = MutableLiveData(false)
    var isSignedIn: LiveData<Boolean> = _isSignedIn
    private val _isSignedOut = MutableLiveData(false)
    var isSignedOut: LiveData<Boolean> = _isSignedOut
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
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun initialize(applicationContext: Context, application: Application) : AuthBackend {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSPinpointAnalyticsPlugin(application))

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
        UserData.setSignedIn(withSignedInStatus)
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

    fun setSignedOut(newValue : Boolean) {
        _isSignedOut.postValue(newValue)
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
            {
                Log.i(TAG, "Signed out successfully")
                setSignedOut(true)
                setSignedIn(false)
            },
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
                    setSignedOut(false)
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
                updateUserData(true)
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
                Log.i(TAG, "New password confirmed")
                setConfirmResetPassword(true)
            },
            {
                Log.d(TAG, code)
                Log.e(TAG, "Failed to confirm password reset", it)
                confirmResetPasswordError.value = it.message.toString()
            }
        )
    }

    fun getAuthUserAttributes(completed: (attr: MutableList<AuthUserAttribute>) -> Unit){
        Amplify.Auth.fetchUserAttributes(
            {
                Log.i(TAG, "User attributes = $it")
                completed(it)
            },
            { Log.e(TAG, "Failed to fetch user attributes", it) }
        )
    }
}