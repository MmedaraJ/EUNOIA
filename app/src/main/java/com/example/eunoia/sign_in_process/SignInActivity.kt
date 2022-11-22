package com.example.eunoia.sign_in_process

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.upload_files.UploadFilesActivity
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.theme.Blue
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.theme.EUNOIATheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class SignInActivity : ComponentActivity() {
    private val TAG = "SignIn"
    private var username: String = ""
    private var password: String = ""
    private var message: String = ""
    private val showErrorMessage = mutableStateOf(false)
    private var currentUser: MutableLiveData<UserData>? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeIsSignedIn()
        observeSetResendCode()
        setUpSignInInfo()
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignInActivityUI()
                }
            }
        }
    }

    private fun setUpSignInInfo(){
        if(!intent.getStringExtra("message").isNullOrEmpty()){
            message = intent.getStringExtra("message")!!
        }
    }

    private fun observeIsSignedIn(){
        AuthBackend.isSignedIn.observe(this) { isSignedIn ->
            Log.i(TAG, "isSignedIn changed : $isSignedIn")
            if (isSignedIn) {
                if (AuthBackend.isSignedIn.value!!) {
                    getSignedInUser{ userData ->
                        Log.i(TAG, "ij df sjfnifks  sjf sf s$userData")
                        if(userData == null){
                            createUserObject{
                                scope.launch { UserObject.setSignedInUser(UserObject.User.from(it!!)) }
                            }
                        }else{
                            setSignedInUser{
                                scope.launch { UserObject.setSignedInUser(UserObject.User.from(it!!)) }
                            }
                        }
                        if(Amplify.Auth.currentUser.username == "eunoia"){
                            val intent = Intent(this, UploadFilesActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else {
                            val intent = Intent(this, UserDashboardActivity::class.java)
                            intent.putExtra("username", Amplify.Auth.currentUser.username)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                } else {
                    Log.d(TAG, AuthBackend.isSignedIn.value.toString())
                }
            } else {
                Log.d(TAG, isSignedIn.toString())
            }
        }
    }

    private fun getSignedInUser(completed: (userData: UserData?) -> Unit){
        UserBackend.getUserWithUsername(Amplify.Auth.currentUser.username){userData ->
            completed(userData)
        }
    }

    private fun createUserObject(completed: (userData: UserData?) -> Unit){
        AuthBackend.getAuthUserAttributes {
            val user = UserObject.User(
                UUID.randomUUID().toString(),
                username,
                Amplify.Auth.currentUser.userId,
                "",
                "",
                "",
                it[2].value,
                "",
                "",
                "",
                "",
                "",
                "",
                true,
                "rookie"
            )
            UserBackend.createUser(user){ userData ->
                globalViewModel!!.currentUser = userData
                Log.i(TAG, "bdid mfidf dkfjd fdk ${globalViewModel!!.currentUser}")
                completed(userData)
            }
        }
    }

    private fun observeSetResendCode(){
        AuthBackend.resendCode.observe(this) { resendCode ->
            // update UI
            Log.i(TAG, "resendCode changed : $resendCode")
            if (resendCode) {
                if (AuthBackend.resendCode.value!!) {
                    val intent = Intent(this, SignUpConfirmationCodeActivity::class.java)
                    if (username.isNotEmpty()) {
                        intent.putExtra("username", username)
                        startActivity(intent)
                    }else{
                        hasValidInputs()
                    }
                } else {
                    Log.d(TAG, AuthBackend.resendCode.value.toString())
                }
            } else {
                Log.d(TAG, resendCode.toString())
            }
        }
    }

    private fun setSignedInUser(completed: (userData: UserData?) -> Unit){
        UserBackend.getUserWithUsername(Amplify.Auth.currentUser.username){
            globalViewModel!!.currentUser = it
            Log.i(TAG, "bdid mfidf dkfjd fdk ${globalViewModel!!.currentUser}")
            completed(it)
        }
    }

    @Composable
    fun SignInActivityUI() {
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EunoiaLogo(
                dimensionResource(id = R.dimen.big_logo_w),
                dimensionResource(id = R.dimen.big_logo_h),
                0,
                0
            ){}
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
            NormalText(
                stringResource(id = R.string.eunoia_definition),
                MaterialTheme.colors.primary,
                12,
                0,
                0
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            username = standardOutlinedTextInput(211, 50, "username", 55)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
            password = standardPasswordOutlinedTextInput("password", 55)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.four_sp)))
            ClickableNormalText(
                text = stringResource(id = R.string.forgot_password),
                color = MaterialTheme.colors.primary,
                12,
                -50,
                0
            ) { forgotPasswordListener(context!!) }
            Spacer(modifier = Modifier.height(12.dp))
            if(showErrorMessage.value || AuthBackend.signInError.value!="") ErrorMessage()
            Spacer(modifier = Modifier.height(12.dp))
            if(message.isNotEmpty()) SuccessMessage(message)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            StandardBlueButton(stringResource(id = R.string.sign_in)){hasValidInputs()}
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-20).dp)
        ) {
            ClickableNormalText(
                text = stringResource(id = R.string.sign_up_no_account),
                color = Blue,
                12,
                0,
                0
            ) { signUpListener(context) }
        }
    }

    private fun signUpListener(context: Context){
        context.startActivity(Intent(context, SignUpActivity::class.java))
    }

    private fun forgotPasswordListener(context: Context){
        context.startActivity(Intent(context, UsernameResetPwActivity::class.java))
    }

    private fun hasValidInputs() {
        if (username.isEmpty() or password.isEmpty()) {
            showErrorMessage.value = true
        }else {
            showErrorMessage.value = false
            AuthBackend.signInError.value = ""
            AuthBackend.signIn(
                username,
                password
            )
        }
    }

    @Composable
    fun SuccessMessage(message: String){
        NormalText(
            text = message,
            Color.Green,
            12,
            0,
            0
        )
    }

    /**
     * Show any error message
     */
    @Composable
    fun ErrorMessage(){
        if(showErrorMessage.value){
            ErrorTextSize12(text = stringResource(id = R.string.empty_sign_in_up_error))
        }else{
            if(AuthBackend.signInError.value!="") {
                ErrorTextSize12(text = AuthBackend.signInError.value)
            }
        }
    }

    @Preview(
        showBackground = true,
        name = "Light mode"
    )
    @Preview(
        showBackground = true,
        name = "Dark mode",
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    fun SignInActivityPreview() {
        EUNOIATheme {
            SignInActivityUI()
        }
    }
}