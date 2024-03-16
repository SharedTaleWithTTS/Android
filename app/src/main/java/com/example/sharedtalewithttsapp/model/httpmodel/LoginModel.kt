package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.model.ChildInfoModel
import com.example.sharedtalewithttsapp.model.MemberSettingModel
import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("account")
    var id: String,
    var passwd: String
)

data class LoginResponseModel(
    @SerializedName("message")
    var state : String,
    var user : MemberJoinModel
)
