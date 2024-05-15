package com.example.sharedtalewithttsapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CreateTaleInfoModel(
    @SerializedName("account")
    var userNickname : String,
    var title : String,
    var thumbnail : String,
    @SerializedName("video")
    var videoURI : String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userNickname)
        parcel.writeString(title)
        parcel.writeString(thumbnail)
        parcel.writeString(videoURI)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreateTaleInfoModel> {
        override fun createFromParcel(parcel: Parcel): CreateTaleInfoModel {
            return CreateTaleInfoModel(parcel)
        }

        override fun newArray(size: Int): Array<CreateTaleInfoModel?> {
            return arrayOfNulls(size)
        }
    }
}
