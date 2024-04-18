package com.cs4520.remindme.viewmodel

import android.database.sqlite.SQLiteOpenHelper
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.cs4520.remindme.Category
import com.cs4520.remindme.Reminder
import com.cs4520.remindme.ReminderDAO
import com.cs4520.remindme.ReminderDatabase

//For Testing Viewmodel
class FakeReminderDatabase(private val dao : ReminderDAO ) : ReminderDatabase() {
    override fun reminderDAO(): ReminderDAO {
        return dao
    }

    override fun clearAllTables() {

    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return InvalidationTracker(this)
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        
    }
}

class FakeReminderDAO : ReminderDAO{
    var string = "Nothing"
    var r = Reminder("Name", Category.PERSONAL, "Description")
    private var list : List<Reminder> = ArrayList<Reminder>()

    override fun insert(reminder: Reminder) {
        string = "insert"
        r = reminder
    }

    override fun insertAll(reminders: List<Reminder>) {
        TODO("Not yet implemented")
    }

    override fun update(reminder: Reminder) {
        TODO("Not yet implemented")
    }

    override fun delete(reminder: Reminder) {
        string = "delete"
        r = reminder
    }

    override fun clearAll() {
        string = "clearAll"
    }
    fun setData(l : List<Reminder>){
        list = l
    }
    override fun getData(): List<Reminder> {
        return list
    }

}