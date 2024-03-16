package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class WriteCommentRequestModel(
    @SerializedName("parent")
    var userId : String,
    @SerializedName("childnum")
    var childId : String,
    @SerializedName("talenum")
    var taleId : String?,
    var q : String,
    var a : String,
    var direction : String
)

data class WriteCommentResponseModel(
    @SerializedName("message")
    var state : String
)
