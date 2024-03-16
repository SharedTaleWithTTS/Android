package com.example.sharedtalewithttsapp.model

import com.google.gson.annotations.SerializedName

data class CommentModel(
    @SerializedName("num")
    var commentId : String,
    var nickname : String,
    var childAge : String,
    @SerializedName("type")
    var childGender : String,
    @SerializedName("personality")
    var childPersonality : String,
    @SerializedName("writedate")
    var writeDate : String,
    @SerializedName("likes")
    var like : String,

    var q : String,
    var a : String,
    var direction : String
)
