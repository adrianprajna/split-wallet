package edu.bluejack20_1.splitwallet

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.viewpager.widget.ViewPager
import edu.bluejack20_1.splitwallet.R
import edu.bluejack20_1.splitwallet.adapter.PageAdapter
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig
import edu.bluejack20_1.splitwallet.support_class.Users
import edu.bluejack20_1.splitwallet.support_class.json_class.NotificationReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var preferenceConfig : PreferenceConfig

    lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceConfig =
            PreferenceConfig(
                applicationContext
            )
        if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPagerAdapter()
        setBottomNav()
        setViewPagerListener()

        notificationManager = NotificationManagerCompat.from(this)
//        createNotification()
        sendOnNOtificationChannel()
    }

    fun sendOnNOtificationChannel(){
        createNotification()
    }

    fun createNotification(){
        val u : Users
        u = preferenceConfig.getGson().fromJson(preferenceConfig.getString(Constants.KEY_USER), Users::class.java)
        if (u.notification == "true" && preferenceConfig.loadNotification() == "false"){
            preferenceConfig.putString(Constants.NOTIFICATION_STATUS, "true")

            var calendar = Calendar.getInstance()

            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 51)
            calendar.set(Calendar.SECOND, 20)

            var intent = Intent(this, NotificationReceiver::class.java)
            intent.setAction("MY_NOTIFICATION_MESSAGE")

            var pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT )

            var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            Toast.makeText(this, "Notification Active", Toast.LENGTH_SHORT).show()

        } else if (u.notification == "false" && preferenceConfig.loadNotification() == "true"){
            preferenceConfig.putString(Constants.NOTIFICATION_STATUS, "false")

            var intent = Intent(this, NotificationReceiver::class.java)

            var pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT )

            var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "Notification Disable", Toast.LENGTH_SHORT).show()
        }
    }

    var exit = false

    private fun setViewPagerListener(){
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                bottom_nav.selectedItemId = when(position){
                    0 -> R.id.nav_home
                    1 -> R.id.nav_calendar
                    2 -> R.id.nav_transaction
                    3 -> R.id.nav_report
                    4 -> R.id.nav_wallet
                    else -> R.id.nav_home
                }
            }

        })
    }

    private fun setBottomNav(){
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            viewPager.currentItem = when(item.itemId) {
                R.id.nav_home -> 0
                R.id.nav_calendar -> 1
                R.id.nav_transaction -> 2
                R.id.nav_report -> 3
                R.id.nav_wallet -> 4
                else -> 0
            }

            true
        }
    }

    private fun setViewPagerAdapter(){
        viewPager.adapter =
            PageAdapter(supportFragmentManager)
    }

    @Override
    override fun onBackPressed() {
        if (exit){
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        } else {
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
            Handler().postDelayed(Runnable { exit = false }, 3 * 1000)
        }
    }


}