package com.example.timelog.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.logdata.UserTimeLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewDetailsModel: ViewModel() {

    private val detailModel= MutableStateFlow<DetailModel>(DetailModel.Empty)
    val detailModelstate = detailModel.asStateFlow()

    private lateinit var returnData: List<UserTimeLog>

    fun detailModelData(context: Context, detailId: Int) = viewModelScope.launch {
        detailModel.value = DetailModel.Loading
        delay(300)

        returnData = returnDetails(context, detailId)

        if(returnData.isEmpty()){
            detailModel.value = DetailModel.Error("Empty record!")
        }else{
            detailModel.value = DetailModel.Success(returnData)
        }

    }

    sealed class DetailModel{
        object Empty: DetailModel()
        object Loading: DetailModel()
        data class Error(val message: String): DetailModel()
        data class Success(val logData:List<UserTimeLog>): DetailModel()
    }

    private fun returnDetails(context: Context, detailId: Int): List<UserTimeLog>{
        //return data from usertimelog table where usertimelogid is equal to given id
        return LogDataUserDatabase.getDatabaseInstance(context).logUserDao().getLogDetailsById(detailId)
    }
}