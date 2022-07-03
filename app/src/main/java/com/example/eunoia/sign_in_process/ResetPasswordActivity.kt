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
import com.example.eunoia.ui.theme.Blue
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.theme.EUNOIATheme

class ResetPasswordActivity : ComponentActivity() {
    private val TAG = "ResetPasswordActivity"
    private lateinit var code: String
    private lateinit var username: String
    private lateinit var new_password: String
    private val showErrorMessage = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeConfirmResetPassword()
        username = intent.getStringExtra("username")!!
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ResetPasswordActivityUI()
                }
            }
        }
    }

    private fun observeConfirmResetPassword(){
        AuthBackend.confirmResetPassword.observe(this) { confirmResetPassword ->
            // update UI
            Log.i(TAG, "confirmResetPassword changed : $confirmResetPassword")
            if (confirmResetPassword) {
                if (AuthBackend.confirmResetPassword.value!!) {
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.putExtra("message", "Password successfully updated")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun ResetPasswordActivityUI() {
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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
            new_password = standardPasswordOutlinedTextInput("new password", 43)
            Spacer(modifier = Modifier.height(12.dp))
            if(showErrorMessage.value || AuthBackend.confirmResetPasswordError.value!="") ErrorMessage()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.forty_sp)))
            StandardBlueButton(stringResource(id = R.string.reset_password)){hasValidInputs()}
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
        AuthBackend.resetPassword(username)
    }

    private fun hasValidInputs() {
        if (code.isEmpty() or new_password.isEmpty()) {
            showErrorMessage.value = true
        }else {
            showErrorMessage.value = false
            AuthBackend.confirmResetPasswordError.value = ""
            AuthBackend.confirmResetPassword(
                code, new_password
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
            if(AuthBackend.confirmResetPasswordError.value!="") {
                ErrorTextSize12(text = AuthBackend.confirmResetPasswordError.value)
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
            ResetPasswordActivityUI()
        }
    }
}