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

    //For Location
    const val PACKAGE_NAME = "edu.bluejack20_1.splitwallet"
    const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
    const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
    const val SUCCESS_RESULT = 1
    const val FAILURE_RESULT = 0


}