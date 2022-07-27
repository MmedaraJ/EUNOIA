package com.example.eunoia.dashboard.article

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.R
import com.example.eunoia.ui.components.*
import com.example.eunoia.ui.navigation.globalViewModel_
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme
import com.example.eunoia.viewModels.GlobalViewModel

@Composable
fun ArticleUI(navController: NavController, globalViewModel: GlobalViewModel){
    globalViewModel_!!.navController = navController
    val scrollState = rememberScrollState()
    val sub_texts = arrayOf(
        "Your skincare routine ends with a goodnight sleep.\n" +
                "Do you know that most Retinoids works in the dark? Go to sleep.",
        "Instead of getting addicted to caffein, get a goodnight sleep instead. " +
                "It reduces stress and anxiety too.",
        "When your mind is fully at rest at night, it allows you to be " +
                "more creative, more productive, and more innovative.",
        "Do you know that sleeping pills are not meant to be used daily or even " +
                "often? Getting insufficient sleep has been linked to a higher risk of conditions " +
                "such as heart disease, diabetes, and obesity."
    )
    val icons = arrayOf(
        R.drawable.beauty_sleep_icon,
        R.drawable.sleep_coffee_icon,
        R.drawable.no_creative_block_icon,
        R.drawable.good_riddance_icon,
    )
    val sub_titles = arrayOf(
        "Beauty Sleep",
        "Sleep > Coffee",
        "No Creative Block",
        "Good Riddance"
    )
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            intro,
            info,
            endSpace
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            BackArrowHeader({ navController.popBackStack() }, {}, {navController.navigate(Screen.Settings.screen_route)})
        }
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            CompleteTitle()
        }
        Column(
            modifier = Modifier
                .constrainAs(intro) {
                    top.linkTo(title.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            AlignedLightText(
                "Sleep is an essential function that allows your body and mind to recharge, " +
                        "leaving you refreshed and alert when you wake up.",
                color = MaterialTheme.colors.primary,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(info) {
                    top.linkTo(intro.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icons.forEachIndexed { index, icon ->
                Column(modifier = Modifier.padding(bottom = 0.dp)) {
                    if(index%2 == 0){
                        StarSurroundedTextWithIconRight(
                            text = sub_titles[index],
                            drawable = icon,
                            drawableWidth = 70.0,
                            drawableHeight = 60.0
                        )
                    }else{
                        StarSurroundedTextWithIconLeft(
                            text = sub_titles[index],
                            drawable = icon,
                            drawableWidth = 70.0,
                            drawableHeight = 60.0
                        )
                    }
                }
                Column(modifier = Modifier.padding(bottom = 0.dp)) {
                    AlignedLightText(
                        sub_texts[index],
                        color = MaterialTheme.colors.primary,
                        fontSize = 15,
                        xOffset = 0,
                        yOffset = 0
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(info.bottom, margin = 40.dp)
                }
        ){
            Spacer(modifier = Modifier.height(40.dp))
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
        ArticleUI(rememberNavController(), globalViewModel)
    }
}
