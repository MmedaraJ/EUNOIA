package com.example.eunoia.settings

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.core.Amplify
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.theme.*
import com.example.eunoia.viewModels.GlobalViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(
    navController: NavController,
    scope: CoroutineScope,
    state: ModalBottomSheetState
){
    globalViewModel!!.navController = navController
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            profile_picture,
            first_name,
            username,
            block1,
            edit,
            block2,
            block3,
            create_new_slumber_party,
            endSpace
        ) = createRefs()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            LogoAndStarHeader(
                {},
                { navController.popBackStack() }
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(profile_picture) {
                    top.linkTo(header.bottom, margin = 30.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            ProfilePictureDefault(
                154.dp,
                167.dp,
                0,
                0,
                1.0
            ){}
        }
        Column(
            modifier = Modifier
                .constrainAs(first_name) {
                    top.linkTo(profile_picture.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            NormalText(
                text = Amplify.Auth.currentUser.username,
                color = Black,
                fontSize = 18,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(username) {
                    top.linkTo(first_name.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            NormalText(
                text = "@${Amplify.Auth.currentUser.username}",
                color = Grey,
                fontSize = 10,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(block1) {
                    top.linkTo(username.bottom, margin = 20.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            SettingsBlockOne(navController)
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(SwansDown)
                .border(BorderStroke(0.5.dp, Color.Black), RoundedCornerShape(50.dp))
                .constrainAs(edit) {
                    top.linkTo(username.bottom, margin = (-10).dp)
                    end.linkTo(parent.end, margin = 10.dp)
                },
            contentAlignment = Alignment.Center
        ){
            MorgeNormalText(
                text = "edit",
                color = Black,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(block2) {
                    top.linkTo(block1.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            SettingsBlockTwo()
        }
        Column(
            modifier = Modifier
                .constrainAs(block3) {
                    top.linkTo(block2.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            SettingsBlockThree()
        }
        Column(
            modifier = Modifier
                .constrainAs(create_new_slumber_party){
                    top.linkTo(block3.bottom, margin = (-40).dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ){
            SlumberParty("Create a new slumber party"){}
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(create_new_slumber_party.bottom, margin = 20.dp)
                }
        ){
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SlumberParty(text: String, lambda: () -> Unit){
    var clicked by rememberSaveable{ mutableStateOf(false) }
    var cardModifier = Modifier
        .wrapContentHeight()
        .clickable {
            clicked = !clicked
            lambda()
        }
        .wrapContentWidth()

    if(clicked){
        cardModifier = cardModifier.then(
            Modifier.border(BorderStroke(1.dp, Black), MaterialTheme.shapes.small)
        )
    }
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = GoldSand,
        elevation = 4.dp,
    ){
        ConstraintLayout(
            modifier = Modifier.padding(8.dp)
        ) {
            val (new_slumber_text) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(new_slumber_text) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                NormalText(
                    text = text,
                    color = Black,
                    fontSize = 15,
                    xOffset = 0,
                    yOffset = 0
                )
            }
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
fun Preview() {
    val globalViewModel: GlobalViewModel = viewModel()
    EUNOIATheme {
        //Settings(rememberNavController(), globalViewModel)
    }
}