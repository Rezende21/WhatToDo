package com.oquefazer.ui.fragment.add

import android.os.Bundle
import android.util.Log
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
import com.oquefazer.databinding.FragmentAddTaskBinding
import com.oquefazer.extencions.convertDataToLong
import com.oquefazer.extencions.format
import com.oquefazer.extencions.text
import com.oquefazer.notification.SetNotification
import com.oquefazer.other.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private val binding by lazy { FragmentAddTaskBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<WhatsViewModel>()
    private var timeMilliseconds = 3L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        insertListeners()
        getThegoogleMob()
        return binding.root
    }

    private fun getThegoogleMob() {
        MobileAds.initialize(requireContext()){}
        val adRequest = AdRequest.Builder().build()
        binding.adViewAdd.loadAd(adRequest)
    }

    private fun insertListeners() {
        binding.editData.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                timeMilliseconds = it
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.editData.text = Date(it + offset).format()
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
        }

        binding.editHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener{
                val hora = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.editHora.text = "${hora}:${minute}"

                val timeConverter = convertDataToLong(minute.toString().toLong(), hora.toString().toLong())

                timeMilliseconds += timeConverter

            }
            timePicker.show(parentFragmentManager, null)
        }

        binding.btCancelar.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btCriar.setOnClickListener {
            val taskName = binding.editNome.text
            val hour = timeMilliseconds
            val message = binding.editMessage.text

            viewModel.verifyTaskValue(taskName, message, hour)
            viewModel.taskStatus.observe(requireActivity()) {
                when(it.status) {
                    Status.SUCCESS -> {
                        scheduleNotification(taskName, hour)
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun scheduleNotification(title : String, hour : Long) {
        viewModel.NotificationStatus.observe(requireActivity()) {
            when(it) {
                true -> {
                    val service = SetNotification(requireContext())
                    service.createNotificationChannel(1, title)
                    service.scheduleNotification(title, hour)
                    activity?.onBackPressed()
                }
                false -> {
                    activity?.onBackPressed()
                }
            }
        }

    }
}