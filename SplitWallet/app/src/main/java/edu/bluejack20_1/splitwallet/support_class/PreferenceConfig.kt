package edu.bluejack20_1.splitwallet.support_class

import android.content.Context
import com.google.gson.Gson


class PreferenceConfig (context: Context){

    private val sharedPreferences = context.getSharedPreferences("myPreferences", 0)
    private val gson : Gson = Gson()

    fun listToJson(list: Any) : String{
        return gson.toJson(list)
    }

    fun getGson() : Gson{
        return gson
    }


    fun putString(key: String, value : String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String) : String? {
        return sharedPreferences.getString(key, null)
    }

    fun clearSharedPreference() : Boolean{
        return sharedPreferences.edit().clear().commit()
    }

    fun clearOneSharedPreference(key : String) : Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }
}