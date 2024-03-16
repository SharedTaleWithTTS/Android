package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.model.ChildInfoModel
import com.google.gson.annotations.SerializedName

data class ChildProfileRequestModel(
    var userId : String
)
data class ChildProfileResponseModel(
    @SerializedName("message")
    var state : String,
    var childProfileList : MutableList<ChildInfoModel>
)
