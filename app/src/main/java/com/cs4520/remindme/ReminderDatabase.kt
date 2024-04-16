package com.cs4520.remindme

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Reminder::class], version = 1)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDAO(): ReminderDAO

    companion object {
        //According to piazza this is how I do it
        @SuppressLint("StaticFieldLeak")
        private var instance: ReminderDatabase? = null
        @SuppressLint("StaticFieldLeak")
        private var context : Context? = null
        @Synchronized
        fun getInstance(): ReminderDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context!!.applicationContext, ReminderDatabase::class.java,
                    "reminders"
                ).allowMainThreadQueries().build()
            }

            return instance!!

        }

        fun setContext(ctx: Context){
            context = ctx
        }
    }
}