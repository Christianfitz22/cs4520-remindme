package com.cs4520.remindme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ReminderViewModel : ViewModel() {
    private val _ResponseData = MutableLiveData<ArrayList<Reminder>>()
    val ResponseData : LiveData<ArrayList<Reminder>> = _ResponseData
    //private val repository = DatabaseRepository()

    private val database = ReminderDatabase.getInstance()

    fun initialize() {
        //createWorkBuilder()

        //makeApiCall()
        reflectDatabase()
    }

    fun reflectDatabase() {
        val dao = database.reminderDAO();
        val databaseEntries = dao.getData();

        if (databaseEntries.isEmpty()) {
            _ResponseData.value?.clear()
        } else {
            val newData = ArrayList<Reminder>()
            for(item in databaseEntries){
                newData.add(item)
            }
            _ResponseData.value = newData
        }
    }

    fun addReminder(reminder: Reminder) {
        database.reminderDAO().insert(reminder);
    }

    fun deleteReminder(reminder: Reminder) {
        database.reminderDAO().delete(reminder);
    }

    /*
    private fun makeApiCall(input: String?=null) {
        val dao = database.reminderDAO()

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getAllRepository() //TA says that crashing here if nothing is loaded is ok
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {

                            _ResponseData.value = response.body()

                            _ResponseData.value?.let { dao.insertAll(it) }
                        } else {
                            _ResponseData.value?.clear()
                        }
                    } catch (e: Throwable) {
                        _ResponseData.value?.clear()
                    }
                }
            } catch (e : UnknownHostException){ //access database
                withContext(Dispatchers.Main){
                    val databaseEntries = dao.getData()
                    if (databaseEntries.isEmpty()) {
                        _ResponseData.value?.clear()
                    } else {
                        val newData = ArrayList<Reminder>()
                        for(item in databaseEntries){
                            newData.add(item)
                        }
                        _ResponseData.value = newData
                    }
                }
            }

            //TODO: if list empty, check database as well?
        }
    }
     */
}