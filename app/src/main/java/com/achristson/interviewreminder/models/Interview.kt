package com.achristson.interviewreminder.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "interview_table")
data class Interview(

    val companyName : String,
    val day : Int,
    val month : Int,
    val year : Int,
    val hour : Int,
    val minute : Int,
    var notes : String = "",
    @Json(name="logoUrl") var logoSrcUrl : String = "",

    @PrimaryKey(autoGenerate = true)
    val interviewId : Long = 0L
)
