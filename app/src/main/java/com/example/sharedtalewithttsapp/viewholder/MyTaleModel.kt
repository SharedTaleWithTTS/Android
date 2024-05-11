package com.example.sharedtalewithttsapp.viewholder

import com.google.gson.annotations.SerializedName

data class MyTaleModel(
    var title : String,
    var thumbnail : String,
    @SerializedName("video")
    var videoURI : String
)
