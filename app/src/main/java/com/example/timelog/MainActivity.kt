package com.example.timelog

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.timelog.activity.ActivityLoginSignup
import com.example.timelog.activity.CalendarLogView
import com.example.timelog.databinding.ActivityMainBinding
import com.example.timelog.logdata.LogDataUser
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.viewModel.LogUserModel
import com.google.android.material.navigation.NavigationView

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var backPressed: Boolean = false
    lateinit var itLogUserData: List<LogDataUser>


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       val checkUserLog = LogDataUserDatabase.getDatabaseInstance(this).logUserDao().checkLoggedUser()

        if(checkUserLog.isEmpty()){//check if userdatabase is empty
            startActivity(
                Intent(this, ActivityLoginSignup::class.java)
            )
        }else{
            showFragment()

            showLogUserInfo(checkUserLog)//with viewModel
        }

    }

    override fun onBackPressed() {

                    if(backPressed){
                        finishAffinity()
                    }else{
                        Toast.makeText(this, "Tap again to exit!", Toast.LENGTH_SHORT).show()
                        backPressed = true
                    }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflateMenu = menuInflater
        inflateMenu.inflate(R.menu.menu_calendar_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_calendar -> {
                startActivity(
                    Intent(
                        this, CalendarLogView::class.java
                    ).putExtra("UserId",itLogUserData[0].logId.toString())
                )
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragment(){
        val navigationController = findNavController(R.id.fragmentFrameLayout)
        val bottomNavigation = binding.navigationDrawerView
        bottomNavigation.setupWithNavController(navigationController)

        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.drawerHome,
                R.id.drawerSettings,
                R.id.drawerLogout
            )
        )
        setupActionBarWithNavController(navigationController, appBarConfiguration)
    }


    lateinit var viewModel:LogUserModel

    private fun showLogUserInfo(checkUserLog: List<LogDataUser>) {
        viewModel = ViewModelProvider(this).get(LogUserModel::class.java)

        //extract record from database
        viewModel.viewUserLog = checkUserLog
        viewModel.viewUserLogAlready.value = viewModel.viewUserLog

        viewModel.viewUserLogAlready.observe(this, Observer {
            itLogUserData = it

            //showing username at drawer header
            val navigationView: NavigationView=binding.navigationDrawerView
            val headerView: View = navigationView.getHeaderView(0)
            val textUserView: TextView = headerView.findViewById(R.id.header_log_user)
            textUserView.text = itLogUserData[0].logUser

        })
    }

}