package com.cs4520.remindme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException

class ReminderViewModel : ViewModel() {
    private val _ResponseData = MutableLiveData<ArrayList<Reminder>>()
    val ResponseData : LiveData<ArrayList<Reminder>> = _ResponseData
    //private val repository = DatabaseRepository()

    private val _AdviceData = MutableLiveData<String>()
    val AdviceData : LiveData<String> = _AdviceData

    private val database = ReminderDatabase.getInstance()

    fun initialize() {
        //createWorkBuilder()

        reflectDatabase()
        generateAdvice()
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
        reflectDatabase()
        generateAdvice()
    }

    fun deleteReminder(reminder: Reminder) {
        database.reminderDAO().delete(reminder);
        reflectDatabase()
        generateAdvice()
    }

    fun generateAdvice() {

        _AdviceData.value = ""

        val service = RetrofitClient.getRetrofitInstance()

        viewModelScope.launch(Dispatchers.IO) {

            val response: Response<String>

            try {
                response = service.getIsDayOff()
            } catch (e: UnknownHostException) {
                _AdviceData.postValue("Unable to establish connection to host.")
                return@launch
            }

            if (response.isSuccessful) {
                if (response.body() == "0") {
                    _AdviceData.postValue("Today's work will make tomorrow's happiness.")
                } else if (response.body() == "1") {
                    _AdviceData.postValue("Enjoy relaxing times to the fullest.")
                } else {
                    _AdviceData.postValue("Accomplish your goals! Work day or not.")
                }
            } else {
                _AdviceData.postValue("API call failed.")
            }
        }
    }
}