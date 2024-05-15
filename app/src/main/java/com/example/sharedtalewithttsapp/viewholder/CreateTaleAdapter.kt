package com.example.sharedtalewithttsapp.viewholder

import android.net.Uri
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.R
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import java.util.Collections

//ListAdapter에서 목록관리하므로, collection으로 받을 필요 없음.
class CreateTaleAdapter(private val clickListener: (Int, Int) -> Unit)
    : ListAdapter<CreateTaleModel, CreateTaleAdapter.CustomViewHolder>(CreateTaleComparator) {
    companion object CreateTaleComparator : DiffUtil.ItemCallback<CreateTaleModel>() {
        override fun areItemsTheSame(oldItem: CreateTaleModel, newItem: CreateTaleModel): Boolean {
            Log.d(TAG, "CreateTaleAdapter - areItemsTheSame() called");
            return oldItem.taleImage == newItem.taleImage
        }

        override fun areContentsTheSame(oldItem: CreateTaleModel, newItem: CreateTaleModel): Boolean {
            Log.d(TAG, "CreateTaleAdapter - areContentsTheSame() called");
            // Item의 id 값으로 비교한다. object에 id가 있으면 그 id를 사용하여 비교.
            return oldItem == newItem
        }
    }


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 아이템을 이동시키면 배경 색 변경
        fun setBackground(color: Int) {
            itemView.setBackgroundColor(color)
        }
        val text : TextView = itemView.findViewById<TextView>(R.id.create_tale_recycler_view_sence_text)
        var image : ImageView = itemView.findViewById<ImageView>(R.id.create_tale_recycler_view_image)
        val voiceBtn : Button = itemView.findViewById<Button>(R.id.create_tale_recycler_view_get_voice_btn)
        fun bindInfo(data: CreateTaleModel, position: Int) {
            Log.d(TAG, "CustomViewHolder - bindInfo() called");
            text.text = "동화 장면 ${position + 1}"
            image.setImageURI(data.taleImage)

            image.setOnClickListener {
                clickListener(position, 100)
            }

            voiceBtn.setOnClickListener {
                clickListener(position, 101)
            }
        }
    }


    // 아이템이 보여질 때 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.create_tale_recycler_view, parent, false)
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
    fun addItem(newItem: CreateTaleModel) {
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
        //notifyDataSetChanged()
    }

    // 삭제. 해당위치의 아이템을 삭제한다.
    fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }
    fun setImageAtPosition(position: Int, uri: Uri) {
        val newItem = getItem(position).copy(taleImage = uri) // 해당 포지션의 아이템을 복사하고 이미지를 새로운 비트맵으로 설정
        val newList = currentList.toMutableList()
        newList[position] = newItem // 리스트에서 해당 포지션의 아이템을 새로운 아이템으로 교체
        submitList(newList) // 업데이트된 리스트를 어댑터에 제출하여 RecyclerView를 새로 고침
        //notifyItemInserted(insertPosition) // 새로운 아이템이 추가된 포지션을 알림
    }
    fun setVoiceAtPosition(position: Int, uri: Uri?){
        val newItem = getItem(position).copy(taleVoice = uri)
        val newList = currentList.toMutableList()
        newList[position] = newItem // 리스트에서 해당 포지션의 아이템을 새로운 아이템으로 교체
        submitList(newList) // 업데이트된 리스트를 어댑터에 제출하여 RecyclerView를 새로 고침
    }

}
