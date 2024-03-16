package com.example.sharedtalewithttsapp.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.sharedtalewithttsapp.sharedpreferences.PreferenceHelper.get
import com.example.sharedtalewithttsapp.sharedpreferences.PreferenceHelper.set


class SharedManager(context : Context) {

    // prefs[] 대괄호 연산 사용을 위해 get, set import
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveLoginSave(user: LoginSaveSharedPreferencesModel) {
        prefs["userId"] = user.userId
        prefs["passwd"] = user.passwd
        prefs["isLoginSave"] = user.isLoginSave
    }

    fun getLoginSave(): LoginSaveSharedPreferencesModel {
        return LoginSaveSharedPreferencesModel().apply {
            userId = prefs["userId", ""]
            passwd = prefs["passwd", ""]
            isLoginSave = prefs["isLoginSave", false]
        }
    }
}