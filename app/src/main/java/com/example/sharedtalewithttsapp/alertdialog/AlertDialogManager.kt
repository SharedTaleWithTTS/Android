package com.example.sharedtalewithttsapp.alertdialog

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog

class AlertDialogManager {
    companion object {
        val instance = AlertDialogManager()
    }
    
    // 일반적인 단순한 알림창
    // 구성: 내용, 닫기 버튼
    fun simpleAlertDialog(alertMessage : String, context: Context, eventHandler: DialogInterface.OnClickListener?){
        AlertDialog.Builder(context).run{
            setTitle("안내")
            setIcon(android.R.drawable.ic_dialog_info)
            setMessage(alertMessage)
            setPositiveButton("닫기", eventHandler)
            show()
        }
    }
    // 예, 아니오 버튼
    fun ynAlertDialog(alertMessage : String, context: Context,eventHandler:DialogInterface.OnClickListener){
        AlertDialog.Builder(context).run{
            setTitle("안내")
            setIcon(android.R.drawable.ic_dialog_info)
            setMessage(alertMessage)
            setPositiveButton("예", eventHandler)
            setNegativeButton("아니오", eventHandler)
            show()
        }
    }
}