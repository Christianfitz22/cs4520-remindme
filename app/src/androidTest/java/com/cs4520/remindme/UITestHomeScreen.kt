package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Testing for home screen
 */
@RunWith(AndroidJUnit4::class)
class UITestHomeScreen {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private var createNavigate = 0
    private var listNavigate = 0

    @Before
    fun setUp(){
        composeTestRule.activity.setContent{
            MaterialTheme {
                composeTestRule.activity.HomeScreen(
                    onNavigateToCreate = { createNavigate += 1 },
                    onNavigateToList = { listNavigate += 1 })
            }
        }
    }

    @Test
    fun testIfDisplayed(){
        composeTestRule.onNodeWithTag("Title").assertExists()
        composeTestRule.onNodeWithTag("Title").assertTextEquals("RemindMe")

        composeTestRule.onNodeWithTag("Create Button").assertExists()
        composeTestRule.onNodeWithTag("Create Button").assertHasClickAction()
        composeTestRule.onNodeWithTag("Create Button").assertTextEquals("Create Reminder")

        composeTestRule.onNodeWithTag("List Button").assertExists()
        composeTestRule.onNodeWithTag("List Button").assertHasClickAction()
        composeTestRule.onNodeWithTag("List Button").assertTextEquals("View Reminders")
    }

    @Test
    fun testIfButtonPress(){
        composeTestRule.onNodeWithTag("Create Button").performClick()
        assertEquals(createNavigate, 1)
        composeTestRule.onNodeWithTag("List Button").performClick()
        assertEquals(listNavigate, 1)
    }

}