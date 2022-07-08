package com.example.eunoia.sign_in_process

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.core.Amplify
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.dashboard.home.UserDashboardActivity
import com.example.eunoia.models.UserObject
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.theme.EUNOIATheme
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class HelloUserActivity : ComponentActivity() {
    private var username: String = ""
    private var first_name: String = ""
    private var last_name: String = ""
    private var email: String = ""
    private val TAG = "HelloUserActivity"
    private val imageSelected = mutableStateOf(false)
    private val imageStoredS3 = mutableStateOf(false)
    private val imageRetrievedS3 = mutableStateOf<Bitmap?>(null)
    private var userImagePath : String? = null
    private var userImageKey : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeImageStoredOnS3()
        username = intent.getStringExtra("username")!!
        first_name = intent.getStringExtra("first_name")!!
        last_name = intent.getStringExtra("last_name")!!
        email = intent.getStringExtra("email")!!
        setContent {
            EUNOIATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HelloUserActivityUI()
                }
            }
        }
    }

    private fun observeImageStoredOnS3(){
        AuthBackend.imageStoredOnS3.observe(this) { imageStoredOnS3 ->
            // update UI
            Log.i(TAG, "imageStoredOnS3 changed : $imageStoredOnS3")
            if (imageStoredOnS3) {
                if (AuthBackend.imageStoredOnS3.value!!) {
                    imageStoredS3.value = true
                } else {
                    Log.d(TAG, AuthBackend.imageStoredOnS3.value.toString())
                }
            } else {
            }
        }
    }

    @Composable
    fun HelloUserActivityUI() {
        val context = LocalContext.current
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = 40.dp)
        ) {
            EunoiaLogo(
                dimensionResource(id = R.dimen.logo_top_width_dimen),
                dimensionResource(id = R.dimen.logo_top_height_dimen),
                0,
                0
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 35.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NormalText(
                "Hi $first_name!",
                MaterialTheme.colors.primary,
                24,
                0,
                0
            )
            NormalText(
                stringResource(id = R.string.ready),
                MaterialTheme.colors.primary,
                16,
                0,
                0
            )
            Spacer(modifier = Modifier.height(24.dp))
            var imageUri by remember {mutableStateOf<Uri?>(null)}
            val bitmap = remember {mutableStateOf<Bitmap?>(null)}
            val launcher = rememberLauncherForActivityResult(contract =
            ActivityResultContracts.GetContent()) { uri: Uri? ->
                imageUri = uri
                imageSelected.value = true
            }
            if(!imageSelected.value){
                ProfilePictureDefault(
                    dimensionResource(id = R.dimen.profile_picture_width_dimen_big),
                    dimensionResource(id = R.dimen.profile_picture_height_dimen_big),
                    0,
                    0,
                    1.0
                ){}
            }else{
                // read the stream to store to a file
                var imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)

                val tempFile = File.createTempFile("image", ".image")
                copyStreamToFile(imageStream!!, tempFile)

                // store the path to create a note
                userImagePath = tempFile.absolutePath
                userImageKey = "$username - profile picture"

                //store user image in aws s3
                //AuthBackend.storeImage(userImagePath!!, userImageKey!!)

                if(imageStoredS3.value) {
                    //Update user profile image on amplify
                    Amplify.Auth.updateUserAttribute(
                        AuthUserAttribute(AuthUserAttributeKey.picture(), userImagePath),
                        { Log.i("AuthDemo", "Updated user attribute = $it") },
                        { Log.e("AuthDemo", "Failed to update user attribute.", it) }
                    )

                    //retrieve user image
                    /*AuthBackend.retrieveImage(userImageKey!!) {
                        imageRetrievedS3.value = it
                    }*/
                    if (imageRetrievedS3.value != null) {
                        ProfilePictureEdited(
                            dimensionResource(id = R.dimen.profile_picture_width_dimen_big),
                            dimensionResource(id = R.dimen.profile_picture_height_dimen_big),
                            imageRetrievedS3.value!!.asImageBitmap(),
                            0,
                            0
                        ){}
                    } else {
                        ProfilePictureDefault(
                            dimensionResource(id = R.dimen.profile_picture_width_dimen_big),
                            dimensionResource(id = R.dimen.profile_picture_height_dimen_big),
                            0,
                            0,
                            1.0
                        ){}
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.eight_sp)))

            ClickableNormalText(
                text = stringResource(id = R.string.edit_profile_picture),
                color = MaterialTheme.colors.primary,
                12,
                0,
                0
            ) {
                launcher.launch("image/*")
            }
            Spacer(modifier = Modifier.height(24.dp))
            NormalText(
                stringResource(id = R.string.start),
                MaterialTheme.colors.primary,
                16,
                0,
                0
            )
            Spacer(modifier = Modifier.height(4.dp))
            IconButton(
                onClick = {
                    goToDashboard(context)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.East,
                    contentDescription = "Get Started",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
    }

    private fun goToDashboard(context: Context){
        context.startActivity(Intent(context, UserDashboardActivity::class.java))
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
            HelloUserActivityUI()
        }
    }
}