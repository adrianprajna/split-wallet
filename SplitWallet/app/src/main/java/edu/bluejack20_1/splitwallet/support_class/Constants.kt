package edu.bluejack20_1.splitwallet.support_class

import android.util.Log
import java.lang.StringBuilder

object Constants {
    const val KEY_USER = "Users"
    const val LIST_WALLET = "listWallets"
    lateinit var KEY_USER_ID : String

    const val NOTIFICATION_STATUS = "notificationStatus"
    const val THEME_STATUS = "themeStatus"

    const val THEME_LIGHT = "LIGHT"
    const val THEME_DARK = "DARK"

    fun capitalizeEachWord(string : String) : String{
        return string.split(" ").map { it.capitalize() }.joinToString(" ")
    }
}