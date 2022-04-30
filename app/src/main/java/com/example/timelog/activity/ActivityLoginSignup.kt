package com.example.timelog.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.timelog.R
import com.example.timelog.databinding.ActivityLoginBinding

class ActivityLoginSignup: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var backPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showFragments()
    }

    override fun onBackPressed() {
        if(backPressed){
            finishAffinity()
        }else{
            Toast.makeText(this, "Tap again to exit!", Toast.LENGTH_SHORT).show()
            backPressed = true
        }
    }

    private fun showFragments(){

        val navigationController = findNavController(R.id.fragmentLogSign)
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.setupWithNavController(navigationController)

        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.bottomLogin,
                R.id.bottomSignup
            )
        )
        setupActionBarWithNavController(navigationController, appBarConfiguration)
    }
}