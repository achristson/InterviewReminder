package com.achristson.interviewreminder.network

fun parseResponse(jsonResponse : String) : String{
    val jsonArray = jsonResponse.split(" ")
    var logoArrayString = ""

    for (item in jsonArray){
        if (item.contains("logo")){
            logoArrayString = item.split("=")[1].dropLast(1)
        }
    }

    return logoArrayString
}