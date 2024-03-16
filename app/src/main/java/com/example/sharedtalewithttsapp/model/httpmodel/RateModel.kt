package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class RateRequestModel(
    @SerializedName("childnum")
    var childId : String,
    @SerializedName("talenum")
    var taleId : String,
    var rate : String
)

data class RateResponseModel(
    @SerializedName("message")
    var state : String,
    @SerializedName("average_rate")
    var avgRate : String
)
