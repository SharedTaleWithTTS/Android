package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class FavoritesRequestModel(
    @SerializedName("childnum")
    var childId : String,
    @SerializedName("talenum")
    var taleId : String?,
)

data class FavoritesResponseModel(
    @SerializedName("message")
    var state : String
)
