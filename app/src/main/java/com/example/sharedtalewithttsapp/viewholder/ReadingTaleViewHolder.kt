package com.example.sharedtalewithttsapp.viewholder

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.databinding.ActivityReadingTaleBinding
import com.example.sharedtalewithttsapp.databinding.ReadingTaleTextRecyclerViewBinding
import com.example.sharedtalewithttsapp.media.MediaPlayerManager
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.utils.Constants.TAG

class ReadingTaleViewHolder(val binding: ReadingTaleTextRecyclerViewBinding, val binding2: ActivityReadingTaleBinding): RecyclerView.ViewHolder(binding.root)

class ReadingTaleAdapter(private var datas: TestReadingTaleResponseModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ReadingTaleTextRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val binding2 = ActivityReadingTaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReadingTaleViewHolder(binding, binding2)
    }

    override fun getItemCount(): Int {
        return datas.ttsText.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        
        val binding = (holder as ReadingTaleViewHolder).binding
        val binding2 = (holder as ReadingTaleViewHolder).binding2

        binding.textListItemData.text = datas.ttsText[position]         // 해당 포지션의 텍스트 삽입
        binding.textListItemData.setBackgroundColor(Color.argb(0,0,0,0))        // 호출할 때마다 배경색 WHITE 유지
        var currentTTSIndex = MediaPlayerManager.instance.getTTSIndex()

        if(currentTTSIndex == position){
            binding.textListItemData.setBackgroundColor(Color.argb(150,255,255,0))
        }
        binding.textListItemData.setOnClickListener{
            Log.d(TAG, "인덱스 $position 클릭 됨");
            binding2.toolBar.visibility = View.VISIBLE
            binding2.settingBar.visibility = View.VISIBLE

            if(!MediaPlayerManager.instance.getPauseState()){   // 현재 재생중이라면
                MediaPlayerManager.instance.ttsAudioPause(MediaPlayerManager.instance.getTTSIndex())
            }
            MediaPlayerManager.instance.setCurrentPosition(0)
            MediaPlayerManager.instance.ttsAudioStart(datas, position)

        }
    }
}
