package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITestListLogic {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val rem = ArrayList<Reminder>()

    @Before
    fun setUp(){
        for(i in 0..10){
            val string = "Reminder $i"
            if(i % 2 == 0){
                rem.add(Reminder(string, Category.HOME, "Description here"))
            } else {
                rem.add(Reminder(string, Category.WORK, "Description here"))
            }
        }
        composeTestRule.activity.setContent {
            composeTestRule.activity.DrawBox(rem, onNavigateToDetail = {})
        }
    }

    @Test
    fun ifDisplayed(){
        composeTestRule.onNodeWithTag("Box").assertExists()
    }

    //For each reminder
    @Test
    fun assertReminders(){
        for(i in 0 .. 10){
            val string = "Reminder $i"
            composeTestRule.onNodeWithTag("Box", useUnmergedTree = true).onChild().performScrollToIndex(i)
            composeTestRule.onNodeWithText(string).printToLog("Testing UI")
            composeTestRule.onNodeWithText(string).assertExists()
            composeTestRule.onNodeWithText(string).assertIsDisplayed()
        }
    }
}