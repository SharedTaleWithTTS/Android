package com.example.sharedtalewithttsapp.model

import com.google.gson.annotations.SerializedName

data class TTSSettingModel(
    @SerializedName("ttsspeed")
    var ttsSpeed : String,
    @SerializedName("ttsvoice")
    var ttsVoice : String
)
