package com.example.splitwallet.support_class

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DateHelper {

    companion object {
        fun nowToString(): String {
            val date = Calendar.getInstance().time
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            return sdf.format(date)
        }

        fun splitDate(date : String): List<String> {
            return date.split("/").toList()
        }
    }

}