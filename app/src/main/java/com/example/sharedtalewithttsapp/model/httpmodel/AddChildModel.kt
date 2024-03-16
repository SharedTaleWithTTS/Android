package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class AddChildRequestModel(
    @SerializedName("parent")
    var userId : String,
    @SerializedName("name")
    var childName : String,
    @SerializedName("age")
    var childAge : String,
    @SerializedName("type")
    var childGender : String,
    @SerializedName("personality")
    var childPersonality : String
)

data class AddChildResponseModel(
    @SerializedName("message")
    var state : String
)
