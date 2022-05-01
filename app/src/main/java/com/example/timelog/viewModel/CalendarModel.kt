package com.example.timelog.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.logdata.UserTimeLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalendarModel: ViewModel() {

    private val calendar = MutableStateFlow<CalendarView>(CalendarView.Empty)
    private lateinit var userLogData : List<UserTimeLog>

    val calendarState = calendar.asStateFlow()
    fun calendarData(context: Context, userId: Int, selectedDate: String) = viewModelScope.launch{
        calendar.value = CalendarView.Loading
        delay(200)

        userLogData = viewUserLog(context, userId, selectedDate)

        if(userLogData.isEmpty()){
            calendar.value = CalendarView.Error(userLogData)
        }else{
            calendar.value = CalendarView.Success(userLogData)
        }

    }

    sealed class CalendarView{
        object Empty: CalendarView()
        object Loading: CalendarView()
        data class Error(val logListAsSelected: List<UserTimeLog>): CalendarView()
        data class Success(val logListAsSelected:List<UserTimeLog>): CalendarView()
    }
    private fun viewUserLog(context: Context, userId: Int, selectedDate: String): List<UserTimeLog>{
        return LogDataUserDatabase.getDatabaseInstance(context).logUserDao().getUserTimeLogByDate(userId,selectedDate)
    }

}