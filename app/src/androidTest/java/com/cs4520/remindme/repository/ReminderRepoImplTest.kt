package com.cs4520.remindme.repository

import android.content.Context
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.cs4520.remindme.Category
import com.cs4520.remindme.MainActivity
import com.cs4520.remindme.Reminder
import com.cs4520.remindme.ReminderDatabase
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReminderRepoImplTest {
    lateinit var database: ReminderDatabase

    @Before
    fun setup() {
        ReminderDatabase.setContext(InstrumentationRegistry.getInstrumentation().targetContext)
        database = ReminderDatabase.getInstance()
    }

    @Test
    fun testRepoEmpty() {
        database.reminderDAO().clearAll()
        assertTrue(database.reminderDAO().getData().isEmpty())

        val example1 = Reminder("Name1", Category.PERSONAL, "Desc1")

        database.reminderDAO().insert(example1)
        assertFalse(database.reminderDAO().getData().isEmpty())

        database.reminderDAO().clearAll()

        assertTrue(database.reminderDAO().getData().isEmpty())
    }

    @Test
    fun testRepoAdditions() {
        database.reminderDAO().clearAll()

        val example1 = Reminder("Name1", Category.PERSONAL, "Desc1")
        val example2 = Reminder("Name2", Category.WORK, "Desc2")
        val example3 = Reminder("Name3", Category.FAMILY, "Desc3")

        val exampleList = ArrayList<Reminder>()

        database.reminderDAO().insert(example1);
        exampleList.add(example1)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), exampleList.toTypedArray())

        database.reminderDAO().insert(example2);
        exampleList.add(example2)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), exampleList.toTypedArray())

        database.reminderDAO().insert(example3);
        exampleList.add(example3)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), exampleList.toTypedArray())
    }

    @Test
    fun testRepoOverrides() {
        database.reminderDAO().clearAll()

        val example1 = Reminder("Name1", Category.PERSONAL, "Desc1")
        val example2 = Reminder("Name2", Category.WORK, "Desc2")
        val example3 = Reminder("Name3", Category.FAMILY, "Desc3")

        database.reminderDAO().insert(example1);
        database.reminderDAO().insert(example2);
        database.reminderDAO().insert(example3);

        val exampleList = listOf(example1, example2, example3)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), exampleList.toTypedArray())

        val example1different = Reminder("Name1", Category.HOME, "Desc1Diff")

        database.reminderDAO().insert(example1different)

        val changedList = listOf(example2, example3, example1different)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), changedList.toTypedArray())
    }

    @Test
    fun testRepoDeletes() {
        database.reminderDAO().clearAll()

        val example1 = Reminder("Name1", Category.PERSONAL, "Desc1")
        val example2 = Reminder("Name2", Category.WORK, "Desc2")
        val example3 = Reminder("Name3", Category.FAMILY, "Desc3")

        val example2SameDesc = Reminder("Name4", Category.PERSONAL, "Desc2")
        val example2SameCat = Reminder("Name4", Category.WORK, "Desc4")
        val example2SameName = Reminder("Name2", Category.PERSONAL, "Desc4")

        database.reminderDAO().insert(example1)
        database.reminderDAO().insert(example2)
        database.reminderDAO().insert(example3)

        val startingList = listOf(example1, example2, example3)
        val endingList = listOf(example1, example3)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), startingList.toTypedArray())

        database.reminderDAO().delete(example2SameDesc)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), startingList.toTypedArray())

        database.reminderDAO().delete(example2SameCat)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), startingList.toTypedArray())

        database.reminderDAO().delete(example2SameName)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), endingList.toTypedArray())

        database.reminderDAO().delete(example1)

        assertArrayEquals(database.reminderDAO().getData().toTypedArray(), listOf(example3).toTypedArray())

        database.reminderDAO().delete(example3)

        assertTrue(database.reminderDAO().getData().isEmpty())
    }
}