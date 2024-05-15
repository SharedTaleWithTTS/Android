package com.example.sharedtalewithttsapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.MediaController
import android.widget.Toast
import com.example.sharedtalewithttsapp.databinding.ActivityCreateTaleInfoBinding
import com.example.sharedtalewithttsapp.databinding.ActivityMyTaleListBinding
import com.example.sharedtalewithttsapp.model.CreateTaleInfoModel
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG

class CreateTaleInfoActivity : AppCompatActivity(), SurfaceHolder.Callback {
    lateinit var binding : ActivityCreateTaleInfoBinding

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var mediaController : MediaController
    private lateinit var surfaceHolder : SurfaceHolder
    private var createTaleInfo : CreateTaleInfoModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaleInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "유저 창작 동화"

        setSupportActionBar(binding.toolBar)    // 툴바 표시
        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기

        mediaPlayer = MediaPlayer()
        mediaController = MediaController(this)

        surfaceHolder = binding.surfaceView.holder
        surfaceHolder.addCallback(this)

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

        createTaleInfo = intent.getParcelableExtra<CreateTaleInfoModel>("createTaleInfo") // intent 가져오기
        // 기본 text UI 세팅
        binding.textUserNickname.text = "창작 동화 만든 유저 : ${createTaleInfo?.userNickname ?: "닉네임"}"
        binding.textTitle.text = "동화 제목 : ${createTaleInfo?.title ?: "영상 제목"}"

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


    }// onCreate

    override fun onStop() {
        Log.d(TAG, "CreateTaleInfoActivity - onStop() called");
        super.onStop()
        mediaController.hide()
        mediaPlayer.release()

    }

    // Surface가 생성될 때 호출
    override fun surfaceCreated(holder: SurfaceHolder) {
        // Surface 사용 준비 완료
        mediaPlayer.reset()                 // mediaPlayer 객체 재설정
        mediaPlayer.setDataSource(createTaleInfo!!.videoURI)
        mediaPlayer.setDisplay(binding.surfaceView.holder)
        mediaPlayer.prepareAsync()  // 비동기 준비
        Toast.makeText(applicationContext, "동영상 로딩중입니다", Toast.LENGTH_LONG).show()
    }

    // Surface의 형식이나 크기가 변경될 때 호출
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Surface 업데이트 처리
    }

    // Surface가 해제될 때 호출
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface 사용 종료 처리
    }
}