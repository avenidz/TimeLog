package com.example.timelog.activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.timelog.callback.ViewLogDetailsCallback
import com.example.timelog.databinding.LayoutViewlogdetailsBinding
import com.example.timelog.viewModel.ViewDetailsModel
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat

class ViewLogDetails : AppCompatActivity(),ViewLogDetailsCallback {

    private lateinit var binding: LayoutViewlogdetailsBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = LayoutViewlogdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getId = intent.getStringExtra("logId")

        if (getId != null) {
            showingDetails(getId.toInt())
        }

        binding.detailButtonLogEnd.setOnClickListener{
            if (getId != null) {
                pendingLogUpdate(binding.detailButtonLogEnd.context, getId.toInt())
                showingDetails(getId.toInt())
            }
        }

    }



    private val detailModel: ViewDetailsModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showingDetails(detailId: Int){
        detailModel.detailModelData(this, detailId)
        lifecycleScope.launchWhenStarted {
            detailModel.detailModelstate.collect{
                when(it){
                    is ViewDetailsModel.DetailModel.Error -> {
                        Toast.makeText(applicationContext,it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ViewDetailsModel.DetailModel.Success -> {
                        val getData = it.logData
                        binding.detailTitle.text = getData[0].timeLogTitle
                        binding.detailDescription.text = getData[0].timeLogDescription
                        binding.detailDate.text = getData[0].timeLogDate

                        val timeLogin = getData[0].timeLogIn
                        val timeToLongIn = timeLogin.toLong()
                        val convertToTimeIn = SimpleDateFormat("HH:mm").format(timeToLongIn)
                        binding.detailIn.text = convertToTimeIn



                        val checkOutIfNot = getData[0].timeLogOut

                        if(checkOutIfNot.isEmpty()){
                            binding.detailOut.text = "Pending log"
                            binding.detailButtonLogEnd.isVisible = true
                        }else{
                            val timeToLong = checkOutIfNot.toLong()
                            val convertToTimeOut = SimpleDateFormat("HH:mm").format(timeToLong)

                            binding.detailOut.text = convertToTimeOut
                            binding.detailButtonLogEnd.isVisible = false

                            var listOfTime = listOf(getData[0].timeLogIn, getData[0].timeLogOut)
                            var sumOfTime = countTime(listOfTime)
                            binding.logCount.isVisible = true
                            binding.logCount.text = "Total time: $sumOfTime"
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

}