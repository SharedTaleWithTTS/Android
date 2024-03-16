package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.model.TTSSettingModel
import com.example.sharedtalewithttsapp.model.TaleModel
import com.google.gson.annotations.SerializedName

data class HomeScreenRequestModel(
    var userId : String,
    var childId: String
)

data class HomeScreenResponseModel(
    @SerializedName("message")
    var state : String,
    var ttsSetting : TTSSettingModel,               // 설정 정보
    var recentlyView : MutableList<TaleModel>,    // 최근 동화
    var favorites : MutableList<TaleModel>,     // 즐겨찾기
    var recommend : MutableList<TaleModel>
)
