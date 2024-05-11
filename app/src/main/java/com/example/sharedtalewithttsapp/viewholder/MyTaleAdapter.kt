package com.example.sharedtalewithttsapp.viewholder

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharedtalewithttsapp.R
import com.example.sharedtalewithttsapp.media.MediaPlayerManager
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import java.util.Collections

//ListAdapter에서 목록관리하므로, collection으로 받을 필요 없음.
class MyTaleAdapter(private val clickListener: (String) -> Unit, private val activity : Context)
    : ListAdapter<MyTaleModel, MyTaleAdapter.CustomViewHolder>(MyTaleComparator) {
    companion object MyTaleComparator : DiffUtil.ItemCallback<MyTaleModel>() {
        override fun areItemsTheSame(oldItem: MyTaleModel, newItem: MyTaleModel): Boolean {
            Log.d(TAG, "MyTaleComparator - areItemsTheSame() called");
            return oldItem.videoURI == newItem.videoURI
        }

        override fun areContentsTheSame(oldItem: MyTaleModel, newItem: MyTaleModel): Boolean {
            Log.d(TAG, "MyTaleComparator - areContentsTheSame() called");
            // Item의 id 값으로 비교한다. object에 id가 있으면 그 id를 사용하여 비교.
            return oldItem == newItem
        }
    }


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 아이템을 이동시키면 배경 색 변경
        fun setBackground(color: Int) {
            itemView.setBackgroundColor(color)
        }

        var titleText : TextView = itemView.findViewById<TextView>(R.id.my_tale_recycler_view_title)
        var thumbnail : ImageView = itemView.findViewById<ImageView>(R.id.my_tale_recycler_view_image)
        fun bindInfo(data: MyTaleModel, position: Int) {
            Log.d(TAG, "CustomViewHolder - bindInfo() called");
            Log.d(TAG, "data : ${data}");
            titleText.text = data.title

            Glide.with(activity)
                .load(data.thumbnail)
                .override(140, 140)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(thumbnail)

            thumbnail.setOnClickListener {
                clickListener(data.videoURI)
            }

        }
    }


    // 아이템이 보여질 때 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_tale_recycler_view, parent, false)
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
    fun addItem(newItem: MyTaleModel) {
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

}
