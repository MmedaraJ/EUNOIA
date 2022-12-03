package com.example.eunoia.dashboard.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eunoia.dashboard.sound.SoundActivityUI
import com.example.eunoia.services.GeneralMediaPlayerService
import com.example.eunoia.services.SoundMediaPlayerService
import com.example.eunoia.ui.navigation.EunoiaApp
import com.example.eunoia.ui.navigation.MultiNavTabContent
import com.example.eunoia.ui.navigation.globalViewModel
import com.example.eunoia.ui.theme.EUNOIATheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class UserDashboardActivityKtTest{
    private val generalMediaPlayerService = GeneralMediaPlayerService()
    private val soundMediaPlayerService = SoundMediaPlayerService()

    @get: Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterialApi::class)
    @Before
    fun setUp() {
        composeTestRule.setContent {
            /*EunoiaApp(
                rememberCoroutineScope(),
                rememberModalBottomSheetState(
                    ModalBottomSheetValue.Hidden
                ),
                viewModel(),
                generalMediaPlayerService,
                soundMediaPlayerService,
            ) { screen ->
                MultiNavTabContent(
                    screen,
                    rememberCoroutineScope(),
                    rememberModalBottomSheetState(
                        ModalBottomSheetValue.Hidden
                    ),
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                )
            }*/

            EUNOIATheme {
                UserDashboardActivityUI(
                    rememberNavController(),
                    rememberCoroutineScope(),
                    rememberModalBottomSheetState(
                        initialValue = ModalBottomSheetValue.Hidden,
                        confirmStateChange = { true }
                    ),
                    viewModel(),
                    viewModel(),
                    viewModel(),
                    viewModel(),
                    viewModel(),
                    viewModel(),
                    generalMediaPlayerService,
                    soundMediaPlayerService,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun verify_if_all_views_exists() {
        composeTestRule.onNodeWithTag("options").assertExists()
        composeTestRule.onNodeWithText("sound").performClick()
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithText("library")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText("library").assertExists()
    }
}