package com.cs4520.remindme

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITestListScreen {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
}