package com.example.sharedtalewithttsapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityAddChildBinding
import com.example.sharedtalewithttsapp.glide.GlideManager
import com.example.sharedtalewithttsapp.media.MediaPlayerManager
import com.example.sharedtalewithttsapp.model.httpmodel.AddChildRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.ADD_CHILD_STATE
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.READING_TALE_STATE
import com.example.sharedtalewithttsapp.viewholder.ReadingTaleAdapter

class AddChildActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddChildBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 아이 추가하기 버튼을 눌렀을 때
        binding.addChildBtn.setOnClickListener {
            Log.d(TAG, "아이 추가하기 버튼 누름");

            // 입력값이 맞는지 확인 (아이 나이 - 정수)
            if(binding.etChildAge.text.toString().toIntOrNull() != null){
                // 라디오 버튼 추적
                val checkedGenderRadioButtonId = binding.genderRg.checkedRadioButtonId
                val checkedGenderRadioButton = binding.root.findViewById<RadioButton>(checkedGenderRadioButtonId)

                val checkedPersonalityRadioButtonId = binding.personalityRg.checkedRadioButtonId
                val checkedPersonalityRadioButton = binding.root.findViewById<RadioButton>(checkedPersonalityRadioButtonId)

                // 통신
                val addChildInfo = AddChildRequestModel(userId = AppData.instance.getUserId(),
                    childName = binding.etChildName.text.toString(),
                    childAge = binding.etChildAge.text.toString(),
                    childGender = when(checkedGenderRadioButton.text.toString()){
                        "남아" -> "MALE"
                        "여아" -> "FEMALE"
                        else -> ""
                    },
                    childPersonality = when(checkedPersonalityRadioButton.text.toString()){
                        "외향형" -> "E"
                        "내향형" -> "I"
                        else -> ""
                    })
                RetrofitManager.instance.addChild(addChildInfo,
                    completion = { httpResponseState, addChildResponse ->
                        when (httpResponseState) {
                            HTTP_RESPONSE_STATE.OKAY -> {
                                Log.d(Constants.TAG, "아이 추가 api 호출 성공 : ${addChildResponse}")

                                when (addChildResponse?.state) {
                                    ADD_CHILD_STATE.SUCCESS -> {
                                        Log.d(Constants.TAG, "아이 추가 성공")
                                        val eventHandler =
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                when (i) {
                                                    DialogInterface.BUTTON_POSITIVE -> {
                                                        Log.d(TAG, "닫기 버튼 누름")
                                                        finish()
                                                    }

                                                }
                                            }

                                        AlertDialogManager.instance.simpleAlertDialog("아이 추가 성공했습니다..!", this, eventHandler)
                                    }
                                    ADD_CHILD_STATE.FAIL -> {
                                        Log.d(Constants.TAG, "아이 추가 실패")
                                    }
                                }
                            }
                            HTTP_RESPONSE_STATE.FAIL -> {
                                Log.d(Constants.TAG, "아이 추가 api 호출 실패 : ${addChildResponse}")
                            }
                        }
                    })
            }
            else{
                AlertDialogManager.instance.simpleAlertDialog("나이를 다시 입력해 주세요", this, null)
            }
            
        }
        
    }
}