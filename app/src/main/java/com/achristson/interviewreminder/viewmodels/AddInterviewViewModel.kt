package com.achristson.interviewreminder.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.achristson.interviewreminder.alarmservice.setAlarm
import com.achristson.interviewreminder.data.InterviewDatabase
import com.achristson.interviewreminder.models.Interview
import com.achristson.interviewreminder.network.LogoApi
import com.achristson.interviewreminder.network.parseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class AddInterviewViewModel(private val app: Application) : AndroidViewModel(app){

    val database = InterviewDatabase.getInstance(app)

    private lateinit var interview : Interview

    private val _showInvalidDateSnackbar = MutableLiveData<Boolean>()
    val showInvalidDateSnackbar : LiveData<Boolean> = _showInvalidDateSnackbar

    private val _showInvalidCompanySnackbar = MutableLiveData<Boolean>()
    val showInvalidCompanySnackbar : LiveData<Boolean> = _showInvalidCompanySnackbar

    private val _showInterviewSavedSnackbar = MutableLiveData<Boolean>()
    val showInterviewSavedSnackbar : LiveData<Boolean> = _showInterviewSavedSnackbar

    private val _showErrorSavingInterviewSnackbar = MutableLiveData<Boolean>()
    val showErrorSavingInterviewSnackbar : LiveData<Boolean> = _showErrorSavingInterviewSnackbar

    private val _eventNavigateToInterviews = MutableLiveData<Boolean>()
    val eventNavigateToInterviews : LiveData<Boolean> = _eventNavigateToInterviews

    var selectedDate : String? = null
    var selectedDay : Int? = null
    var selectedMonth : Int? = null
    var selectedYear : Int? = null
    var hour : Int? = null
    var minute : Int? = null
    lateinit var company : String
    lateinit var notes : String

    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.O)
    fun validateDateAndTime() : Boolean{

        if (selectedDate == null ||
                hour == null ||
                minute == null) {
            _showInvalidDateSnackbar.value = true
            return false
        }

        val dateToday = LocalDate.now().toString().split("-")
        val currentDay = dateToday[2].toInt()
        val currentMonth = dateToday[1].toInt()
        val currentYear = dateToday[0].toInt()

        val selectedDateList = selectedDate!!.split(" ")
        selectedDay = selectedDateList[1].dropLast(1).toInt()
        selectedMonth = when (selectedDateList[0]) {
            "Jan" -> 1
            "Feb" -> 2
            "Mar" -> 3
            "Apr" -> 4
            "May" -> 5
            "Jun" -> 6
            "Jul" -> 7
            "Aug" -> 8
            "Sep" -> 9
            "Oct" -> 10
            "Nov" -> 11
            else -> 12
        }
        selectedYear = selectedDateList[2].toInt()

        val currentDate = Date(
            currentYear,
            currentMonth,
            currentDay,
            LocalTime.now().hour,
            LocalTime.now().minute)

        val newDate = Date(
            selectedYear!!,
            selectedMonth!!,
            selectedDay!!,
            hour!!,
            minute!!)

        return if (newDate < currentDate){
            _showInvalidDateSnackbar.value = true
            false
        } else {
            true
        }
    }

    fun removeInvalidDateSnackbar(){
        _showInvalidDateSnackbar.value = false
    }

    fun validateCompanyName(company : String?) : Boolean{
        return if (company == null) {
            _showInvalidCompanySnackbar.value = true
            false
        } else {
            true
        }
    }

    fun removeInvalidCompanySnackbar() {
        _showInvalidCompanySnackbar.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveInterview(company: String?){
        if (validateDateAndTime() && validateCompanyName(company)){
            interview = Interview(company!!,selectedDay!!,selectedMonth!!,selectedYear!!,hour!!,minute!!,notes)
            viewModelScope.launch {
                try {
                    val companyDomain = "http://${interview.companyName}.com"
                    val companyData = LogoApi.retrofitService.getLogo(companyDomain).toString()
                    val logoUrl = parseResponse(companyData)
                    interview.logoSrcUrl = logoUrl
                    withContext(Dispatchers.IO){
                        database.interviewDao.insert(interview)
                        val recentInterview = database.interviewDao.getRecentInterview()
                        setAlarm(app, recentInterview)
                    }
                    _showInterviewSavedSnackbar.postValue(true)
                    _eventNavigateToInterviews.postValue(true)
                } catch (e : Exception){
                    Log.e("save", "${e.message}")
                }
            }
        } else {
            _showErrorSavingInterviewSnackbar.value = true
        }
    }

    fun removeInterviewSavedSnackbar(){
        _showInterviewSavedSnackbar.value = false
    }

    fun removeErrorSavingInterviewSnackbar(){
        _showErrorSavingInterviewSnackbar.value = false
    }

    fun navigationToInterviewsComplete(){
        _eventNavigateToInterviews.value = false
    }
}