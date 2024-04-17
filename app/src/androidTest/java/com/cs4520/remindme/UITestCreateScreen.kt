package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITestCreateScreen {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        composeTestRule.activity.setContent{
            MaterialTheme {
                composeTestRule.activity.Create()
            }
        }
    }

    @Test
    fun testTitleExist(){
        composeTestRule.onNodeWithTag("Title").assertExists()
        composeTestRule.onNodeWithTag("Title").assertTextEquals("Create a new reminder")
    }

    @Test
    fun testDropdownMenu(){
        composeTestRule.onNodeWithTag("Image").assertExists()
        composeTestRule.onNodeWithTag("Image").assertContentDescriptionEquals("Reminder Category Symbol")

        composeTestRule.onNodeWithTag("Category").assertExists()
        composeTestRule.onNodeWithTag("Category").assertHasClickAction()
        composeTestRule.onNodeWithTag("Category").performClick()
        //assert the dropdown members exist
        composeTestRule.onNodeWithText("Family", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Family", useUnmergedTree = true).onParent().assertHasClickAction()
    }

    @Test
    fun testDescriptionExist(){
        composeTestRule.onNodeWithTag("Description").assertExists()
        composeTestRule.onNodeWithText("Description").assertExists()
    }

    @Test
    fun testName(){
        composeTestRule.onNodeWithTag("Name").assertExists()
        composeTestRule.onNodeWithText("Name").assertExists()
    }

    @Test
    fun testButton(){
        composeTestRule.onNodeWithTag("Create Button").assertExists()
        composeTestRule.onNodeWithTag("Create Button").assertHasClickAction()
        composeTestRule.onNodeWithTag("Create Button").assertTextEquals("Create")
    }

    @Test
    fun testFillOut(){
        composeTestRule.onNodeWithTag("Name").performTextInput("Test Reminder")
        //Because it is editable text, label is included, so it is contains instead
        composeTestRule.onNodeWithTag("Name").assertTextContains("Test Reminder")

        composeTestRule.onNodeWithTag("Category").performClick()
        composeTestRule.onNodeWithText("Family", useUnmergedTree = true).onParent().performClick()

        composeTestRule.onNodeWithTag("Category Chosen", useUnmergedTree = true).assertTextContains("Family")

        composeTestRule.onNodeWithTag("Description").performTextInput("Testing, testing, 123")
        composeTestRule.onNodeWithTag("Description").assertTextContains("Testing, testing, 123")
    }

    //Check if it successfully clear fields
    @Test
    fun testCreate(){
        composeTestRule.onNodeWithTag("Name").performTextInput("Test Reminder")
        composeTestRule.onNodeWithTag("Category").performClick()
        composeTestRule.onNodeWithText("Family", useUnmergedTree = true).onParent().performClick()
        composeTestRule.onNodeWithTag("Description").performTextInput("Testing, testing, 123")

        composeTestRule.onNodeWithTag("Create Button").performClick()
        composeTestRule.onNodeWithText("Test Reminder").assertDoesNotExist()
        composeTestRule.onNodeWithTag("Category Chosen", useUnmergedTree = true).assertTextContains("Home")
        composeTestRule.onNodeWithText("Testing, testing, 123").assertDoesNotExist()
    }

    //the data should not be cleared
    @Test
    fun testCreateNoName(){
        composeTestRule.onNodeWithTag("Category").performClick()
        composeTestRule.onNodeWithText("Personal", useUnmergedTree = true).onParent().performClick()

        composeTestRule.onNodeWithTag("Description").performTextInput("Testing, testing, 123")
        composeTestRule.onNodeWithTag("Create Button").performClick()

        composeTestRule.onNodeWithTag("Category Chosen").assertTextContains("Personal")
        composeTestRule.onNodeWithTag("Description").assertTextContains("Testing, testing, 123")
    }

    @Test
    fun testCreateNoDescription(){
        composeTestRule.onNodeWithTag("Name").performTextInput("Test Reminder")
        composeTestRule.onNodeWithTag("Category").performClick()
        composeTestRule.onNodeWithText("Work", useUnmergedTree = true).onParent().performClick()

        composeTestRule.onNodeWithTag("Create Button").performClick()
        composeTestRule.onNodeWithTag("Name").assertTextContains("Test Reminder")
        composeTestRule.onNodeWithTag("Category Chosen").assertTextContains("Work")
    }

}