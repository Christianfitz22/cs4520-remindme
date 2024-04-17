package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITestDetailScreen {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    var buttonPressed = 0;
    val rem = Reminder(name = "Test name", category = Category.HOME, description = "Here is my test description")
    @Before
    fun setUp(){
        composeTestRule.activity.setContent{
            MaterialTheme {
                composeTestRule.activity.Detail(onNavigateToList = {buttonPressed++})
            }
        }

        composeTestRule.activity.reminderSelected(rem, onNavigateToDetail = {})
    }

    @Test
    fun testIfShown(){
        composeTestRule.onNodeWithTag("Name").assertExists()
        composeTestRule.onNodeWithTag("Name").assertTextEquals("Test name")

        composeTestRule.onNodeWithTag("Image").assertExists()

        composeTestRule.onNodeWithTag("Description").assertExists()
        composeTestRule.onNodeWithTag("Description").assertTextEquals("Here is my test description")

        composeTestRule.onNodeWithTag("Button").assertExists()
        composeTestRule.onNodeWithTag("Button").assertHasClickAction()
    }

    @Test
    fun testChange(){
        val rem2 = Reminder(name = "Second name", category = Category.PERSONAL, description = "Another description which is different")
        composeTestRule.activity.reminderSelected(rem2, onNavigateToDetail = {})

        composeTestRule.onNodeWithTag("Name").assertTextEquals("Second name")
        composeTestRule.onNodeWithTag("Description").assertTextEquals("Another description which is different")
    }

    @Test
    fun testDelete(){
        composeTestRule.onNodeWithTag("Button").performClick()
        assertEquals(buttonPressed, 1)
    }
}