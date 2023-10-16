package com.android.course.android_course_broadcastreciever

import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.phone.SmsRetriever


class MainActivity : AppCompatActivity() {

    private lateinit var codeTextView: TextView
    private lateinit var codeStatusView: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        val broadcastReceiver = createReceiver()
        val intentFilter = createIntentFilter()

        applicationContext.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun createReceiver(): SmsBroadcastReceiver =
        SmsBroadcastReceiver().apply {
            this.listener = object : SmsListener {
                override fun onCodeReceived(code: String) {
                    codeTextView.text = code
                    codeStatusView.apply {
                        text = applicationContext.resources.getString(R.string.succes_code_message)
                        setTextColor(resources.getColor(R.color.green))
                        isVisible = true
                    }
                }

                override fun onTimeOut() {
                    codeTextView.text =
                        applicationContext.resources.getString(R.string.time_out_code_message)
                }
            }
        }

    private fun createIntentFilter(): IntentFilter =
        IntentFilter().apply {
            this.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        }

    private fun startSmsListener() {
        val smsRetrieverCli = SmsRetriever.getClient(this)
        val task = smsRetrieverCli.startSmsRetriever()

        task.addOnSuccessListener {
            Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
        }
        task.addOnFailureListener {
            Toast.makeText(this, "SMS Retriever fail", Toast.LENGTH_LONG).show();
        }
    }

    private fun initViews() {
        codeTextView = findViewById(R.id.code_text_view)
        button = findViewById<Button?>(R.id.button).apply {
            setOnClickListener {
                startSmsListener()
            }
        }
        codeStatusView = findViewById<TextView?>(R.id.code_status_view).apply {
            isGone = true
        }
    }
}
