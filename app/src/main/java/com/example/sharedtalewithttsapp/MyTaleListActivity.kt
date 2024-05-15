package com.example.sharedtalewithttsapp

import android.media.MediaPlayer
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityMyTaleListBinding
import com.example.sharedtalewithttsapp.model.httpmodel.MyTaleListRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.MY_TALE_LIST_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.MyTaleAdapter
import com.example.sharedtalewithttsapp.viewholder.MyTaleItemTouchCallback
import com.example.sharedtalewithttsapp.viewholder.MyTaleModel

class MyTaleListActivity : AppCompatActivity() /*, MediaPlayer.OnPreparedListener*/{
    lateinit var binding : ActivityMyTaleListBinding
    
    private lateinit var adapter: MyTaleAdapter
    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var mediaController : MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTaleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "내 동화 보기"

        setSupportActionBar(binding.toolBar)    // 툴바 표시
        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기


        mediaPlayer = MediaPlayer()
        mediaController = MediaController(this)

        // MediaController를 MediaPlayer에 연결
        mediaController.setMediaPlayer(object : MediaController.MediaPlayerControl {
            override fun start() = mediaPlayer.start()
            override fun pause() = mediaPlayer.pause()
            override fun getDuration() = mediaPlayer.duration
            override fun getCurrentPosition() = mediaPlayer.currentPosition
            override fun seekTo(pos: Int) = mediaPlayer.seekTo(pos)
            override fun isPlaying() = mediaPlayer.isPlaying
            override fun getBufferPercentage() = 0 // 스트리밍 미디어에 사용되는 버퍼링 비율을 제공합니다. 로컬 파일의 경우 필요하지 않습니다.
            override fun canPause() = true
            override fun canSeekBackward() = true
            override fun canSeekForward() = true
            override fun getAudioSessionId() = mediaPlayer.audioSessionId
        })

        // MediaController를 사용자 인터페이스에 추가
        mediaController.setAnchorView(binding.surfaceView)
        mediaController.isEnabled = true


        // RecyclerView Adapter 설정
        adapter = MyTaleAdapter( clickListener = { uri ->
            Log.d(TAG, "이미지 클릭 후")

            mediaPlayer.reset()                 // mediaPlayer 객체 재설정
            mediaPlayer.setDataSource(uri)
            mediaPlayer.setDisplay(binding.surfaceView.holder)
            mediaPlayer.prepareAsync()  // 비동기 준비
            Toast.makeText(applicationContext, "동영상 로딩중입니다", Toast.LENGTH_LONG).show()
        }, this)

        binding.myTaleRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.myTaleRecyclerView.adapter = adapter
        // 콜백 등록
        val itemTouchCallback = ItemTouchHelper(MyTaleItemTouchCallback(binding.myTaleRecyclerView))
        itemTouchCallback.attachToRecyclerView(binding.myTaleRecyclerView)

        // 액티비티 시작 직후 통신
        val myTaleListInfo = MyTaleListRequestModel(userId = AppData.instance.getUserId())
        Log.d(TAG, "통신 직전");
        RetrofitManager.instance.myTaleList(myTaleListInfo,
            completion = { httpResponseState, myTaleResponse ->
                when (httpResponseState) {
                    HTTP_RESPONSE_STATE.OKAY -> {
                        Log.d(Constants.TAG, "사용자 제작동화 리스트 api 호출 성공 : ${myTaleResponse}")

                        when (myTaleResponse?.state) {
                            MY_TALE_LIST_RESPONSE_STATE.SUCCESS -> {
                                Log.d(Constants.TAG, "사용자 제작동화 리스트 데이터 불러오기 성공")

                                val newList = adapter.currentList.toMutableList().apply {
                                    addAll(myTaleResponse.myTaleList)
                                }
                                adapter.submitList(newList)
                            }
                            MY_TALE_LIST_RESPONSE_STATE.FAIL -> {
                                Log.d(Constants.TAG, "사용자 제작동화 리스트 데이터 불러오기 실패")
                            }
                        }
                    }
                    HTTP_RESPONSE_STATE.FAIL -> {
                        Log.d(Constants.TAG, "사용자 제작동화 리스트 api 호출 실패 : ${myTaleResponse}");
                    }
                }
            })

        // 플레이어 준비가 완료되면
        mediaPlayer.setOnPreparedListener {
            Log.d(TAG, "준비 완료 리스너");
            mediaPlayer.start()
            mediaController.show() // 자동숨김 까지 시간 (기본 3000ms)

        }
        // 영상이 끝났을 때 감지
        mediaPlayer.setOnCompletionListener {
            Log.d(TAG, "영상 끝남");
            // 동영상 재생이 끝났을 때의 동작을 원한다면 이 곳에 코드를 추가하세요.
        }
        // 오류 시 감지
        mediaPlayer.setOnErrorListener { mediaPlayer, what, extra ->
            // 오류가 발생했을 때의 처리를 원한다면 이 곳에 코드를 추가하세요.
            Log.d(TAG, "영상 오류");
            false
        }
        // mediaController 숨김/해제 로직
        binding.surfaceView.setOnClickListener {
            if (mediaController.isShowing) {
                mediaController.hide()
            } else {
                mediaController.show() // 터치 이벤트 발생 시 MediaController를 보여줍니다.
            }
        }
        
    }//onCreate
    
    override fun onStop() {
        Log.d(TAG, "MyTaleListActivity - onStop() called");
        super.onStop()
        mediaController.hide()
        mediaPlayer.release()

    }


}