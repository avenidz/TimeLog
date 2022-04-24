package com.example.timelog.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.timelog.R
import com.example.timelog.databinding.FragmentHomeBinding
import com.example.timelog.viewModel.LogTimeModel
import kotlinx.coroutines.flow.collect


class FragmentHome : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.floatingButtonAdd.setOnClickListener{
            showDialogFragment()
        }

    }


    private val newLogModel: LogTimeModel by viewModels()
    private fun showDialogFragment(){
        val dialog = Dialog(requireContext(), R.style.BottomDialog)
        val thisView = LayoutInflater.from(context).inflate(R.layout.add_new_log,null)
        dialog.setContentView(thisView)

        val layoutParameters = thisView.layoutParams
        layoutParameters.width = resources.displayMetrics.widthPixels
        thisView.layoutParams = layoutParameters

        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        dialog.show()


        //process add log
        val buttonAddLog = dialog.window?.findViewById<Button>(R.id.buttonAddNewLog)
        val textTitle = dialog.window?.findViewById<EditText>(R.id.logTitle)
        val textDescription = dialog.window?.findViewById<EditText>(R.id.logDescription)

        buttonAddLog?.setOnClickListener{
            newLogModel.logMyTime(
                textTitle?.text.toString(),
                textDescription?.text.toString()
            )
        }
        lifecycleScope.launchWhenStarted {
            newLogModel.logTimeState.collect{
                when(it){
                    is LogTimeModel.LogTime.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is LogTimeModel.LogTime.Success -> {
                        Toast.makeText(context, "Log successfully!", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

}