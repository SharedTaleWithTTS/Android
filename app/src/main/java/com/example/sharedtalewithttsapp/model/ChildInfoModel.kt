package com.example.sharedtalewithttsapp.model

import com.google.gson.annotations.SerializedName

data class ChildInfoModel(
    @SerializedName("num")
    var childId : String,
    @SerializedName("name")
    var childName : String,
    @SerializedName("age")
    var childAge : String,
    @SerializedName("type")
    var childGender : String,
    @SerializedName("personality")
    var childPersonality : String,

    var parent_id : String
)
