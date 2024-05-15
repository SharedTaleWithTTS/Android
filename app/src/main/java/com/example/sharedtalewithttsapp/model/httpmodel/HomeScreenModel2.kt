package com.example.sharedtalewithttsapp.model.httpmodel

import com.example.sharedtalewithttsapp.model.CreateTaleInfoModel
import com.example.sharedtalewithttsapp.model.TTSSettingModel
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.viewholder.MyTaleModel
import com.google.gson.annotations.SerializedName

data class HomeScreenRequestModel2(
    var userId : String,
    var childId: String
)

data class HomeScreenResponseModel2(
    @SerializedName("message")
    var state : String,
    var ttsSetting : TTSSettingModel,               // 설정 정보
    var mostViewTale : MutableList<TaleModel>,    // 조회 수 높은 동화
    var recentCreateTale : MutableList<CreateTaleInfoModel>    // 최근 창작 동화
)

