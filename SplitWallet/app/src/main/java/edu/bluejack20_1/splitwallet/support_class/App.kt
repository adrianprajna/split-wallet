package edu.bluejack20_1.splitwallet.support_class

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel = NotificationChannel("NOTIFICATION CHANNEL", "NOTIFICATION CHANNEL", NotificationManager.IMPORTANCE_HIGH)

            channel.description = "This is notification channel"

            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}