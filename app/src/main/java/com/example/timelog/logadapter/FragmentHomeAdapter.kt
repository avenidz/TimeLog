package com.example.timelog.logadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timelog.R
import com.example.timelog.callback.FragmentHomeCallback
import com.example.timelog.logdata.UserTimeLog
import java.text.SimpleDateFormat

class FragmentHomeAdapter: RecyclerView.Adapter<FragmentHomeAdapter.LogAdapter>(), FragmentHomeCallback{

    val logList : MutableList<UserTimeLog> = mutableListOf()

    inner class LogAdapter(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(timeLog: UserTimeLog){

            val logTitle = view.findViewById<TextView>(R.id.showTitle)
            val logDescription = view.findViewById<TextView>(R.id.showDescription)
            val logTime = view.findViewById<TextView>(R.id.showTimeFrame)

            logTitle.text = timeLog.timeLogTitle
            logDescription.text = timeLog.timeLogDescription

            val userLogDate = timeLog.timeLogDate
            val userLogTime = timeLog.timeLogIn
            val userLogTimeOut = timeLog.timeLogOut

            val longTimeStamp = userLogTime.toLong()
            val longTimeStampOut: Long = if(userLogTimeOut == "") {
                0
            }else{
                userLogTimeOut.toLong()
            }

            val formatTime = SimpleDateFormat("HH:mm").format(longTimeStamp)
            val formatTimeOut = SimpleDateFormat("HH:mm").format(longTimeStampOut)

            val showValue = "Date: $userLogDate || Time: $formatTime - ${
                if(userLogTimeOut == ""){
                    "Pending"
                }else{
                    formatTimeOut
                }
            }"
            logTime.text = showValue

            view.setOnClickListener{
                //showing details
                onItemSelected(view, timeLog)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogAdapter {
        var inflater = LayoutInflater.from(parent.context)
        return LogAdapter(inflater.inflate(R.layout.log_list, parent, false))
    }

    override fun onBindViewHolder(holder: LogAdapter, position: Int) {
        holder.bind(logList[position])
    }

    override fun getItemCount(): Int {
       return logList.size
    }

    fun showUserLogList(list: List<UserTimeLog>){
        logList.clear()
        logList.addAll(list)
        notifyDataSetChanged()
    }

}