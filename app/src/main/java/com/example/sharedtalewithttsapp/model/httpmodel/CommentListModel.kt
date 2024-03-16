package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.model.CommentModel
import com.google.gson.annotations.SerializedName

data class CommentListRequestModel(
    @SerializedName("talenum")
    var taleId : String?,
    @SerializedName("childnum")
    var childId : String
)
data class CommentListResponseModel(
    @SerializedName("message")
    var state : String,
    @SerializedName("list")
    var commentList : MutableList<CommentModel>,
    var likeList : MutableList<String>
)
