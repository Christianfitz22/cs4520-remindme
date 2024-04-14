package com.cs4520.remindme

enum class ReminderCategory {
    Home, Work, Family, Personal
}

data class ReminderEntry(val name: String, val category: ReminderCategory, val desc: String);