package com.example.timelog.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timelog.logdata.LogDataUser
import com.example.timelog.logdata.LogDataUserDatabase

class LogUserModel: ViewModel() {

    var viewUserLog = listOf<LogDataUser>()
    val viewUserLogAlready : MutableLiveData<List<LogDataUser>> by lazy {
        MutableLiveData<List<LogDataUser>>()
    }
}