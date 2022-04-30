package com.example.timelog.logadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timelog.R
import com.example.timelog.logdata.UserTimeLog

class FragmentHomeAdapter: RecyclerView.Adapter<FragmentHomeAdapter.LogAdapter>(){

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
            val dateAndTime = "$userLogDate / $userLogTime"
            logTime.text = dateAndTime

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