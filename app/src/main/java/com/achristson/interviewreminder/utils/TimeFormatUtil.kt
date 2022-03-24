package com.achristson.interviewreminder.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.achristson.interviewreminder.models.Interview
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TimeFormatUtil() {

    fun formatTime(hour: Int, minute: Int) :String {
        val pickedHour: Int = hour
        val pickedMinute: Int = minute

        val formattedTime: String = when {
            pickedHour > 12 -> {
                if (pickedMinute < 10) {
                    "${hour - 12}:0${minute} pm"
                } else {
                    "${hour - 12}:${minute} pm"
                }
            }
            pickedHour == 12 -> {
                if (pickedMinute < 10) {
                    "${hour}:0${minute} pm"
                } else {
                    "${hour}:${minute} pm"
                }
            }
            pickedHour == 0 -> {
                if (pickedMinute < 10) {
                    "${hour + 12}:0${minute} am"
                } else {
                    "${hour + 12}:${minute} am"
                }
            }
            else -> {
                if (pickedMinute < 10) {
                    "${hour}:0${minute} am"
                } else {
                    "${hour}:${minute} am"
                }
            }
        }
        return formattedTime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateAndTimeData(interview: Interview): String {
        val month = when (interview.month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            else -> "December"
        }
        val ordinal = when (interview.day) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
        
        return "$month ${interview.day}$ordinal, ${interview.year} at ${formatTime(interview.hour,interview.minute)}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNumberOfDaysDifference(interview: Interview) : Int{
        val dateToday = LocalDate.now()
        val monthString = preprendZero(interview.month)
        val dayString = preprendZero(interview.day)

        val interviewDate = LocalDate.parse(
            "$dayString$monthString${interview.year}",
            DateTimeFormatter.ofPattern("ddMMyyyy"))

        return Duration.between(dateToday.atStartOfDay(), interviewDate.atStartOfDay()).toDays().toInt()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimeTill(interview: Interview) : String{
        var numberOfDaysDifference = getNumberOfDaysDifference(interview)

        var numberOfYears = 0
        while (numberOfDaysDifference > 365){
            numberOfDaysDifference -= 365
            numberOfYears += 1
        }

        var numberOfMonths = 0
        while (numberOfDaysDifference > 30){
            numberOfDaysDifference -= 30
            numberOfMonths += 1
        }

        var timeTill: String

        if (numberOfYears > 0) {
            timeTill = if (numberOfMonths >= 6) {
                if (numberOfYears == 1){
                    "In more than $numberOfYears year"
                } else {
                    "In more than $numberOfYears years"
                }
            } else {
                if (numberOfYears == 1){
                    "In $numberOfYears year"
                } else {
                    "In $numberOfYears years"
                }
            }
        } else if (numberOfMonths > 0){
            timeTill = if (numberOfDaysDifference >= 15){
                if (numberOfMonths == 1) {
                    "In more than $numberOfMonths month"
                } else {
                    "In more than $numberOfMonths months"
                }
            } else {
                if (numberOfMonths == 1){
                    "In $numberOfMonths month"
                } else {
                    "In $numberOfMonths months"
                }
            }
        } else if (numberOfDaysDifference == 0) {
            timeTill = "Today"
        } else if (numberOfDaysDifference != 1){
            timeTill = "In $numberOfDaysDifference days"
        } else {
            timeTill = "In $numberOfDaysDifference day"
        }
        return timeTill
    }

    fun preprendZero(number : Int) : String{
        val numString: String = if (number < 10){
            "0${number}"
        } else {
            number.toString()
        }
        return numString
    }
}