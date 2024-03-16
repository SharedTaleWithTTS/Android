package com.example.sharedtalewithttsapp.media

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.sharedtalewithttsapp.databinding.ActivityReadingTaleBinding
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.utils.API
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.viewholder.ReadingTaleViewHolder

class MediaPlayerManager {

    companion object {
        val instance = MediaPlayerManager()
    }
    private lateinit var binding: ActivityReadingTaleBinding
    private lateinit var mediaPlayer : MediaPlayer              // 미디어 플레이어 정의
    private val handler = Handler(Looper.getMainLooper())       // 핸들러 정의
    private var currentTaleId : String?= ""                              // 현재 동화 Id
    private var currentTTSIndex = 0                             // 현재 동화 인덱스
    private var currentPosition = 0                             // 현재 tts 재생 위치 (일시정지 시)
    private var currentRunnable = Runnable{}                      // 현재 스레드 작업 정의
    private var pauseState = true                                   // 현재 재생/일시정지 상태
    private val ttsSpeedList = arrayListOf("0.6", "0.8", "1.0", "1.2", "1.4")        // tts 속도 목록
    private var currentTTSSpeed = "1.0"
    private var currentVoice = "A"

    
    // 외부 바인딩 객체 가져오기
    fun initialize(binding: ActivityReadingTaleBinding) {
        this.binding = binding
    }
    
    // 플레이어 연결
    fun ttsRequest(url : String){
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepare()
            seekTo(instance.currentPosition)     // 오디오 위치 지정
            start()
            Log.d(TAG, "시작위치 : $currentPosition");
        }
    }
    // 플레이어 해제
    fun mediaPlayerRelease() {
        mediaPlayer.release()
    }
    // 동화 Id 가져오기
    fun getTaleId() : String?{
        return this.currentTaleId
    }
    // 동화 Id 설정하기
    fun setTaleId(id : String?){
        this.currentTaleId = id
    }
    // 현재 인덱스 가져오기
    fun getTTSIndex() : Int{
        return this.currentTTSIndex
    }
    // 현재 인덱스 설정
    fun setTTSIndex(index: Int){
        this.currentTTSIndex = index
    }
    // 현재 인덱스 증가
    fun doPlusIndex(){
        currentTTSIndex++
    }
    fun setCurrentPosition(position : Int){
        this.currentPosition = position
    }
    fun getPauseState() : Boolean{
        return this.pauseState
    }
    fun getTTSSpeedList() : List<String>{
        return this.ttsSpeedList
    }
    fun setCurrentTTSSpeed(speed : String){
        this.currentTTSSpeed = speed
    }
    fun getCurrentTTSSpeed() : String{
        return this.currentTTSSpeed
    }
    fun getCurrentVoice() : String{
        return this.currentVoice
    }
    fun setCurrentVoice(voice : String){
        this.currentVoice = voice
    }
    // 모든 텍스트 배경색 초기화
    fun allTextWhite(readingTale : TestReadingTaleResponseModel){
        for (index in 0 until readingTale.ttsText.size){
            val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(index) as? ReadingTaleViewHolder
            viewHolder?.binding?.textListItemData?.setBackgroundColor(Color.argb(0,0,0,0))  // 투명
        }
    }
    // 동화 읽기 중 배경색 변경
    fun ttsReadingSetBackgroundColor(index: Int, color: Int){
        // 리사이클러 뷰의 특정 인덱스만 골라 특성 변경 : 텍스트배경(노랑)
        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(index) as? ReadingTaleViewHolder
        viewHolder?.binding?.textListItemData?.setBackgroundColor(color)
    }
    // index 위치 Audio의 지정한 위치 부터 재생
    fun ttsAudioStart(readingTale : TestReadingTaleResponseModel, index : Int) {
        Log.d(TAG, "MediaPlayerManager - ttsAudioStart() called : index = $index");
        // 시작하려는 오디오가 '인덱스 끝 + 1' 일때
        if(readingTale.ttsText.size <= index){
            setTTSIndex(0)
            binding.fabPlayPause.extend()
            binding.fabPlayPause.setIconResource(android.R.drawable.ic_media_play)
            Log.d(TAG, "동화 끝");
            return
        }
        pauseState = false      // 일시정지 상태 아님
        binding.fabPlayPause.shrink()
        binding.fabPlayPause.setIconResource(android.R.drawable.ic_media_pause)     // 재생 아이콘으로 변화
        setTTSIndex(index)                                          // 현재 인덱스 초기화
        allTextWhite(readingTale)                                   // 하이라이트 초기화
        ttsReadingSetBackgroundColor(index, Color.argb(150,255,255,0))           // 하이라이트 설정
        //ttsRequest("${API.BASE_URL}${API.AUDIO_REQUEST}?num=8&seq=${index + 1}")                    // 지정한 위치부터 오디오 실행
        ttsRequest("${API.BASE_URL}${API.AUDIO_REQUEST}?num=${currentTaleId}&type=${currentVoice}&speed=${currentTTSSpeed}&seq=${index + 1}")                    // 지정한 위치부터 오디오 실행

        // 지정된 작업을 오디오 길이만큼 뒤에 처리
        this.currentRunnable = nextAudioHandler(readingTale, index)
        handler.postDelayed(currentRunnable, mediaPlayer.duration.minus(currentPosition).toLong())
    }
    // 현재 오디오 다음에 자동 재생될 핸들러 반환
    fun nextAudioHandler(readingTale : TestReadingTaleResponseModel, index : Int) : Runnable{
        // 예약된 작업 정의
        val runnable = Runnable {
            ttsReadingSetBackgroundColor(index, Color.argb(0,0,0,0))                // 하이라이팅 제거
            doPlusIndex()                                                   // 인덱스 증가
            currentPosition = 0
            ttsAudioStart(readingTale, currentTTSIndex)      // 다음 오디오 재생
        }
        return runnable
    }
    fun ttsAudioPause(index : Int){
        Log.d(TAG, "MediaPlayerManager - ttsAudioPause() called, index: $index");
        mediaPlayer.pause()
        pauseState = true
        handler.removeCallbacks(currentRunnable)
        this.currentPosition = mediaPlayer.currentPosition
        Log.d(TAG, "일시정지 직후 시작위치 : $currentPosition");
        mediaPlayerRelease()
    }

}