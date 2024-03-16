package com.example.sharedtalewithttsapp

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivitySelectChildProfileBinding
import com.example.sharedtalewithttsapp.model.httpmodel.ChildProfileRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.ChildProfileResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.LoginRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.CHILD_PROFILE_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.LOGIN_STATE
import com.example.sharedtalewithttsapp.viewholder.ChildProfileAdapter
import com.example.sharedtalewithttsapp.viewholder.ReadingTaleAdapter

class SelectChildProfileActivity : AppCompatActivity(){
    lateinit var binding: ActivitySelectChildProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectChildProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "SelectChildProfileActivity - onCreate() called");

        // http 통신으로 받을 모델
        var childProfileList: ChildProfileResponseModel?


        // 통신
        val childProfileInfo = ChildProfileRequestModel(userId = AppData.instance.getUserId())
        RetrofitManager.instance.childProfileList(childProfileInfo, completion = {
                httpResponseState, childProfileResponse ->

            when(httpResponseState){
                HTTP_RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "아이 프로필 목록 api 호출 성공 : ${childProfileResponse}")

                    when(childProfileResponse?.state){
                        CHILD_PROFILE_RESPONSE_STATE.SUCCESS -> {
                            Log.d(TAG, "불러오기 성공")
                            childProfileList = childProfileResponse
                            binding.childProfileRecyclerView.layoutManager = LinearLayoutManager(this)
                            binding.childProfileRecyclerView.adapter = ChildProfileAdapter(childProfileList!!, clickListener = {
                                val intent: Intent = Intent(this, HomeScreenActivity::class.java)

                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // 태스크 클리어 and 실행할 intent 새로운 task
                                startActivity(intent)

                            })
                            //binding.childProfileRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                        }
                        CHILD_PROFILE_RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "불러오기 실패")
                        }
                    }
                }
                HTTP_RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "아이 프로필 목록 호출 실패 : ${childProfileResponse}");
                }
            }
        })

        binding.addChildLayout.setOnClickListener {
            Log.d(TAG, "아이 추가하기 버튼 클릭 됨");
            val intent: Intent = Intent(this, AddChildActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        Log.d(TAG, "SelectChildProfileActivity - onDestroy() called");
        super.onDestroy()
    }

}