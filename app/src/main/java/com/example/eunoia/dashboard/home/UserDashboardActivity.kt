package com.example.eunoia.dashboard.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.UserData
import com.example.eunoia.R
import com.example.eunoia.backend.AuthBackend
import com.example.eunoia.backend.UserBackend
import com.example.eunoia.models.UserObject
import com.example.eunoia.sign_in_process.SignInActivity
import com.example.eunoia.ui.theme.Grey
import com.example.eunoia.ui.theme.White
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.MultiBottomNavApp
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDashboardActivity : ComponentActivity() {
    private val TAG = "UserDashboardActivity"
    private val _currentUser = MutableLiveData<UserData>(null)
    var currentUser: LiveData<UserData> = _currentUser
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeCurrentUserChanged()
        setSignedInUser()
        observeIsSignedOut()
        setContent {
            MultiBottomNavApp()
        }
    }

    fun setCurrentUser(newValue: UserData) {
        _currentUser.postValue(newValue)
        Log.i(TAG, "currentUser changed : $_currentUser")
    }

    private fun observeCurrentUserChanged(){
        currentUser.observe(this) { currentUser ->
            Log.i(TAG, "currentUser changed : $currentUser")
            if (currentUser != null) {
                UserObject.setSignedInUser(UserObject.User.from(currentUser))
            }
        }
    }

    private fun setSignedInUser(){
        UserBackend.getUserWithUsername(Amplify.Auth.currentUser.username){
            if(it != null)
            setCurrentUser(it)
        }
    }

    private fun observeIsSignedOut(){
        AuthBackend.isSignedOut.observe(this) { isSignedOut ->
            // update UI
            Log.i(TAG, "isSignedOut changed : $isSignedOut")
            if (isSignedOut) {
                if (AuthBackend.isSignedOut.value!!) {
                    Log.d(TAG, AuthBackend.isSignedOut.value.toString())
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Log.d(TAG, AuthBackend.isSignedOut.value.toString())
                }
            } else {
                Log.d(TAG, isSignedOut.toString())
            }
        }
    }
}

@Composable
fun UserDashboardActivityUI(navController: NavHostController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            introTitle,
            options,
            favorite_routine_title,
            emptyRoutine,
            articles_title,
            articles,
            endSpace
        ) = createRefs()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ){
            ProfilePictureHeader({}, {navController.navigate(Screen.Settings.screen_route)})
        }
        Column(
            modifier = Modifier
                .constrainAs(introTitle){
                    top.linkTo(header.bottom, margin = 20.dp)
                }
        ){
            NormalText(
                text = stringResource(id = R.string.explore),
                color = Grey,
                10,
                xOffset = 6,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier.constrainAs(options){
                top.linkTo(introTitle.bottom, margin = 8.dp)
            }
        ) {
            OptionsList(context, navController)
        }
        Column(
            modifier = Modifier
                .constrainAs(favorite_routine_title) {
                    top.linkTo(options.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Favourite Routines")
        }
        Column(
            modifier = Modifier
                .constrainAs(emptyRoutine) {
                    top.linkTo(favorite_routine_title.bottom, margin = 18.dp)
                }
                .padding(bottom = 12.dp)
        ){
            RoutineListWhenEmpty()
        }
        Column(
            modifier = Modifier
                .constrainAs(articles_title) {
                    top.linkTo(emptyRoutine.bottom, margin = -10.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            StarSurroundedText("Did You Know")
        }
        Column(
            modifier = Modifier
                .constrainAs(articles) {
                    top.linkTo(articles_title.bottom, margin = 18.dp)
                }
                .padding(bottom = 24.dp)
        ){
            ArticlesList(navController)
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(articles.bottom, margin = 20.dp)
                }
        ){
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun OptionsList(context: Context, navController: NavHostController){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(displayName = "sleep", icon = R.drawable.sleep_icon, 71, 71, false, 0, 0){ AuthBackend.signOut() }
        OptionItem(displayName = "music", icon = R.drawable.music_icon, 71, 71, false, 0, 0){something()}
        OptionItem(displayName = "meditate", icon = R.drawable.meditate_icon, 71, 71, false, 0, 0){something()}
        OptionItem(displayName = "sound", icon = R.drawable.sound_icon, 71, 71, false, 0, 0){toSoundActivity(context, navController)}
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ){
        OptionItem(displayName = "self-love", icon = R.drawable.self_love_icon, 71, 71, false, 0, 0){something()}
        OptionItem(displayName = "stretch", icon = R.drawable.stretch_icon, 71, 71, false, 0, 0){something()}
        OptionItem(displayName = "slumber party", icon = R.drawable.slumber_party_icon, 71, 71, false, 0, 0){something()}
        OptionItem(displayName = "bedtime story", icon = R.drawable.bedtime_story_icon, 71, 71, false, 0, 0){something()}
    }
}

@Composable
fun OptionItem(
    displayName: String,
    icon: Int,
    width: Int,
    height: Int,
    pro: Boolean,
    xOffset: Int,
    yOffset: Int,
    lambda: (displayName: String) -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 15.dp)
            .clickable { lambda(displayName) }
    ){
        Box(
        ){
            Card(
                modifier = Modifier
                    .size(width.dp, height.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                backgroundColor = White,
                elevation = 8.dp
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "$displayName icon",
                    modifier = Modifier
                        .size(width = 25.64.dp, height = 25.64.dp)
                        .padding(20.dp)
                )
            }
            if(pro) {
                Card(
                    modifier = Modifier
                        .size(45.dp, 22.dp)
                        .offset(xOffset.dp, yOffset.dp),
                    shape = MaterialTheme.shapes.small,
                    backgroundColor = Color.Black,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        MorgeNormalText(
                            text = "PRO",
                            color = Color.White,
                            fontSize = 12,
                            xOffset = 0,
                            yOffset = 0
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        NormalText(
            text = displayName,
            color = MaterialTheme.colors.primary,
            fontSize = 9,
            xOffset = 0,
            yOffset = 0
        )
    }
}

@Composable
private fun RoutineListWhenEmpty(){
    Column(
    ) {
        EmptyRoutine {
            something()
        }
        SurpriseMeRoutine {
            something()
        }
    }
}

@Composable
private fun ArticlesList(navController: NavController){
    Column(
    ) {
        Article(
            title = "the danger of sleeping pills",
            summary = "Sleeping pills are not meant to be taken daily.",
            icon = R.drawable.danger_of_sleeping_pills_icon
        ) {
            navController.navigate(Screen.Article.screen_route)
        }
        Article(
            title = "benefits of a goodnight sleep",
            summary = "Your skincare routine ends with a goodnight sleep.",
            icon = R.drawable.benefits_of_goodnight_sleep_icon
        ) {
            something()
        }
        Article(
            title = "how to be extra creative & productive?",
            summary = "Your day starts right after a goodnight sleep.",
            icon = R.drawable.extra_creative_and_productive_icon
        ) {
            something()
        }
    }
}

private fun toSoundActivity(context: Context, navController: NavHostController){
   /* val i = Intent(context, SoundActivity::class.java)
    context.startActivity(i)*/
   /* val sounds = SoundObject.sounds().value
    val isEmpty = sounds?.isEmpty() ?: false
    if(isEmpty){
        SoundBackend.querySound()
    }*/
    navController.navigate(Screen.Sound.screen_route)
}

private fun something(){

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
fun UserDashboardActivityPreview() {
    EUNOIATheme {
        //UserDashboardActivityUI()
    }
}