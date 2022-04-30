package com.example.timelog.logadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timelog.R
import com.example.timelog.logdata.UserTimeLog

class CalendarViewAdapter: RecyclerView.Adapter<CalendarViewAdapter.CalendarAdapter>() {

    val loglist : MutableList<UserTimeLog> = mutableListOf()

    inner class CalendarAdapter(val view : View) : RecyclerView.ViewHolder(view){
        fun bind(loglistSelected: UserTimeLog){
            val logTitle = view.findViewById<TextView>(R.id.showTitle)
            val logDescription = view.findViewById<TextView>(R.id.showDescription)
            val logTime = view.findViewById<TextView>(R.id.showTimeFrame)

            logTitle.text = loglistSelected.timeLogTitle
            logDescription.text = loglistSelected.timeLogDescription

            val userLogDate = loglistSelected.timeLogDate
            val userLogTime = loglistSelected.timeLogIn
            val dateAndTime = "$userLogDate / $userLogTime"
            logTime.text = dateAndTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter {
        var inflater = LayoutInflater.from(parent.context)
        return CalendarAdapter(inflater.inflate(R.layout.log_list, parent, false))
    }

    override fun onBindViewHolder(holder: CalendarAdapter, position: Int) {
        holder.bind(loglist[position])
    }

    override fun getItemCount(): Int {
        return loglist.size
    }

    fun showSelectedLogList(list: List<UserTimeLog>){
        loglist.clear()
        loglist.addAll(list)
        notifyDataSetChanged()
    }
}
