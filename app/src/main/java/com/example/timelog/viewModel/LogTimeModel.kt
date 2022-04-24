package com.example.timelog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogTimeModel: ViewModel() {

    private val logTime = MutableStateFlow<LogTime>(LogTime.Empty)
    val logTimeState: StateFlow<LogTime> = logTime

    fun logMyTime(logTitle: String, logDescription: String) = viewModelScope.launch {
        logTime.value = LogTime.Loading
        delay(1000)

        if(logTitle == ""){
            logTime.value = LogTime.Error("Specify title.")
        }else if(logDescription == ""){
            logTime.value = LogTime.Error("Add Description.")
        }else{
            logTime.value = LogTime.Success
        }
    }


    sealed class LogTime{
        object Empty: LogTime()
        data class Error(val message: String): LogTime()
        object Loading: LogTime()
        object Success: LogTime()
    }
}