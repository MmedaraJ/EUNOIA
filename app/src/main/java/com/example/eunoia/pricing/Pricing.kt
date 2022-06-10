package com.example.eunoia.pricing

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eunoia.ui.components.AlignedLightText
import com.example.eunoia.ui.components.AlignedNormalText
import com.example.eunoia.ui.components.BackArrowHeader
import com.example.eunoia.ui.screens.Screen
import com.example.eunoia.ui.theme.EUNOIATheme

@Composable
fun PricingUI(navController: NavController){
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
    ) {
        val (
            header,
            title,
            intro,
            rookies,
            master6,
            master9,
            contributor,
            select_membership_text1,
            select_membership,
            endSpace,
        ) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top, margin = 40.dp)
                }
                .fillMaxWidth()
        ) {
            BackArrowHeader(
                { navController.popBackStack() },
                { navController.navigate(Screen.Settings.screen_route) })
        }
        Column(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(header.bottom, margin = 40.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            PricingCompleteTitle()
        }
        Column(
            modifier = Modifier
                .constrainAs(intro) {
                    top.linkTo(title.bottom, margin = 5.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            AlignedLightText(
                "Everybody deserves a goodnight sleep.",
                color = MaterialTheme.colors.primary,
                fontSize = 15,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(rookies) {
                    top.linkTo(intro.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(master6.start, margin = 4.dp)
                }
        ) {
            Rookie{}
        }
        Column(
            modifier = Modifier
                .constrainAs(master6) {
                    top.linkTo(rookies.top, margin = 0.dp)
                    bottom.linkTo(rookies.bottom, margin = 0.dp)
                    start.linkTo(rookies.end, margin = 4.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            MasterSix{}
        }
        Column(
            modifier = Modifier
                .constrainAs(master9) {
                    top.linkTo(rookies.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(contributor.start, margin = 4.dp)
                }
        ) {
            MasterNine{}
        }
        Column(
            modifier = Modifier
                .constrainAs(contributor) {
                    top.linkTo(master9.top, margin = 0.dp)
                    start.linkTo(master9.end, margin = 4.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                    bottom.linkTo(master9.bottom, margin = 0.dp)
                }
        ) {
            Contributor{}
        }
        Column(
            modifier = Modifier
                .constrainAs(select_membership) {
                    top.linkTo(master9.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            AlignedNormalText(
                "Select a membership level that suits you.",
                color = MaterialTheme.colors.primary,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(select_membership_text1) {
                    top.linkTo(select_membership.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                }
        ) {
            AlignedLightText(
                "Prices listed in CAD. Taxes may apply. \n" +
                        "Cancel anytime. \n" +
                        "Your subscription will automatically renew unless auto-renew is turned off at least 24 hours before \n" +
                        "the end of the current period.\n" +
                        "By using the program, you agree to \n" +
                        "our terms and policies.",
                color = MaterialTheme.colors.primary,
                fontSize = 16,
                xOffset = 0,
                yOffset = 0
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(endSpace) {
                    top.linkTo(select_membership_text1.bottom, margin = 40.dp)
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
    EUNOIATheme {
        PricingUI(rememberNavController())
    }
}