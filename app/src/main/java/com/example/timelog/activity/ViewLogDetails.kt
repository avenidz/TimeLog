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
                        binding.detailIn.text = getData[0].timeLogIn

                        val checkOutIfNot = getData[0].timeLogOut

                        if(checkOutIfNot.isEmpty()){
                            binding.detailOut.text = "Pending log"
                            binding.detailButtonLogEnd.isVisible = true
                        }else{
                            binding.detailOut.text = checkOutIfNot
                            binding.detailButtonLogEnd.isVisible = false
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

}