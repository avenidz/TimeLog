package com.example.timelog.callback

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.logdata.UserTimeLog
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

interface ViewLogDetailsCallback {

    @RequiresApi(Build.VERSION_CODES.O)
    fun pendingLogUpdate(thisContext: Context, logId: Int){

        val logDetails = returnedData(thisContext, logId)

        val current = Date()
        val currentTime = current.time

        val _convertTo24Format = SimpleDateFormat("HH:mm")
        val updateTime = _convertTo24Format.format(currentTime)

        val updateData = UserTimeLog(
            logId,
            logDetails[0].logUserId,
            logDetails[0].timeLogTitle,
            logDetails[0].timeLogDescription,
            logDetails[0].timeLogIn,
            updateTime.toString(),
            logDetails[0].timeLogDate
        )
        LogDataUserDatabase.getDatabaseInstance(thisContext).logUserDao().updateLogPending(updateData)
    }

    private fun returnedData(thisContext: Context, logId: Int): List<UserTimeLog>{
        return LogDataUserDatabase.getDatabaseInstance(thisContext).logUserDao().getLogDetailsById(logId)
    }

}