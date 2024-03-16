package com.example.sharedtalewithttsapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityEditCommentBinding
import com.example.sharedtalewithttsapp.glide.GlideManager
import com.example.sharedtalewithttsapp.media.MediaPlayerManager
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.WriteCommentRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.READING_TALE_STATE
import com.example.sharedtalewithttsapp.utils.WRITE_COMMENT_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.ReadingTaleAdapter

class EditCommentActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityEditCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "EditCommentActivity - onCreate() called")
        title = ""
        var qaDirection : String = "CTOP"      // 질문 방향 변수

        setSupportActionBar(binding.toolBar)    // 툴바 표시

        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기
        
        // 질문 방향 스위치
        binding.swQaDirection.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 스위치가 켜진 경우
                binding.textQ.setText("부모의 질문")
                binding.textA.setText("아이의 답변")
                qaDirection = "PTOC"
            } else {
                // 스위치가 꺼진 경우
                binding.textQ.setText("아이의 질문")
                binding.textA.setText("부모의 답변")
                qaDirection = "CTOP"
            }
        }

        // 댓글 작성 버튼 눌렀을 때
        binding.writeCommentBtn.setOnClickListener {
            Log.d(TAG, "AppData childId : ${AppData.instance.getChildId()}");
            Log.d(TAG, "방향 테스트 ${qaDirection}");
            val writeCommentInfo = WriteCommentRequestModel(userId = AppData.instance.getUserId(),
                                                            childId = AppData.instance.getChildId(),
                                                            taleId = intent.getStringExtra("taleId"),
                                                            q = binding.etQ.text.toString(),
                                                            a = binding.etA.text.toString(),
                                                            direction = qaDirection)
            RetrofitManager.instance.writeComment(writeCommentInfo,
                completion = { httpResponseState, writeCommentResponse ->
                    when (httpResponseState) {
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(TAG, "댓글작성 api 호출 성공 : ${writeCommentResponse}")

                            when (writeCommentResponse?.state) {
                                WRITE_COMMENT_RESPONSE_STATE.SUCCESS -> {
                                    Log.d(TAG, "댓글작성 성공")
                                    // 이벤트 핸들러 등록
                                    val eventHandler = DialogInterface.OnClickListener{ dialogInterface, i ->
                                        when(i){
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                Log.d(TAG, "닫기버튼 누름")
                                                onBackPressed() // 뒤로가기
                                            }
                                        }
                                    }
                                    AlertDialogManager.instance.simpleAlertDialog("댓글 작성이 완료되었습니다.", this, eventHandler)
                                }
                                WRITE_COMMENT_RESPONSE_STATE.FAIL -> {
                                    Log.d(TAG, "댓글작성 실패")
                                    AlertDialogManager.instance.simpleAlertDialog("댓글 작성 실패", this, null)
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "댓글작성 api 호출 실패 : ${writeCommentResponse}");
                        }
                    }
                })
        }
    }
}