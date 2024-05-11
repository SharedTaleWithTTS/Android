package com.example.sharedtalewithttsapp.viewholder


import android.content.Context
import android.graphics.drawable.Icon
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedtalewithttsapp.R
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import java.util.Collections

//ListAdapter에서 목록관리하므로, collection으로 받을 필요 없음.
class PickVoiceListAdapter(private val clickListener: (Uri?) -> Unit, private val activity : Context)
    : ListAdapter<PickVoiceListModel, PickVoiceListAdapter.CustomViewHolder>(PickVoiceListModelComparator) {
    companion object PickVoiceListModelComparator : DiffUtil.ItemCallback<PickVoiceListModel>() {
        override fun areItemsTheSame(oldItem: PickVoiceListModel, newItem: PickVoiceListModel): Boolean {
            Log.d(TAG, "ImageComparator - areItemsTheSame() called");
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: PickVoiceListModel, newItem: PickVoiceListModel): Boolean {
            Log.d(TAG, "ImageComparator - areContentsTheSame() called");
            // Item의 id 값으로 비교한다. object에 id가 있으면 그 id를 사용하여 비교.
            return oldItem == newItem
        }
    }


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 아이템을 이동시키면 배경 색 변경
        fun setBackground(color: Int) {
            itemView.setBackgroundColor(color)
        }

        var voiceTitle : TextView = itemView.findViewById<TextView>(R.id.voice_list_recycler_view_text)
        var playStopBtn : ImageView = itemView.findViewById(R.id.voice_list_recycler_view_play_stop_btn)
        var seekBar: SeekBar = itemView.findViewById(R.id.voice_list_recycler_view_seek_bar) // SeekBar를 레이아웃에 추가해야 합니다.
        private var mediaPlayer: MediaPlayer? = null
        private var handler = Handler(Looper.getMainLooper())
        private lateinit var runnable: Runnable
        fun bindInfo(data: PickVoiceListModel, position: Int) {
            Log.d(TAG, "CustomViewHolder - bindInfo() called");
            voiceTitle.text = data.title


            voiceTitle.setOnClickListener {
                clickListener(data.uri)
            }
            // MediaPlayer 초기화 및 SeekBar 업데이트
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(activity, data.uri).apply {
                setOnPreparedListener {
                    seekBar.max = duration
                    updateSeekBar()
                }
                setOnCompletionListener {
                    mediaPlayer?.seekTo(0)
                    seekBar.progress = 0
                }
            }

            // SeekBar 변경 시 MediaPlayer 업데이트
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) mediaPlayer?.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            // 재생 버튼 클릭 이벤트 처리
            playStopBtn.setOnClickListener {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                    val icon = Icon.createWithResource(activity, android.R.drawable.ic_media_play)
                    playStopBtn.setImageIcon(icon)
                } else {
                    mediaPlayer?.start()
                    updateSeekBar()
                    val icon = Icon.createWithResource(activity, android.R.drawable.ic_media_pause)
                    playStopBtn.setImageIcon(icon)
                }
            }
        }
        private fun updateSeekBar() {
            runnable = Runnable {
                seekBar.progress = mediaPlayer?.currentPosition ?: 0
                handler.postDelayed(runnable, 500)
            }
            handler.postDelayed(runnable, 0)
        }

        fun releaseMediaPlayer() {
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
        }
    }


    // 아이템이 보여질 때 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.voice_list_recycler_view, parent, false)
        Log.d(TAG, "inflate")

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.apply {
            //getItem : 위치의 값을 가져옴.
            bindInfo(getItem(position), position)
        }
    }
    // 새로운 아이템을 RecyclerView에 추가하는 함수
    fun addItem(newItem: PickVoiceListModel) {
        val newList = currentList.toMutableList()
        val insertPosition = newList.size
        newList.add(insertPosition, newItem)
        submitList(newList)
    }

    // 자리이동.현재 데이터 가져와서 바꿔치기한다. 위치가 한칸씩 바뀔때 마다 호출된다.
    fun moveItem(fromPosition: Int, toPosition: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPosition, toPosition)
        submitList(newList)
    }

    // 삭제. 해당위치의 아이템을 삭제한다.
    fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }
    // 뷰 홀더가 재활용될 때 호출됩니다.
    override fun onViewRecycled(holder: CustomViewHolder) {
        super.onViewRecycled(holder)
        holder.releaseMediaPlayer()
    }

}
