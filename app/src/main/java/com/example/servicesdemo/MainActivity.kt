package com.example.servicesdemo

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.servicesdemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var builder: Notification.Builder
    private val description = "Test notification"

    private val channelId = "12345"

    lateinit var reciver: AirplaneModeChangeReceiver

    private val NOTIFICATION_TITLE = "Notification Sample App"
    private val CONTENT_TEXT = "Expand me to see a detailed message!"

    @SuppressLint("RemoteViewLayout")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        reciver = AirplaneModeChangeReceiver(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val  mEditText = binding.editText
        binding.btnShowNotification.setOnClickListener {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Notification_View::class.java)


            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


            val collapsedView = RemoteViews(packageName,R.layout.activity_notification_view)
            collapsedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME))


            val expandedView = RemoteViews(packageName, R.layout.view_notification_expanded)
            expandedView.setTextViewText(
                R.id.timestamp,
                DateUtils.formatDateTime(
                    this,
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                )
            )
            expandedView.setTextViewText(R.id.notification_message, mEditText.text)

            // checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setSmallIcon(R.drawable.man)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(CONTENT_TEXT)
                    .setCustomContentView(collapsedView)
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setCustomBigContentView(expandedView)
                    // setting style to DecoratedCustomViewStyle() is necessary for custom views to display
                    .setStyle(Notification.DecoratedCustomViewStyle())

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder = Notification.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                this.resources,
                                R.drawable.ic_launcher_background
                            )
                        )
                        .setContentIntent(pendingIntent)
                }
            }
            notificationManager.notify(1234, builder.build())
        }

        binding.btnStartService.setOnClickListener {
            startService(Intent(this, NewService::class.java))
        }
        binding.btnStopService.setOnClickListener {
            stopService(Intent(this, NewService::class.java))
        }

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(reciver,it)
        }




    }

    override fun onPause() {
        super.onPause()
        stopService(Intent(this, NewService::class.java))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(reciver)
    }
}
