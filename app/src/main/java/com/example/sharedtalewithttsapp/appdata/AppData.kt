package com.example.sharedtalewithttsapp.appdata

import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager

class AppData{
    companion object {
        val instance = AppData()
    }
    private var userId : String = ""
    private var childId : String = ""

    fun getUserId() : String{
        return this.userId
    }
    fun setUserId(id : String){
        this.userId = id
    }
    fun getChildId() : String{
        return this.childId
    }
    fun setChildId(id : String){
        this.childId = id
    }

}



