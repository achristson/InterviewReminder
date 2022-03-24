package com.achristson.interviewreminder

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.achristson.interviewreminder.models.Interview
import com.achristson.interviewreminder.utils.TimeFormatUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("timeData")
fun TextView.setTimeFormat(item : Interview){
    text = TimeFormatUtil().formatDateAndTimeData(item)
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("timeTill")
fun TextView.setTimeTillFormat(item : Interview){
    text = TimeFormatUtil().formatTimeTill(item)
}

@BindingAdapter("logoUrl")
fun setLogo(imageView : ImageView, item: Interview){
    Glide.with(imageView.context)
        .load(item.logoSrcUrl)
        .apply(RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image))
        .into(imageView)
}