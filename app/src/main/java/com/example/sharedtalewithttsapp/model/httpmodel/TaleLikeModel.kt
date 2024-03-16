package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName

data class TaleLikeRequestModel(
    @SerializedName("childnum")
    var childId : String,
    @SerializedName("talenum")
    var taleId : String?,
    @SerializedName("commentid")
    var commentId : String
)

data class TaleLikeResponseModel(
    @SerializedName("message")
    var state : String,
    var likeNum : String
)
