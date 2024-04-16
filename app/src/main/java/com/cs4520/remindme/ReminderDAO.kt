package com.cs4520.remindme

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReminderDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(reminder: Reminder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(reminders: List<Reminder>)

    @Update
    fun update(reminder: Reminder)

    @Delete
    fun delete(reminder: Reminder)

    @Query("DELETE FROM reminders")
    fun clearAll();

    @Query("SELECT * FROM reminders")
    fun getData(): List<Reminder>
}