package com.example.eunoia.sign_in_process

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
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.theme.EUNOIATheme

class UsernameResetPwActivity : ComponentActivity() {
    private val TAG = "UsernameResetPwActivity"
    private lateinit var username: String
    private val showErrorMessage = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeResetPassword()
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UsernameResetPwActivityUI()
                }
            }
        }
    }

    private fun observeResetPassword(){
        AuthBackend.resetPassword.observe(this) { resetPassword ->
            // update UI
            Log.i(TAG, "resetPassword changed : $resetPassword")
            if (resetPassword) {
                if (AuthBackend.resetPassword.value!!) {
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun UsernameResetPwActivityUI() {
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
            username = standardOutlinedTextInput(211, 50, "username", 55)
            Spacer(modifier = Modifier.height(12.dp))
            if(showErrorMessage.value || AuthBackend.resetPasswordError.value!="") ErrorMessage()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            StandardBlueButton(stringResource(id = R.string.request_code)){hasValidInputs()}
        }
    }

    private fun hasValidInputs() {
        if (username.isEmpty()) {
            showErrorMessage.value = true
        }else {
            showErrorMessage.value = false
            AuthBackend.resetPasswordError.value = ""
            AuthBackend.resetPassword(
                username
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
            if(AuthBackend.resetPasswordError.value!="") {
                ErrorTextSize12(text = AuthBackend.resetPasswordError.value)
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
            UsernameResetPwActivity()
        }
    }
}