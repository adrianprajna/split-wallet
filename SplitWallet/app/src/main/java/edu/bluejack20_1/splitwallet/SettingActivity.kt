package edu.bluejack20_1.splitwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.splitwallet.support_class.Constants
import edu.bluejack20_1.splitwallet.support_class.PreferenceConfig
import edu.bluejack20_1.splitwallet.support_class.Users
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    lateinit var preferenceConfig : PreferenceConfig
    override fun onCreate(savedInstanceState: Bundle?) {

        preferenceConfig =
            PreferenceConfig(
                this@SettingActivity
            )
        if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setIcon()
        initButton()

        val u : Users
        u = preferenceConfig.getGson().fromJson(preferenceConfig.getString(Constants.KEY_USER), Users::class.java)
        setNotifIcon(u.notification)
        home_username.text = u.username
        home_email.text = u.email
    }

    fun getFlag() : Boolean{
        var preferenceConfig : PreferenceConfig =
            PreferenceConfig(
                this@SettingActivity
            )
        return preferenceConfig.getString(Constants.THEME_STATUS) != Constants.THEME_DARK
    }

    override fun onBackPressed() {
        var i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun setIcon(){
//        var preferenceConfig : PreferenceConfig =
//            PreferenceConfig(
//                this@SettingActivity
//            )
//        var theme = preferenceConfig.getString(Constants.THEME_STATUS)
//        if (theme == Constants.THEME_LIGHT){
//            theme_icon.setImageResource(R.drawable.icon_theme_light)
//        } else {
//            theme_icon.setImageResource(R.drawable.icon_theme_dark)
//        }

        if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
            theme_icon.setImageResource(R.drawable.icon_theme_dark)
        } else {
            theme_icon.setImageResource(R.drawable.icon_theme_light)
        }
    }

    fun restartApp(){
        var i = Intent(applicationContext, SettingActivity::class.java)
        startActivity(i)
        finish()
    }

    fun initButton() {
        btn_back.setOnClickListener {
            val intent = Intent(this@SettingActivity, MainActivity::class.java)
            this@SettingActivity.startActivity(intent)
        }

        btn_theme.setOnClickListener {
            var preferenceConfig : PreferenceConfig =
                PreferenceConfig(
                    this@SettingActivity
                )
            var theme = preferenceConfig.getString(Constants.THEME_STATUS)
//            if (theme == Constants.THEME_LIGHT){
//                preferenceConfig.putString(Constants.THEME_STATUS, Constants.THEME_DARK)
//            } else {
//                preferenceConfig.putString(Constants.THEME_STATUS, Constants.THEME_LIGHT)
//            }

            if (preferenceConfig.loadTheme() == Constants.THEME_DARK){
                preferenceConfig.putString(Constants.THEME_STATUS, Constants.THEME_LIGHT)
                restartApp()
            } else {
                preferenceConfig.putString(Constants.THEME_STATUS, Constants.THEME_DARK)
                restartApp()
            }
            setIcon()

        }

        btn_notification.setOnClickListener {
            var preferenceConfig : PreferenceConfig =
                PreferenceConfig(
                    this@SettingActivity
                )

            val u : Users
            u = preferenceConfig.getGson().fromJson(preferenceConfig.getString(Constants.KEY_USER), Users::class.java)
            var notification = u.notification

            if (notification == "true"){
                u.notification = "false"
                preferenceConfig.putString(Constants.KEY_USER, preferenceConfig.listToJson(u))

                /*

                Code disini buat set notif yaa


                 */





            } else {
                u.notification = "true"
                preferenceConfig.putString(Constants.KEY_USER, preferenceConfig.listToJson(u))

                /*

                Code disini buat set notif yaa


                 */
            }

            var reff = FirebaseDatabase.getInstance().getReference()
                .child(Constants.KEY_USER).child(Constants.KEY_USER_ID)
                .child("notification")

            reff.setValue(u.notification)
            setNotifIcon(u.notification)

        }

    }

    fun setNotifIcon(value : String){
        if (value == "true"){
            icon_notification.setImageResource(R.drawable.icon_notification_active)
        } else {
            icon_notification.setImageResource(R.drawable.icon_notification_off)
        }
    }


}