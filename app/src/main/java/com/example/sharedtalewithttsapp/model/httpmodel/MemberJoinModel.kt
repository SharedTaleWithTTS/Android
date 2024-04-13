package com.example.sharedtalewithttsapp.model.httpmodel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MemberJoinModel(
    @SerializedName("account")
    var id: String,
    var passwd: String,
    var nickname: String,
    var email: String,
    var mobile: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(passwd)
        parcel.writeString(nickname)
        parcel.writeString(email)
        parcel.writeString(mobile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemberJoinModel> {
        override fun createFromParcel(parcel: Parcel): MemberJoinModel {
            return MemberJoinModel(parcel)
        }

        override fun newArray(size: Int): Array<MemberJoinModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class MemberJoinState(
    @SerializedName("message")
    var state : String
)