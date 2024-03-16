package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class NicknameCheckModel(
    var nickname : String
)

data class NicknameCheckState(
    @SerializedName("message")
    var state : String
)
