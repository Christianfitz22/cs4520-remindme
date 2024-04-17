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
class UITestPreviewList {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val reminder = Reminder("Some name here", Category.PERSONAL, "Some description here")
    var buttonPressed = 0
    @Before
    fun setUp(){
        composeTestRule.activity.setContent{
            MaterialTheme{
                composeTestRule.activity.Preview(reminder, onNavigateToDetail = {buttonPressed++})
            }
        }
    }

    @Test
    fun testIfDisplayed(){
        composeTestRule.onNodeWithTag("Image").assertExists()

        composeTestRule.onNodeWithTag("Name").assertExists()
        composeTestRule.onNodeWithTag("Name").assertTextEquals("Some name here")

        composeTestRule.onNodeWithTag("Button").assertExists()
        composeTestRule.onNodeWithTag("Button").assertHasClickAction()
        composeTestRule.onNodeWithTag("Button").assertTextEquals("...")
    }

    @Test
    fun deleteButtonPressed(){
        composeTestRule.onNodeWithTag("Button").performClick()
        assertEquals(buttonPressed, 1)
    }
}