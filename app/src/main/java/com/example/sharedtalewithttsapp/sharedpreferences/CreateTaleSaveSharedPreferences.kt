package com.example.sharedtalewithttsapp.sharedpreferences

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreateTaleSaveSharedPreferences(private val context: Context) {

    private val preferencesName = "MyPreferencesName"
    private val listKey = "myListKey"
    private val sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    fun saveUriList(list: MutableList<Uri>) {
        // Uri 리스트를 String 리스트로 변환
        val stringList = list.map { it.toString() }.toMutableList()
        val json = Gson().toJson(stringList)
        sharedPreferences.edit().putString(listKey, json).apply()
    }

    fun getUriList(): MutableList<Uri> {
        val json = sharedPreferences.getString(listKey, null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        val stringList: MutableList<String> = Gson().fromJson(json, type) ?: mutableListOf()
        // String 리스트를 Uri 리스트로 변환
        return stringList.map { Uri.parse(it) }.toMutableList()
    }
}