package com.example.timelog.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.timelog.R
import com.example.timelog.databinding.FragmentHomeBinding
import com.example.timelog.logadapter.FragmentHomeAdapter
import com.example.timelog.logdata.LogDataUserDatabase
import com.example.timelog.viewModel.LogTimeModel
import kotlinx.coroutines.flow.collect
import kotlin.math.log


class FragmentHome : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var logDatabase: LogDataUserDatabase

    private lateinit var recyclerView:RecyclerView
    val adapter = FragmentHomeAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        logDatabase = LogDataUserDatabase.getDatabaseInstance(requireContext())

        recyclerView = view.findViewById(R.id.mainRecycleView)
        recyclerView.adapter = adapter
        loadLogList()

        binding.floatingButtonAdd.setOnClickListener{
            showDialogFragment()
        }

    }


    private val newLogModel: LogTimeModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
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
                buttonAddLog.context,
                textTitle?.text.toString(),
                textDescription?.text.toString(),
                binding.textClock.text as String
            )
        }
        lifecycleScope.launchWhenStarted {
            newLogModel.logTimeState.collect{
                when(it){
                    is LogTimeModel.LogTime.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is LogTimeModel.LogTime.Success -> {
                        loadLogList()
                        textTitle?.text?.clear()
                        textDescription?.text?.clear()
                        Toast.makeText(context, "Log successfully!", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    private  fun loadLogList(){
        val getUserData = logDatabase.logUserDao().checkLoggedUser()
        val userId = getUserData[0].logId
        val logList = logDatabase.logUserDao().getUserTimeLog(userId)
        adapter.showUserLogList(logList)
    }

}