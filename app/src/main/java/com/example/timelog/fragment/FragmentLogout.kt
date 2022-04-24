package com.example.timelog.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timelog.R
import com.example.timelog.activity.ActivityLoginSignup
import com.example.timelog.databinding.FragmentLogoutBinding
import com.example.timelog.logdata.LogDataUser
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.viewModel.LogUserModel

class FragmentLogout : Fragment(R.layout.fragment_logout) {


    private lateinit var binding: FragmentLogoutBinding
    private var confirmLogout: Boolean = false
    private lateinit var databaseInstance : LogDataUserDatabase
    private lateinit var extractUserData: List<LogDataUser>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLogoutBinding.bind(view)

        databaseInstance = LogDataUserDatabase.getDatabaseInstance(requireContext())
        extractUserData= databaseInstance.logUserDao().checkLoggedUser()

        showLogUserName()

        binding.buttonLogout.setOnClickListener{
            when(confirmLogout){
                false -> {
                    Toast.makeText(requireContext(), "Try again to logout.", Toast.LENGTH_SHORT).show()
                    confirmLogout = true
                }
                true -> {
                    updateUserLogToFalse()
                    confirmLogout = false
                    startActivity(
                        Intent(
                            requireContext(), ActivityLoginSignup::class.java
                        )
                    )
                }
            }
        }
    }

    lateinit var userLogModel: LogUserModel
    lateinit var itData: List<LogDataUser>

    private fun showLogUserName(){

        userLogModel = ViewModelProvider(this).get(LogUserModel::class.java)
        userLogModel.viewUserLog =extractUserData
        userLogModel.viewUserLogAlready.value = userLogModel.viewUserLog

        userLogModel.viewUserLogAlready.observe(viewLifecycleOwner, Observer {
            itData = it

                binding.userName.text = itData[0].logUser

        })
    }
    private fun updateUserLogToFalse(){

        val userData = LogDataUser(itData[0].logId, itData[0].logUser, itData[0].logPass, itData[0].logEmail, false)
        databaseInstance.logUserDao().updateUser(userData)
    }

}