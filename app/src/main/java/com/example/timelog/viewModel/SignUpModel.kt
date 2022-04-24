package com.example.timelog.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timelog.logdata.LogDataUser
import com.example.timelog.logdata.LogDataUserDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpModel : ViewModel(){

    private val addUser = MutableStateFlow<AddUser>(AddUser.Empty)
    val addUserState : StateFlow<AddUser> = addUser

    fun addNewUser(context: Context, userName: String, userPassA: String, userPassB: String, userEmail: String) = viewModelScope.launch {

        addUser.value = AddUser.Loading
        delay(1000)

        if(userName == ""){
            addUser.value = AddUser.Error("Username required.")
        }else if(matchUser(context, userName)){
            addUser.value = AddUser.Error("Try another username.")
        }else if(userPassA == ""){
            addUser.value = AddUser.Error("Password required.")
        }else if(userPassA != userPassB){
            addUser.value = AddUser.Error("Password not match.")
        }else if(userEmail == ""){
            addUser.value = AddUser.Error("Email required.")
        }else{
            saveUser(context, userName, userPassA, userEmail)
            addUser.value = AddUser.Success
        }

    }

    sealed class AddUser{

        object Success: AddUser()
        data class Error(val message: String) :AddUser()
        object Empty: AddUser()
        object Loading: AddUser()

    }

    private fun saveUser(context: Context, userName: String, userPass: String, userEmail: String) {

        val addNewUser = LogDataUser(0, userName, userPass, userEmail, true)
        LogDataUserDatabase.getDatabaseInstance(context).logUserDao().saveUser(addNewUser)

    }
    private fun matchUser(context: Context, userName: String): Boolean{
        val returnValue: Boolean
        val checkMatchUser = LogDataUserDatabase.getDatabaseInstance(context).logUserDao().checkMatchUser(userName)
        returnValue = checkMatchUser.isNotEmpty()
        return returnValue
    }
}