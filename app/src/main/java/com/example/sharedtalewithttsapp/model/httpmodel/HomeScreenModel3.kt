package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.model.TTSSettingModel
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.viewholder.MyTaleModel
import com.google.gson.annotations.SerializedName

data class HomeScreenRequestModel3(
    var userId : String,
    var childId: String
)

data class HomeScreenResponseModel3(
    @SerializedName("message")
    var state : String,
    var ttsSetting : TTSSettingModel,               // 설정 정보
    var allTaleList : MutableList<TaleModel>,    // 전체 동화
)

