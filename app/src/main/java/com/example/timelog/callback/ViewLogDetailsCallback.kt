package com.example.timelog.callback

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.logdata.UserTimeLog
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

interface ViewLogDetailsCallback {

    @RequiresApi(Build.VERSION_CODES.O)
    fun pendingLogUpdate(thisContext: Context, logId: Int){

        val logDetails = returnedData(thisContext, logId)
        var currentTime : Long = 0

        val current = Date()
        currentTime = current.time



        val updateData = UserTimeLog(
            logId,
            logDetails[0].logUserId,
            logDetails[0].timeLogTitle,
            logDetails[0].timeLogDescription,
            logDetails[0].timeLogIn,
            currentTime.toString(),
            logDetails[0].timeLogDate
        )
        LogDataUserDatabase.getDatabaseInstance(thisContext).logUserDao().updateLogPending(updateData)
    }

    private fun returnedData(thisContext: Context, logId: Int): List<UserTimeLog>{
        return LogDataUserDatabase.getDatabaseInstance(thisContext).logUserDao().getLogDetailsById(logId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun countTime(countTime: List<String>): String {
        val timeIn = countTime[0].toLong()
        val timeOut = countTime[1].toLong()


        val timeDiff = timeOut - timeIn

        val seconds = timeDiff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hours.toInt())
        calendar.set(Calendar.MINUTE, minutes.toInt())
        calendar.set(Calendar.SECOND, seconds.toInt())

        val date = calendar.time
        val formatDateTime = SimpleDateFormat("HH:mm:ss").format(date)


        return "$formatDateTime"
    }

}