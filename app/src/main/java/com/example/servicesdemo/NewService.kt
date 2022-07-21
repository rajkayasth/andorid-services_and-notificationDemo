package com.example.servicesdemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class NewService : Service() {

    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var builder: Notification.Builder
    private val description = "Test notification"

    private val channelId = "12345"

    private lateinit var player: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)

        // providing the boolean
        // value as true to play
        // the audio on loop
        player.isLooping = true

        // starting the process
        player.start()

        val intent = Intent(this, Notification_View::class.java)


        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.man)
                .setContentTitle("service Starred")
                .setContentText(
                    "caller tune is ringing"
                )
                .setColor(ContextCompat.getColor(this, R.color.notification_color))
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
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

        // returns the status
        // of the program
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // stopping the process
        player.stop()
    }
}