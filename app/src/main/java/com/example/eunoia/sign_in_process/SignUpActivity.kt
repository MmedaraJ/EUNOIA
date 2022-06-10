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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eunoia.R
import com.example.eunoia.backend.Backend
import com.example.eunoia.ui.theme.Blue
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.theme.EUNOIATheme

class SignUpActivity : ComponentActivity() {
    private val TAG = "SignUp"
    private lateinit var first_name: String
    private lateinit var last_name: String
    private lateinit var email: String
    private lateinit var username: String
    private lateinit var password: String
    private val showErrorMessage = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeIsSignedUp()
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUpActivityUI()
                }
            }
        }
    }

    private fun observeIsSignedUp(){
        Backend.isSignedUp.observe(this) { isSignedUp ->
            // update UI
            Log.i(TAG, "isSignedUp changed : $isSignedUp")
            if (isSignedUp) {
                val intent = Intent(this, SignUpConfirmationCodeActivity::class.java)
                intent.putExtra("first_name", first_name)
                intent.putExtra("last_name", last_name)
                intent.putExtra("email", email)
                intent.putExtra("username", username)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    @Composable
    fun SignUpActivityUI() {
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
            first_name = standardOutlinedTextInput(211, 50, "first name", 55)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
            last_name = standardOutlinedTextInput(211, 50, "last name", 56)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.four_sp)))
            email = standardOutlinedTextInput(211, 50, "email", 66)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
            username = standardOutlinedTextInput(211, 50, "username", 55)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.four_sp)))
            password = standardPasswordOutlinedTextInput("password", 56)
            Spacer(modifier = Modifier.height(12.dp))
            if(showErrorMessage.value || Backend.signUpError.value!="") ErrorMessage()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            StandardBlueButton(stringResource(id = R.string.sign_up)){hasValidInputs()}
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-20).dp)
        ) {
            ClickableNormalText(
                text = stringResource(id = R.string.sign_in_yes_account),
                color = Blue,
                12,
                0,
                0
            ) { signInListener(context) }
        }
    }

    private fun signInListener(context: Context){
        context.startActivity(Intent(context, SignInActivity::class.java))
    }

    private fun hasValidInputs() {
        if (first_name.isEmpty() or
            last_name.isEmpty() or
            email.isEmpty() or
            username.isEmpty() or
            password.isEmpty()) {
            showErrorMessage.value = true
        }else {
            showErrorMessage.value = false
            Backend.signUpError.value = ""
            Backend.signUp(
                first_name, last_name, email, username, password
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
            if(Backend.signUpError.value!="") {
                ErrorTextSize12(text = Backend.signUpError.value)
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
            SignUpActivityUI()
        }
    }
}