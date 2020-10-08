package com.example.splitwallet.support_class

import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

open class GsonReader {
    private val gson : Gson = Gson()

    fun listToJson(list: Any) : String{
        return gson.toJson(list)
    }

    fun getGson() : Gson {
        return gson
    }

}