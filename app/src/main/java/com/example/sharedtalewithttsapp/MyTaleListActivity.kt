package com.example.sharedtalewithttsapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MyTaleListActivity : AppCompatActivity() /*, MediaPlayer.OnPreparedListener*/{
    lateinit var binding : ActivityMyTaleListBinding
    
    private lateinit var adapter: MyTaleAdapter

    lateinit var exoPlayer : ExoPlayer
    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var mediaController : MediaController

    private fun initializePlayer(){
        if (::exoPlayer.isInitialized) {
            exoPlayer.stop() // 이전에 재생 중이던 동영상을 멈춥니다.
        } else {
            exoPlayer = ExoPlayer.Builder(this).build().also { exoPlayer ->
                binding.exoPlayer2View.player = exoPlayer

                // 오류 감지 리스너 추가
                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)

                        Log.e(TAG, "Player Error: ${error.message}")
                        Log.e(TAG, "errorCode: ${error.errorCode}")
                        //Toast.makeText(applicationContext, "재생 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTaleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "내 동화 보기"

        setSupportActionBar(binding.toolBar)    // 툴바 표시
        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기



        // RecyclerView Adapter 설정
        adapter = MyTaleAdapter( clickListener = { uri ->
            Log.d(TAG, "이미지 클릭 후")
            initializePlayer()
            exoPlayer.run {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                play()
            }
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




        
    }//onCreate
    
    override fun onStop() {
        Log.d(TAG, "MyTaleListActivity - onStop() called");
        super.onStop()
        if (::exoPlayer.isInitialized){
            exoPlayer.release()
        }
    }


}