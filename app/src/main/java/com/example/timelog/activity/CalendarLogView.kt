package com.example.timelog.activity

import android.os.Build
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.timelog.databinding.ActivityCalendarViewBinding
import com.example.timelog.logadapter.CalendarViewAdapter
import com.example.timelog.viewModel.CalendarModel
import kotlinx.coroutines.flow.collect
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
            val modifiedMonth = String.format("%02d", month+1)
            val selectedDate = "$year-${modifiedMonth}-$dayOfMonth"
            callModel(selectedDate)
        }
        lifecycleScope.launchWhenStarted {
            calendarModel.calendarState.collect{
                when(it){
                    is CalendarModel.CalendarView.Error ->{
                        adapter.showSelectedLogList(it.logListAsSelected)
                        binding.calendarRecyclerView.isVisible = false
                        binding.recyclerIsEmpty.isVisible = true
                    }
                    is CalendarModel.CalendarView.Success ->{
                        binding.calendarRecyclerView.isVisible = true
                        binding.recyclerIsEmpty.isVisible = false
                        adapter.showSelectedLogList(it.logListAsSelected)
                    }
                    else -> Unit
                }
            }
        }

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
}