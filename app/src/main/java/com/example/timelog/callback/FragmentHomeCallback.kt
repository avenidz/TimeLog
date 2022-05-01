package com.example.timelog.callback

import android.content.Intent
import android.view.View
import com.example.timelog.activity.ViewLogDetails
import com.example.timelog.logdata.UserTimeLog

interface FragmentHomeCallback {
    fun onItemSelected(view: View, timeLog: UserTimeLog){
        view.context.startActivity(
            Intent(
                view.context, ViewLogDetails::class.java
            ).putExtra("logId", timeLog.timeId.toString())
        )
    }
}