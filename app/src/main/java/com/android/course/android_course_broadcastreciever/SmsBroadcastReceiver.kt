package com.android.course.android_course_broadcastreciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {

    var listener: SmsListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            val extra = intent.extras
            val status = extra?.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    Toast.makeText(
                        context,
                        context?.resources?.getString(R.string.succes_code_message),
                        Toast.LENGTH_LONG
                    ).show();
                    val message = extra.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                    listener?.onCodeReceived(parseMessageWithRegex(message))
                }

                CommonStatusCodes.TIMEOUT -> listener?.onTimeOut()
                else -> throw IllegalStateException("Something going wrong")
            }
        }
    }

    private fun parseMessageWithRegex(message: String?): String =
        REGEX.find(message ?: throw IllegalStateException("Message can't be null"))?.value
            ?: "Error"

    private companion object {
        val REGEX = "[0-9]{6}".toRegex()
    }
}