package com.android.course.android_course_broadcastreciever

interface SmsListener {
    fun onCodeReceived(code: String)
    fun onTimeOut()
}