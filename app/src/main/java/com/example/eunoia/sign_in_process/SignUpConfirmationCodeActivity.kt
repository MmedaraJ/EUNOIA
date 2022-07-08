package com.example.eunoia.sign_in_process

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amplifyframework.core.Amplify
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.dashboard.upload_files.UploadFilesActivity
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.theme.Blue
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.theme.EUNOIATheme
import java.util.*

class SignUpConfirmationCodeActivity : ComponentActivity() {
    private val TAG = "SignUpConfirmationCode"
    private lateinit var code: String
    private lateinit var first_name: String
    private lateinit var last_name: String
    private lateinit var email: String
    private lateinit var username: String
    private val showErrorMessage = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSignUpConfirmed()
        first_name = intent.getStringExtra("first_name")!!
        last_name = intent.getStringExtra("last_name")!!
        email = intent.getStringExtra("email")!!
        username = intent.getStringExtra("username")!!
        setContent {
            EUNOIATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUpConfirmationCodeActivityUI()
                }
            }
        }
    }

    private fun observeSignUpConfirmed(){
        AuthBackend.signUpConfirmed.observe(this) { signUpConfirmed ->
            // update UI
            Log.i(TAG, "isSignedUp changed : $signUpConfirmed")
            if (signUpConfirmed) {
                if(username == "eunoia"){
                    val intent = Intent(this, UploadFilesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }else {
                    //createUserObject()
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("first_name", first_name)
                    intent.putExtra("last_name", last_name)
                    intent.putExtra("email", email)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun SignUpConfirmationCodeActivityUI() {
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
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
            NormalText(
                stringResource(id = R.string.eunoia_definition),
                MaterialTheme.colors.primary,
                12,
                0,
                0
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            code = standardOutlinedTextInput(211, 50, "code", 73)
            Spacer(modifier = Modifier.height(12.dp))
            NormalText(
                "A code has been sent to $email",
                MaterialTheme.colors.primary,
                12,
                0,
                0
            )
            Spacer(modifier = Modifier.height(12.dp))
            if(showErrorMessage.value || AuthBackend.signUpConfirmationError.value!="") ErrorMessage()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            StandardBlueButton(stringResource(id = R.string.verify)){hasValidInputs()}
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-20).dp)
        ) {
            ClickableNormalText(
                text = stringResource(id = R.string.resend_code),
                color = Blue,
                12,
                0,
                0
            ) { resendCodeListener() }
        }
    }

    private fun resendCodeListener(){
        AuthBackend.resendSignUpCode(username)
    }

    private fun hasValidInputs() {
        if (code.isEmpty()) {
            showErrorMessage.value = true
        }else {
            showErrorMessage.value = false
            AuthBackend.signUpConfirmationError.value = ""
            AuthBackend.confirmSignUp(
                username, code
            )
        }
    }

    /**
     * Show any error message
     */
    @Composable
    fun ErrorMessage(){
        if(showErrorMessage.value){
            ErrorTextSize12(text = stringResource(id = R.string.empty_sign_in_up_error))
        }else{
            if(AuthBackend.signUpConfirmationError.value!="") {
                ErrorTextSize12(text = AuthBackend.signUpConfirmationError.value)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        EUNOIATheme {
            SignUpConfirmationCodeActivityUI()
        }
    }
}