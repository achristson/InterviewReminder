package com.achristson.interviewreminder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.achristson.interviewreminder.data.InterviewDatabase
import com.achristson.interviewreminder.models.Interview

class InterviewViewModel(private val app: Application) : AndroidViewModel(app) {

    private val database = InterviewDatabase.getInstance(app)

    private val _interviews = database.interviewDao.getAllInterviews()
    val interviews : LiveData<List<Interview>>
        get() = _interviews

    private val _eventNavigateToAddInterview = MutableLiveData<Boolean>()
    val eventNavigateToAddInterview : LiveData<Boolean> = _eventNavigateToAddInterview

    private val _navigateToInterviewDetails = MutableLiveData<Long?>()
    val navigateToInterviewDetails : LiveData<Long?> = _navigateToInterviewDetails

    fun onNavigateToAddInterview(){
        _eventNavigateToAddInterview.value = true
    }

    fun onNavigationToAddInterviewComplete(){
        _eventNavigateToAddInterview.value = false
    }

    fun navigateToInterviewDetails(interviewId: Long){
        _navigateToInterviewDetails.value = interviewId
    }

    fun navigateToInterviewDetailsComplete(){
        _navigateToInterviewDetails.value = null
    }
}