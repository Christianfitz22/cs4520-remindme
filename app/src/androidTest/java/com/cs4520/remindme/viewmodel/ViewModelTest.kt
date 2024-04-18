package com.cs4520.remindme.viewmodel

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cs4520.remindme.Category
import com.cs4520.remindme.Reminder
import com.cs4520.remindme.ReminderDatabase
import com.cs4520.remindme.ReminderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewModelTest {
    private val viewModel = ReminderViewModel()
    private lateinit var database : ReminderDatabase


    @Before
    fun setUp(){
        ReminderDatabase.setContext(InstrumentationRegistry.getInstrumentation().targetContext)
        database = ReminderDatabase.getInstance()

        viewModel.database = database
    }
    @Test
    fun testReflectDatabase(){
        val rem = Reminder("Name", Category.PERSONAL, "Description")
        val rem2 = Reminder("Test2", Category.HOME, "desc")
        database.reminderDAO().insert(rem)
        database.reminderDAO().insert(rem2)

        viewModel.reflectDatabase()

        viewModel.ResponseData.value?.contains(rem)?.let { assert(it) }
    }


    @Test
    fun testAddReminder(){
        val reminder = Reminder("New name", Category.FAMILY, "Description")
        viewModel.addReminder(reminder)

        viewModel.ResponseData.value?.contains(reminder)?.let {assert(it)}
        assert(database.reminderDAO().getData().contains(reminder))
    }

    @Test
    fun testDeleteReminder(){
        val rem = Reminder("Name", Category.PERSONAL, "Description")
        val rem2 = Reminder("Test2", Category.HOME, "desc")
        val reminder = Reminder("New name", Category.FAMILY, "Description")
        val reminder2 = Reminder("Reminder", Category.FAMILY, "Desc")

        database.reminderDAO().insert(rem)
        database.reminderDAO().insert(rem2)
        database.reminderDAO().insert(reminder)
        database.reminderDAO().insert(reminder2)
        viewModel.deleteReminder(reminder)

    }

    //The last function is API calling only, and thus does not need to be tested according to the rubric
}