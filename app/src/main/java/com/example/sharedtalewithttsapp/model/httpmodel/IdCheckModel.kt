package com.example.sharedtalewithttsapp.model.httpmodel

import com.google.gson.annotations.SerializedName


data class IdCheckModel(
    @SerializedName("account")
    var id : String
)

data class IdCheckState(
    @SerializedName("message")
    var state : String
)
