package com.example.timelog.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.logdata.UserTimeLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class LogTimeModel: ViewModel() {

    private val logTime = MutableStateFlow<LogTime>(LogTime.Empty)
    val logTimeState: StateFlow<LogTime> = logTime

    @RequiresApi(Build.VERSION_CODES.O)
    fun logMyTime(context: Context, logTitle: String, logDescription: String, textClock: String) = viewModelScope.launch {
        logTime.value = LogTime.Loading
        delay(300)

        if(logTitle == ""){
            logTime.value = LogTime.Error("Specify title.")
        }else if(logDescription == ""){
            logTime.value = LogTime.Error("Add Description.")
        }else{
            addNewTimeLog(context,logTitle,logDescription,textClock)
            logTime.value = LogTime.Success
        }
    }


    sealed class LogTime{
        object Empty: LogTime()
        data class Error(val message: String): LogTime()
        object Loading: LogTime()
        object Success: LogTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNewTimeLog(context: Context, logTitle: String, logDescription: String, textClock: String){
        //check id of login user
        val extractLogUserId = returnLogId(context)

        //save new log
        val calendar = Calendar.getInstance()
        val setDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        val newLog = UserTimeLog(0, extractLogUserId, logTitle, logDescription, textClock,"", setDate.toString())
        LogDataUserDatabase.getDatabaseInstance(context).logUserDao().saveTimeLog(newLog)
    }
    private fun returnLogId(context: Context):Int{
        val userId: Int
        val userData = LogDataUserDatabase.getDatabaseInstance(context).logUserDao().checkLoggedUser()
        userId = userData[0].logId
        return userId
    }
}