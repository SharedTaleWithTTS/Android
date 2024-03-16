package com.example.sharedtalewithttsapp.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.R
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.CommentRecyclerViewBinding
import com.example.sharedtalewithttsapp.databinding.SearchResultRecyclerViewBinding
import com.example.sharedtalewithttsapp.model.httpmodel.CommentListResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.SearchResponseModel
import com.example.sharedtalewithttsapp.utils.Constants

class CommentViewHolder(val binding: CommentRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

class CommentAdapter(private var datas: CommentListResponseModel, private val clickListener: (String, Int ,Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val commentListSave = datas.commentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CommentViewHolder(CommentRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return datas.commentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(Constants.TAG, "CommentAdapter - position = ${position}");
        val binding = (holder as CommentViewHolder).binding

        var tempChildGender = when(datas.commentList[position].childGender){
            "FEMALE" -> "여아"
            "MALE" -> "남아"
            else -> ""
        }
        var tempChildPersonality = when(datas.commentList[position].childPersonality){
            "E" -> "외향형"
            "I" -> "내향형"
            else -> ""
        }

        binding.textNickname.setText(datas.commentList[position].nickname)
        binding.textChildInfo.setText("${datas.commentList[position].childAge} ${tempChildGender} ${tempChildPersonality}")
        binding.textDate.setText(datas.commentList[position].writeDate)
        binding.likeNum.setText(datas.commentList[position].like)
        var q : String = ""
        var a : String = ""
        when(datas.commentList[position].direction){
            "CTOP" -> {
                q = "아이 : "
                a = "부모 : "
            }
            "PTOC" -> {
                q = "부모 : "
                a = "아이 : "
            }
        }
        binding.textQ.setText("${q}${datas.commentList[position].q}")
        binding.textA.setText("${a}${datas.commentList[position].a}")
        var likeState : Boolean = false
        if(datas.likeList.contains(commentListSave[position].commentId)){
            binding.likeBtn.setImageResource(R.drawable.like_activate)
            likeState = true
        }


        binding.likeBtn.setOnClickListener {
            Log.d(Constants.TAG, "index = ${position} 좋아요 클릭 됨")

            // notifyDataSetChanged() // 변경된 상태를 반영
            clickListener(datas.commentList[position].commentId, position , likeState)
        }
    }
}