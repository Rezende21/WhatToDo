package com.oquefazer.ui.fragment.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.oquefazer.ui.fragment.viewmodel.WhatsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.oquefazer.data.local.WhatsToDo
import com.oquefazer.databinding.FragmentEditTaskBinding
import com.oquefazer.extencions.convertDataToLong
import com.oquefazer.extencions.format
import com.oquefazer.extencions.text
import com.oquefazer.other.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditTaskFragment : Fragment() {

    private lateinit var task: WhatsToDo
    private val binding by lazy { FragmentEditTaskBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<WhatsViewModel>()
    private var timeMilliseconds = 3L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        task = arguments?.getSerializable("id") as WhatsToDo
        initListenes(task.id)
        getThegoogleMob()
        getthetext()
        return binding.root
    }

    private fun getThegoogleMob() {
        MobileAds.initialize(requireContext()){}
        val adRequest = AdRequest.Builder().build()
        binding.adViewEdit.loadAd(adRequest)
    }

    private fun getthetext() {
        binding.editNomeEditTask.text = task.title
        binding.editMessage.text = task.message

        binding.editHoraEditTask.text = task.hour.toString()
    }

    private fun initListenes(id : Int?) {
        binding.editDataEditTask.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                timeMilliseconds = it
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.editDataEditTask.text = Date(it + offset).format()
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
        }

        binding.editHoraEditTask.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener{
                val hora = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.editHoraEditTask.text = "${hora}:${minute}"

                val timeConverter = convertDataToLong(minute.toString().toLong(), hora.toString().toLong())

                timeMilliseconds += timeConverter
            }
            timePicker.show(parentFragmentManager, null)
        }

        binding.btCriarEditTask.setOnClickListener {
            updateTask(id)
        }

        binding.toolbar.setNavigationOnClickListener{
            activity?.onBackPressed()
        }

        binding.btCancelarEditTask.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun updateTask(id: Int?) {
        val taskName = binding.editNomeEditTask.text
        val taskHour = timeMilliseconds
        //val taskData = binding.editDataEditTask.text
        val message = binding.editMessage.text
        viewModel.checkTaskValue(id!!, taskName, message, taskHour)
        viewModel.taskEdit.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    activity?.onBackPressed()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}