package com.achristson.interviewreminder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.achristson.interviewreminder.InterviewAdapter
import com.achristson.interviewreminder.R
import com.achristson.interviewreminder.databinding.FragmentInterviewsBinding
import com.achristson.interviewreminder.viewmodels.InterviewViewModel

class InterviewFragment : Fragment() {

    private val viewModel : InterviewViewModel by viewModels()
    private lateinit var binding : FragmentInterviewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_interviews,
            container,
            false)

        binding.viewModel = viewModel

        val adapter = InterviewAdapter(
            InterviewAdapter.OnClickListener{
                viewModel.navigateToInterviewDetails(it.interviewId)
            }
        )
        binding.interviewRecyclerView.adapter = adapter

        viewModel.navigateToInterviewDetails.observe(viewLifecycleOwner){
            if (it != null){
                findNavController().navigate(InterviewFragmentDirections.actionInterviewFragmentToInterviewDetailsFragment(it))
                viewModel.navigateToInterviewDetailsComplete()
            }
        }

        viewModel.interviews.observe(viewLifecycleOwner) {
            if (it == null || it.isEmpty()){
                binding.noDataTextView.visibility = View.VISIBLE
            } else {
                binding.noDataTextView.visibility = View.INVISIBLE
            }
            adapter.submitList(it)
        }

        viewModel.eventNavigateToAddInterview.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController().navigate(InterviewFragmentDirections.actionInterviewFragmentToAddInterviewFragment())
                viewModel.onNavigationToAddInterviewComplete()
            }
        }

        return binding.root
    }
}