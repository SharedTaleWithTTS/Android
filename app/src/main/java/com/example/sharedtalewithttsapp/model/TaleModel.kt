package com.example.sharedtalewithttsapp.model

import android.os.Parcel
import android.os.Parcelable
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.google.gson.annotations.SerializedName

data class TaleModel(
    @SerializedName("num")
    var id : String,
    var title : String,
    @SerializedName("imglink")
    var taleImage : String,
    @SerializedName("likes")
    var like : String,          // 좋아요
    var rate : String,          // 평균 평점
    var views : String,     // 조회수
    var reviews : String // 댓글 수
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(taleImage)
        parcel.writeString(like)
        parcel.writeString(rate)
        parcel.writeString(views)
        parcel.writeString(reviews)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaleModel> {
        override fun createFromParcel(parcel: Parcel): TaleModel {
            return TaleModel(parcel)
        }

        override fun newArray(size: Int): Array<TaleModel?> {
            return arrayOfNulls(size)
        }
    }
}
