package com.achristson.interviewreminder.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.achristson.interviewreminder.R
import com.achristson.interviewreminder.databinding.FragmentAddInterviewBinding
import com.achristson.interviewreminder.utils.TimeFormatUtil
import com.achristson.interviewreminder.viewmodels.AddInterviewViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class AddInterviewFragment : Fragment() {

    private lateinit var binding: FragmentAddInterviewBinding
    private val viewModel : AddInterviewViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_interview,
            container,
            false
        )

        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()

        val timePicker: MaterialTimePicker =
            MaterialTimePicker.Builder()
                .setTitleText(R.string.time_picker_text)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

        builder.setTitleText(R.string.date_picker_text)
        val datePicker: MaterialDatePicker<*> = builder.build()

        binding.datePickerButton.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.toString())
        }

        datePicker.addOnPositiveButtonClickListener {
            viewModel.selectedDate = datePicker.headerText
            binding.datePickerButton.text = datePicker.headerText
        }

        binding.timePickerButton.setOnClickListener {
            timePicker.show(childFragmentManager, timePicker.toString())
        }

        timePicker.addOnPositiveButtonClickListener {
            viewModel.hour = timePicker.hour
            viewModel.minute = timePicker.minute
            binding.timePickerButton.text =
                TimeFormatUtil()
                    .formatTime(
                        timePicker.hour,
                        timePicker.minute)
        }

        viewModel.showInvalidDateSnackbar.observe(viewLifecycleOwner) {
            if (it == true){
                Snackbar.make(binding.saveInterview,
                    R.string.invalid_date_selected,
                    Snackbar.LENGTH_SHORT).show()
                viewModel.removeInvalidDateSnackbar()
            }
        }

        viewModel.showInvalidCompanySnackbar.observe(viewLifecycleOwner) {
            if (it == true) {
                Snackbar.make(binding.saveInterview,
                    R.string.invalid_company_selected,
                    Snackbar.LENGTH_SHORT).show()
                viewModel.removeInvalidCompanySnackbar()
            }
        }

        viewModel.showInterviewSavedSnackbar.observe(viewLifecycleOwner) {
            if (it == true){
                Snackbar.make(binding.saveInterview,
                    R.string.interview_saved_message,
                    Snackbar.LENGTH_SHORT).show()
                viewModel.removeInterviewSavedSnackbar()
            }
        }

        viewModel.eventNavigateToInterviews.observe(viewLifecycleOwner) {
            if (it == true){
                findNavController().navigate(AddInterviewFragmentDirections.actionAddInterviewFragmentToInterviewFragment())
                viewModel.navigationToInterviewsComplete()
            }
        }

        viewModel.showErrorSavingInterviewSnackbar.observe(viewLifecycleOwner) {
            if (it == true){
                Snackbar.make(binding.saveInterview,
                    R.string.error_saving_interview_message,
                    Snackbar.LENGTH_SHORT).show()
                viewModel.removeErrorSavingInterviewSnackbar()
            }
        }

        binding.saveInterview.setOnClickListener {
            viewModel.notes = binding.notesValue.text.toString()
            viewModel.saveInterview(binding.companyNameValue.text.toString())
        }

        createChannel(
            getString(R.string.interview_notification_channel_id),
            getString(R.string.interview_notification_channel_name)
        )

        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.interview_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}