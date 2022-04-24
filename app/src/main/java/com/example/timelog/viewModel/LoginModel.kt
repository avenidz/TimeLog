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

class LoginModel: ViewModel() {

    private val logUser= MutableStateFlow<LoginUser>(LoginUser.Empty)
    val logUserState : StateFlow<LoginUser> = logUser

    fun logThisUser(context: Context, username: String, password: String)= viewModelScope.launch{
        logUser.value = LoginUser.Loading
        delay(1000)


        if(username == ""){
            logUser.value = LoginUser.Error("Username required.")
        }else if(userNameNotFound(context, username)){
            logUser.value = LoginUser.Error("Username not found.")
        }else if(password ==""){
            logUser.value = LoginUser.Error("Password required.")
        }else if(matchUserNameAndPassword(context, username, password)){
            logUser.value = LoginUser.Error("Username and Password not match.")
        }else{
            updateLoggedUser(context)
            logUser.value = LoginUser.Success
        }

    }

    sealed class LoginUser{
        object Success: LoginUser()
        data class Error(val message: String): LoginUser()
        object Loading: LoginUser()
        object Empty: LoginUser()
    }

    private lateinit var matchUserAndPass : List<LogDataUser>

    private fun updateLoggedUser(context: Context){
        val updateLogin = LogDataUser(matchUserAndPass[0].logId, matchUserAndPass[0].logUser, matchUserAndPass[0].logPass, matchUserAndPass[0].logEmail, true)
        LogDataUserDatabase.getDatabaseInstance(context).logUserDao().updateUser(updateLogin)
    }
    private fun userNameNotFound(context: Context, userName: String):Boolean{
        val checkIfFound: Boolean
        val checkFoundUser = LogDataUserDatabase.getDatabaseInstance(context).logUserDao().checkMatchUser(userName)
        checkIfFound = checkFoundUser.isEmpty()
        return checkIfFound
    }
    private fun matchUserNameAndPassword(context: Context, username: String, password:String):Boolean{
        matchUserAndPass = LogDataUserDatabase.getDatabaseInstance(context).logUserDao().matchUsernamePassword(username,password)
        val checkIfMatch: Boolean = matchUserAndPass.isEmpty()
        return checkIfMatch
    }
}