package com.example.timelog.activity

import android.os.Build
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.timelog.databinding.ActivityCalendarViewBinding
import com.example.timelog.logadapter.CalendarViewAdapter
import com.example.timelog.logdata.UserTimeLog
import com.example.timelog.viewModel.CalendarModel
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CalendarLogView: AppCompatActivity(){

    private lateinit var binding: ActivityCalendarViewBinding
    private val calendarModel: CalendarModel by viewModels()

    lateinit var userId: String

    private lateinit var recyclerView : RecyclerView
    val adapter = CalendarViewAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("UserId").toString()

        recyclerView = binding.calendarRecyclerView
        recyclerView.adapter = adapter

        initialDate()

        binding.calendarView.setOnDateChangeListener { view: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            val date = calendar.time

            val formatDate = SimpleDateFormat("yyyy-MM-dd").format(date)

            callModel(formatDate.toString())
        }
        lifecycleScope.launchWhenStarted {
            calendarModel.calendarState.collect{
                when(it){
                    is CalendarModel.CalendarView.Error ->{

                        adapter.showSelectedLogList(it.logListAsSelected)
                        showHideError()

                    }
                    is CalendarModel.CalendarView.Success ->{

                        showHideSuccess()
                        adapter.showSelectedLogList(it.logListAsSelected)
                        sumOfTime(it.logListAsSelected)

                    }
                    else -> Unit
                }
            }
        }

    }
    private fun showHideSuccess(){
        binding.calendarRecyclerView.isVisible = true
        binding.recyclerIsEmpty.isVisible = false
        binding.textEarnedTime.isVisible = true
    }
    private fun showHideError(){
        binding.calendarRecyclerView.isVisible = false
        binding.recyclerIsEmpty.isVisible = true
        binding.textEarnedTime.isVisible = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialDate(){
        val c = Calendar.getInstance()
        val presentDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
        callModel(presentDate.toString())
    }
    private fun callModel(selectedDate: String){
        calendarModel.calendarData(
            binding.calendarView.context,
            userId.toInt(),
            selectedDate
        )
    }
    private fun sumOfTime(timeList: List<UserTimeLog>){ //sum of time with the given selected date or initial
        var previous: Long = 0

        for(i in timeList){
            val login = i.timeLogIn

            var logout: String = if(i.timeLogIn == ""){
                ""
            }else{
                i.timeLogOut
            }

            if(logout == ""){
                previous = previous
            }else{
                var difference = logout.toLong() - login.toLong()
                previous += difference
            }


        }

        val seconds = previous / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, hours.toInt())
        calendar.set(Calendar.MINUTE, minutes.toInt())
        calendar.set(Calendar.SECOND, seconds.toInt())

        val date = calendar.time

        val timeFormat: String = SimpleDateFormat("HH:mm:ss").format(date)
        binding.textEarnedTime.text = "Earned Time: $timeFormat"

    }
}