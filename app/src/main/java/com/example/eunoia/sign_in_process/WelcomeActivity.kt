package com.example.eunoia.sign_in_process

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amplifyframework.core.Amplify
import com.example.eunoia.R
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.dashboard.upload_files.UploadFilesActivity
import com.example.eunoia.models.UserData
import com.example.eunoia.ui.components.EunoiaLogo
import com.example.eunoia.ui.components.NormalText
import com.example.eunoia.ui.components.StandardBlueButton
import com.example.eunoia.ui.theme.EUNOIATheme


class WelcomeActivity : ComponentActivity() {
    private val TAG = "WelcomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeIsSignedIn()
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WelcomeActivityUI()
                }
            }
        }
    }

    private fun observeIsSignedIn(){
        UserData.isSignedIn.observe(this) { isSignedIn ->
            // update UI
            Log.i(TAG, "isSignedIn changed : $isSignedIn")
            if (isSignedIn) {
                Log.i(TAG, "user val : ${UserData.isSignedIn.value!!}")
                if (UserData.isSignedIn.value!!) {
                    if(Amplify.Auth.currentUser != null) {
                        if (Amplify.Auth.currentUser.username == "eunoia") {
                            val intent = Intent(this, UploadFilesActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, UserDashboardActivity::class.java)
                            intent.putExtra("username", Amplify.Auth.currentUser.username)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WelcomeActivityUI() {
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            NormalText(
                stringResource(id = R.string.welcome_to),
                MaterialTheme.colors.primary,
                12,
                0,
                0
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))
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
            StandardBlueButton(stringResource(id = R.string.sign_in)) { welcomeSignInListener(context) }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.four_sp)))
            StandardBlueButton(stringResource(id = R.string.sign_up)) { welcomeSignUpListener(context) }
        }
    }

    private fun welcomeSignInListener(context: Context){
        context.startActivity(Intent(context, SignInActivity::class.java))
    }

    private fun welcomeSignUpListener(context: Context){
        context.startActivity(Intent(context, SignUpActivity::class.java))
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
    fun WelcomeActivityPreview() {
        EUNOIATheme {
            WelcomeActivityUI()
        }
    }
}