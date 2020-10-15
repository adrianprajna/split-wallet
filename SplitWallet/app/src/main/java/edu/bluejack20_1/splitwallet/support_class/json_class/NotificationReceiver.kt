package edu.bluejack20_1.splitwallet.support_class.json_class

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import edu.bluejack20_1.splitwallet.MainActivity
import edu.bluejack20_1.splitwallet.R
import kotlinx.android.synthetic.main.activity_setting.*

class NotificationReceiver : BroadcastReceiver()
{
    override fun onReceive(p0: Context?, p1: Intent?) {

        var notificationManager = p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var repeatingIntent = Intent(p0, MainActivity::class.java)

        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent = PendingIntent.getActivity(p0, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(p0, "NOTIFICATION CHANNEL")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.icon_notification_active)
            .setContentTitle("Split Wallet")
            .setContentText("It's a great time to manage your wallet!!")
            .setAutoCancel(true)

        if (p1!!.action == "MY_NOTIFICATION_MESSAGE"){
            notificationManager.notify(100, builder.build())
        }
    }

}