package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class MemberJoinModel(
    @SerializedName("account")
    var id: String,
    var passwd: String,
    var nickname: String,
    var email: String,
    var mobile: String
)

data class MemberJoinState(
    @SerializedName("message")
    var state : String
)