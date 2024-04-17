package com.cs4520.remindme

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
    var buttonpressed = 0

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
            composeTestRule.activity.List(onNavigateToDetail = {buttonpressed = 1})
        }
    }

    @Test
    fun isDisplayed(){

    }

    @Test
    fun isScrollable(){
        //since the viewmodel handles the data itself, just test if all of the items that are there are scrollable
        //since the test will utilize the preexisting data
    }
}