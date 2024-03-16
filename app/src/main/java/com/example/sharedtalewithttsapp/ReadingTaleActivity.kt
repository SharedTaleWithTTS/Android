package com.example.sharedtalewithttsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityReadingTaleBinding
import com.example.sharedtalewithttsapp.glide.GlideManager
import com.example.sharedtalewithttsapp.media.MediaPlayerManager
import com.example.sharedtalewithttsapp.model.httpmodel.CommentListRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TaleLikeRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.COMMENT_LIST_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.READING_TALE_STATE
import com.example.sharedtalewithttsapp.utils.TALE_LIKE_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.CommentAdapter
import com.example.sharedtalewithttsapp.viewholder.CommentViewHolder
import com.example.sharedtalewithttsapp.viewholder.ReadingTaleAdapter
import com.example.sharedtalewithttsapp.viewholder.ReadingTaleViewHolder
import org.w3c.dom.Comment

class ReadingTaleActivity : AppCompatActivity() {
    lateinit var binding: ActivityReadingTaleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingTaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)    // 툴바 표시

        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기

        
        binding.fabPlayPause.shrink()   // 플로팅 액션 버튼 접기
        binding.fabPlayPause.setIconResource(android.R.drawable.ic_media_pause) // 일시정지로 바꾸기
        title = "title"
        // http 통신으로 받을 모델
        var readingTale: TestReadingTaleResponseModel? = null
        
        // activity 시작 후 통신
        // 동화 읽기 데이터
        val readingTaleInfo = TestReadingTaleRequestModel(taleId = intent.getStringExtra("taleId"),
                                                            childId = AppData.instance.getChildId(),
                                                            userId = AppData.instance.getUserId())
        RetrofitManager.instance.readingTale(readingTaleInfo,
            completion = { httpResponseState, readingTaleResponse ->
                when (httpResponseState) {
                    HTTP_RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "동화 읽기 api 호출 성공 : ${readingTaleResponse}")

                        when (readingTaleResponse?.state) {
                            READING_TALE_STATE.SUCCESS -> {
                                Log.d(TAG, "동화 읽기 데이터 불러오기 성공")

                                // http 통신으로부터 받은 동화읽기 데이터
                                readingTale = readingTaleResponse

                                //binding.textTitle.text = readingTale?.title
                                title = readingTale?.title
                                GlideManager.instance.serverImageRequest(
                                    this,
                                    readingTale!!.taleImage,
                                    500,
                                    500,
                                    binding.imageTale
                                )
                                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                                binding.recyclerView.adapter = ReadingTaleAdapter(readingTale!!)
                                binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                                MediaPlayerManager.instance.initialize(binding)
                                MediaPlayerManager.instance.setTaleId(intent.getStringExtra("taleId"))
                                MediaPlayerManager.instance.ttsAudioStart(readingTale!!, 0)

                            }

                            READING_TALE_STATE.FAIL -> {
                                Log.d(TAG, "동화 읽기 데이터 불러오기 실패")
                            }
                        }
                    }

                    HTTP_RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "동화 읽기 api 호출 실패 : ${readingTaleResponse}");
                    }
                }
            })

        // 플로팅 액션 버튼을 눌렀을 때
        binding.fabPlayPause.setOnClickListener {
            Log.d(TAG, "ReadingTaleActivity - 플로팅 액션 버튼 누름");
            // 아이콘과 문자열을 함꼐 표시했다면
            when(binding.fabPlayPause.isExtended){
                true -> {   // 현재 일시정지 상태
                    binding.fabPlayPause.shrink()
                    binding.fabPlayPause.setIconResource(android.R.drawable.ic_media_pause)
                    MediaPlayerManager.instance.ttsAudioStart(readingTale!!, MediaPlayerManager.instance.getTTSIndex())
                }
                false -> {  // 현재 재생 상태
                    binding.fabPlayPause.extend()
                    binding.fabPlayPause.setIconResource(android.R.drawable.ic_media_play)
                    MediaPlayerManager.instance.ttsAudioPause(MediaPlayerManager.instance.getTTSIndex())
                }
            }
        }
        // 설정바의 듣기 설정 버튼을 눌렀을 때
        binding.settingBarSpeak.setOnClickListener {
            Log.d(TAG, "설정바의 듣기설정 버튼 누름");
            // 화면 조정
            binding.ttsSettingLayout.visibility = View.VISIBLE
            binding.settingBar.visibility = View.INVISIBLE
            binding.fabPlayPause.y -= 430f
        }

        // 소통 버튼을 눌렀을 때
        binding.settingBarQa.setOnClickListener {
            Log.d(TAG, "소통 버튼 누름")
            // 화면 조정
            binding.qaLayout.visibility = View.VISIBLE
            binding.settingBar.visibility = View.INVISIBLE
            binding.fabPlayPause.y -= 1160f

            val commentListRequest = CommentListRequestModel(taleId = intent.getStringExtra("taleId"),
                                                            childId = AppData.instance.getChildId())

            RetrofitManager.instance.commentList(commentListRequest,
                completion = { httpResponseState, commentListResponse ->
                    when (httpResponseState) {
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(TAG, "댓글 리스트 api 호출 성공 : ${commentListResponse}")

                            when (commentListResponse?.state) {
                                COMMENT_LIST_RESPONSE_STATE.SUCCESS -> {
                                    Log.d(TAG, "댓글 리스트 데이터 불러오기 성공")

                                    binding.qaRecyclerView.layoutManager = LinearLayoutManager(this)
                                    binding.qaRecyclerView.adapter = CommentAdapter(commentListResponse!!, clickListener = {
                                        commentId, position ,likeState ->
                                        val taleLike = TaleLikeRequestModel(childId = AppData.instance.getChildId(),
                                                                            taleId = intent.getStringExtra("taleId"),
                                                                            commentId = commentId)
                                        RetrofitManager.instance.addTaleLike(taleLike,
                                            completion = { httpResponseState, taleLikeResponse ->
                                                when (httpResponseState) {
                                                    HTTP_RESPONSE_STATE.OKAY -> {
                                                        Log.d(TAG, "좋아요 추가 api 호출 성공 : ${taleLikeResponse}")

                                                        when (taleLikeResponse?.state) {
                                                            TALE_LIKE_RESPONSE_STATE.SUCCESS -> {
                                                                Log.d(TAG, "좋아요 변경 성공")
                                                                // 어댑터에서 특정 포지션의 아이템을 가져옵니다.
                                                                val viewHolder = binding.qaRecyclerView.findViewHolderForAdapterPosition(position) as? CommentViewHolder
                                                                Log.d(TAG, "viewHolder : ${viewHolder}");
                                                                if(likeState){
                                                                    viewHolder?.binding?.likeBtn?.setImageResource(R.drawable.like_disabled)
                                                                }else {
                                                                    viewHolder?.binding?.likeBtn?.setImageResource(R.drawable.like_activate)
                                                                }
                                                            }
                                                            TALE_LIKE_RESPONSE_STATE.FAIL -> {
                                                                Log.d(TAG, "좋아요 변경 실패")
                                                            }
                                                        }
                                                    }
                                                    HTTP_RESPONSE_STATE.FAIL -> {
                                                        Log.d(TAG, "좋아요 추가 api 호출 실패 : ${taleLikeResponse}");
                                                    }
                                                }
                                            })
                                    })
                                    binding.qaRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                                }
                                COMMENT_LIST_RESPONSE_STATE.FAIL -> {
                                    Log.d(TAG, "댓글 리스트 데이터 불러오기 실패")
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "댓글 리스트 api 호출 실패 : ${commentListResponse}");
                        }
                    }
                })
        }
        
        // 듣기 설정 화면 닫기를 눌렀을 때
        binding.ttsSettingLayoutClosed.setOnClickListener {
            Log.d(TAG, "듣기 설정화면 닫기 버튼 누름");
            // 화면 조정
            binding.ttsSettingLayout.visibility = View.INVISIBLE
            binding.settingBar.visibility = View.VISIBLE
            binding.fabPlayPause.y += 430f
        }

        // 소통 화면 닫기를 눌렀을 때
        binding.qaLayoutClosedBtn.setOnClickListener {
            Log.d(TAG, "소통화면 닫기 버튼 누름");
            // 화면 조정
            binding.qaLayout.visibility = View.INVISIBLE
            binding.settingBar.visibility = View.VISIBLE
            binding.fabPlayPause.y += 1160f
        }
        
        // tts 세팅 레이아웃 화면을 눌렀을 때
        // 레이아웃 간 클릭 이벤트 분리 위함
        // 뒤쪽의 리사이클러 뷰에 클릭 이벤트가 가지 않음
        binding.ttsSettingLayout.setOnClickListener {
            Log.d(TAG, "tts 세팅 레이아웃 클릭");
        }
        binding.qaLayout.setOnClickListener{
            Log.d(TAG, "qa 레이아웃 클릭")        // 위와 같은 이유
        }

        // tts 속도 줄이기 버튼을 눌렀을 때
        binding.ttsSppedMinusBtn.setOnClickListener {
            Log.d(TAG, "속도 감소 버튼")
            // tts 속도 리스트
            val ttsSpedList = MediaPlayerManager.instance.getTTSSpeedList()
            when(val index = ttsSpedList.indexOf(binding.ttsSpeed.text)){
                index -> if(index != 0) {        // index가 0이 아니면 속도 감소
                    binding.ttsSpeed.text = ttsSpedList[index - 1]                              // text 변경
                    MediaPlayerManager.instance.setCurrentTTSSpeed(ttsSpedList[index - 1])
                    Log.d(TAG, "설정된 속도 : ${MediaPlayerManager.instance.getCurrentTTSSpeed()}");
                }
            }
        }
        // tts 속도 높이기 버튼을 눌렀을 때
        binding.ttsSppedPlusBtn.setOnClickListener {
            Log.d(TAG, "속도 증가 버튼")
            // tts 속도 리스트
            val ttsSpedList = MediaPlayerManager.instance.getTTSSpeedList()
            when(val index = ttsSpedList.indexOf(binding.ttsSpeed.text)){
                index -> if(ttsSpedList.size - 1 != index) {        // index가 0이 아니면 속도 증가
                    binding.ttsSpeed.text = ttsSpedList[index + 1]                              // text 변경
                    MediaPlayerManager.instance.setCurrentTTSSpeed(ttsSpedList[index + 1])
                    Log.d(TAG, "설정된 속도 : ${MediaPlayerManager.instance.getCurrentTTSSpeed()}");
                }
            }
        }
        // 목소리선택 - 여자1
        binding.womenRbtn1.setOnClickListener {
            Log.d(TAG, "여자 1 선택")
            MediaPlayerManager.instance.setCurrentVoice("A")
        }
        // 목소리선택 - 여자2
        binding.womenRbtn2.setOnClickListener {
            Log.d(TAG, "여자2 선택")
            MediaPlayerManager.instance.setCurrentVoice("B")
        }
        // 목소리선택 - 남자1
        binding.menRbtn1.setOnClickListener {
            Log.d(TAG, "남자1 선택")
            MediaPlayerManager.instance.setCurrentVoice("C")
        }// 목소리선택 - 남자2
        binding.menRbtn2.setOnClickListener {
            Log.d(TAG, "남자2 선택")
            MediaPlayerManager.instance.setCurrentVoice("D")
        }



    }//onCreate
    
    // 뒤로가기 버튼
    override fun onBackPressed() {
        super.onBackPressed()
        if(!MediaPlayerManager.instance.getPauseState()){
            MediaPlayerManager.instance.ttsAudioPause(0)    // 재생상태일 때 일시정지
        }
        MediaPlayerManager.instance.setCurrentPosition(0)   // 오디오 시작지점 변경 (동화 재실행 경우 대비)
    }

}
