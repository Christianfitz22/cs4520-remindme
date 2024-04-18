package com.cs4520.remindme.viewmodel

import com.cs4520.remindme.Category
import com.cs4520.remindme.Reminder
import com.cs4520.remindme.ReminderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    private val viewModel = ReminderViewModel()
    private val fakeReminderDao = FakeReminderDAO()
    private val fakeDatabase = FakeReminderDatabase(fakeReminderDao)


    @Before
    fun setUp(){
        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel.database = fakeDatabase
    }
    @Test
    fun testReflectDatabase() = runTest{
        val rem = ArrayList<Reminder>()
        for(i in 0..10){
            val string = "Reminder $i"
            if(i % 2 == 0){
                rem.add(Reminder(string, Category.HOME, "Description here"))
            } else {
                rem.add(Reminder(string, Category.WORK, "Description here"))
            }
        }

        fakeReminderDao.setData(rem)
        viewModel.reflectDatabase()

        for(r in rem){
            viewModel.ResponseData.value?.let { assert(it.contains(r)) }
        }
    }

    //@Test
    //fun testGenerateAdvice() = runTest{

    //}


    @Test
    fun testAddReminder() = runTest{
        val reminder = Reminder("New name", Category.FAMILY, "Description")
        viewModel.addReminder(reminder)
        assert(fakeReminderDao.string == "insert")
        assert(fakeReminderDao.r.name == "New name")
        assert(fakeReminderDao.r.category == Category.FAMILY)
        assert(fakeReminderDao.r.description == "Description")
    }

    @Test
    fun testDeleteReminder() = runTest{
        val reminder = Reminder("Reminder", Category.FAMILY, "Desc")
        viewModel.deleteReminder(reminder)
        assert(fakeReminderDao.string == "delete")
        assert(fakeReminderDao.r.name == "Reminder")
        assert(fakeReminderDao.r.category == Category.FAMILY)
        assert(fakeReminderDao.r.description == "Desc")
    }

}