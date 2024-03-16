package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class TestReadingTaleRequestModel(
    var userId : String,
    @SerializedName("childnum")
    var childId : String,
    @SerializedName("num")
    var taleId : String?
)

data class TestReadingTaleResponseModel(
    var state : String,
    @SerializedName("imglink")
    var taleImage : String,
    var title : String,
    @SerializedName("tts_text")
    var ttsText : List<String>
)
