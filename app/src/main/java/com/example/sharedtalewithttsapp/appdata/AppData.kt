package com.example.sharedtalewithttsapp.appdata

import com.example.sharedtalewithttsapp.model.ChildInfoModel

class AppData {
    companion object {
        val instance = AppData()
    }
    private var userId: String = ""
    private var childId: String = ""
    private var userNickname: String = ""
    private var userEmail: String = ""
    private var userMobile: String = ""

    private var childInfo =
        ChildInfoModel(childId = "", childName = "", childPersonality = "", childGender = "", childAge = ""
                        , parent_id = "")

    fun getUserId(): String {
        return this.userId
    }

    fun setUserId(id: String) {
        this.userId = id
    }

    fun getChildId(): String {
        return this.childId
    }

    fun setChildId(id: String) {
        this.childId = id
    }

    fun getUserNickname(): String {
        return this.userNickname
    }

    fun setUserNickname(nickname: String) {
        this.userNickname = nickname
    }

    fun getUserEmail(): String {
        return this.userEmail
    }

    fun setUserEmail(email: String) {
        this.userEmail = email
    }

    fun getUserMobile(): String {
        return this.userMobile
    }

    fun setUserMobile(mobile: String) {
        this.userMobile = mobile
    }
    fun setChildInfo(childInfo : ChildInfoModel){
        this.childInfo = childInfo
    }
    fun getChildInfo() : ChildInfoModel{
        return this.childInfo
    }
}
