package com.example.sharedtalewithttsapp.sharedpreferences

data class LoginSaveSharedPreferencesModel(
    var userId : String? = null,
    var passwd : String? = null,
    var isLoginSave : Boolean = false
)
