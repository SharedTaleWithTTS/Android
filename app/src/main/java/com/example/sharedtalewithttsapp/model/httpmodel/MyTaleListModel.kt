package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.viewholder.MyTaleModel
import com.google.gson.annotations.SerializedName

data class MyTaleListRequestModel(
    var userId : String
)
data class MyTaleListResponseModel(
    @SerializedName("message")
    var state : String,
    @SerializedName("data")
    var myTaleList : MutableList<MyTaleModel>
)
