package com.example.timelog.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.timelog.MainActivity
import com.example.timelog.R
import com.example.timelog.databinding.FragmentSignupBinding
import com.example.timelog.viewModel.SignUpModel
import kotlinx.coroutines.flow.collect


class SignupFragment : Fragment(R.layout.fragment_signup) {


    private lateinit var binding: FragmentSignupBinding
    private val userAddModel: SignUpModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.bind(view)

        binding.buttonSignup.setOnClickListener{
            userAddModel.addNewUser(
                binding.buttonSignup.context,
                binding.signUser.text.toString(),
                binding.signPassA.text.toString(),
                binding.signPassB.text.toString(),
                binding.signEmail.text.toString()
            )
        }
        lifecycleScope.launchWhenStarted {
            userAddModel.addUserState.collect{
                when(it){
                    is SignUpModel.AddUser.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is SignUpModel.AddUser.Success -> {
                        Toast.makeText(context, "You are now log-in!", Toast.LENGTH_SHORT).show()

                        startActivity(
                            Intent(
                                context, MainActivity::class.java
                            )
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

}