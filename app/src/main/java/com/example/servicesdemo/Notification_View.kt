package com.example.servicesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.servicesdemo.databinding.ActivityNotificationViewBinding

class Notification_View : AppCompatActivity() {
    private var _binding: ActivityNotificationViewBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_view)

//        val message = intent.getStringExtra("message")
//        binding.textView.text = message


    }
}