package com.achristson.interviewreminder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.achristson.interviewreminder.R
import com.achristson.interviewreminder.databinding.FragmentInterviewDetailsBinding
import com.achristson.interviewreminder.viewmodels.InterviewDetailsViewModel

class InterviewDetailsFragment : Fragment() {

    private val viewModel: InterviewDetailsViewModel by viewModels()
    private lateinit var binding: FragmentInterviewDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_interview_details,
            container,
            false
        )
        binding.viewModel = viewModel
        val interviewDetailFragmentArgs by navArgs<InterviewDetailsFragmentArgs>()
        viewModel.getInterview(interviewDetailFragmentArgs.interviewId)

        return binding.root
    }
}