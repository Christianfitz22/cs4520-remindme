package com.cs4520.remindme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder (
    @PrimaryKey
    val name: String,
    val category: Category,
    val description: String
)

enum class Category(val value: String) {
    WORK("Work"),
    FAMILY("Family"),
    HOME("Home"),
    PERSONAL("Personal")
}