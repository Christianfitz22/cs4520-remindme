package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITestListScreen {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    val composeTestRule2 = createComposeRule()

    val rem = ArrayList<Reminder>()
    @Before
    fun setUp(){
        composeTestRule.activity.setContent{
            MaterialTheme {
                composeTestRule.activity.List(onNavigateToDetail = {})
            }
        }
        for(i in 0..10){
            val string = "Reminder $i"
            if(i % 2 == 0){
                rem.add(Reminder(string, Category.HOME, "Description here"))
            } else {
                rem.add(Reminder(string, Category.WORK, "Description here"))
            }
        }
        composeTestRule2.setContent {
            MockList(rem)
        }
    }

    //Testing the functionality of the list with a mocked list of items (no reliance on viewModel)
    @Composable
    fun MockList(reminders : List<Reminder>){
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                itemsIndexed(reminders) { index, reminder ->
                    composeTestRule.activity.Preview(reminder, onNavigateToDetail = {})
                }
            }
        }
    }

    @Test
    fun testIfDisplayed(){

    }

    @Test
    fun scrollable(){

    }
}