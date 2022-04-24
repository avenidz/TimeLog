package com.example.timelog.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.timelog.MainActivity
import com.example.timelog.R
import com.example.timelog.databinding.FragmentLoginBinding
import com.example.timelog.databinding.FragmentLogoutBinding
import com.example.timelog.viewModel.LoginModel
import kotlinx.coroutines.flow.collect


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val userLoginModel: LoginModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.buttonLogin.setOnClickListener{
            userLoginModel.logThisUser(
                binding.buttonLogin.context,
                binding.logUsername.text.toString(),
                binding.logPassword.text.toString(),
            )
        }
        lifecycleScope.launchWhenStarted {
            userLoginModel.logUserState.collect {
                when(it){
                    is LoginModel.LoginUser.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is LoginModel.LoginUser.Success -> {
                        Toast.makeText(context, "You are now logged in.", Toast.LENGTH_SHORT).show()

                        startActivity(
                            Intent(
                                context, MainActivity::class.java
                            )
                        )
                    }else -> Unit
                }
            }
        }
    }
}