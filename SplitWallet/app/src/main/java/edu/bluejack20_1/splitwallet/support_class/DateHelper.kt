package edu.bluejack20_1.splitwallet.support_class

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit


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

        fun getDifferenceToNow(date : String): Long {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            var firstDate = sdf.parse(date)
            var secondDate = sdf.parse(sdf.format(Calendar.getInstance().time))

            var diffInMillies = Math.abs(secondDate.time - firstDate.time)
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)


        }
    }

}