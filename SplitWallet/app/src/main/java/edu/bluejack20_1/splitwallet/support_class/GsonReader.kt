package edu.bluejack20_1.splitwallet.support_class

import com.google.gson.Gson

open class GsonReader {
    private val gson : Gson = Gson()

    fun listToJson(list: Any) : String{
        return gson.toJson(list)
    }

    fun getGson() : Gson {
        return gson
    }



}