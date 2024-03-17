package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class StateCheckRequestModel(
    @SerializedName("childnum")
    var childId : String,
    @SerializedName("talenum")
    var taleId : String
)

data class StateCheckResponseModel(
    @SerializedName("message")
    var state : String,
    @SerializedName("like")
    var likeCheck : String,
    var rate : String,
    @SerializedName("favorite")
    var favoritesCheck : String
)
