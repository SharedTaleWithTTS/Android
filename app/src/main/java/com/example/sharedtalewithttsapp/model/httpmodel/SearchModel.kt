package com.example.sharedtalewithttsapp.model.httpmodel

import android.os.Parcel
import android.os.Parcelable
import com.example.sharedtalewithttsapp.model.TaleModel
import com.google.gson.annotations.SerializedName

data class SearchRequestModel(
    var type : String,      // 제목으로 검색 등
    var search : String?     // 검색어
)

data class SearchResponseModel(
    @SerializedName("message")
    var state: String,
    var count: String,
    var searchResult: MutableList<TaleModel>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        mutableListOf<TaleModel>().apply {
            parcel.readList(this, TaleModel::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(state)
        parcel.writeString(count)
        parcel.writeList(searchResult)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchResponseModel> {
        override fun createFromParcel(parcel: Parcel): SearchResponseModel {
            return SearchResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<SearchResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}
