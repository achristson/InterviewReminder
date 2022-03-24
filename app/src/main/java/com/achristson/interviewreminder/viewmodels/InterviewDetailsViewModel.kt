package com.achristson.interviewreminder.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.achristson.interviewreminder.data.InterviewDatabase
import com.achristson.interviewreminder.models.Interview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InterviewDetailsViewModel(private val app: Application) : AndroidViewModel(app) {

    private val database = InterviewDatabase.getInstance(app)
    lateinit var interview : Interview

    fun getInterview(interviewId : Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                interview = database.interviewDao.getInterviewWithId(interviewId)
            }
        }
    }
}