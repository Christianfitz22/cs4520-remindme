package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITestListScreen {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val rem = ArrayList<Reminder>()

    @Before
    fun setUp(){
        composeTestRule.activity.setContent {
            composeTestRule.activity.List(onNavigateToDetail = {})
        }
    }

    @Test
    fun isDisplayed(){
        composeTestRule.onNodeWithTag("Quote").assertExists()
    }
}