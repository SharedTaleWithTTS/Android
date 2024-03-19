package com.example.sharedtalewithttsapp.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.R
import com.example.sharedtalewithttsapp.databinding.CommentRecyclerViewBinding
import com.example.sharedtalewithttsapp.model.httpmodel.CommentListResponseModel
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class CommentViewHolder(val binding: CommentRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

class CommentAdapter(private var datas: CommentListResponseModel, private val clickListener: (String, Int ,Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val commentListSave = datas.commentList

    // 현재 시간과의 차이를 분 단위로 받아 "몇 분 전"과 같은 형식으로 변환하는 함수
    private fun formatTimeAgo(minutes: Long): String {
        val formatter = PeriodFormatterBuilder()
            .appendMinutes().appendSuffix(" 분 전").printZeroNever()
            .appendHours().appendSuffix(" 시간 전").printZeroNever()
            .toFormatter()

        return formatter.print(Period().withMinutes(minutes.toInt()))
    }

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

        val dateStringFromServer = datas.commentList[position].writeDate    // 서버에서 받은 문자열
        Log.d(TAG, "서버시간 : ${dateStringFromServer}")
        val currentTime = DateTime.now()                                    // 현재 시간
        val serverTime = DateTime.parse(dateStringFromServer)               // 서버에서 받아온 날짜 문자열을 DateTime 객체로 변환
        val duration = Duration(serverTime, currentTime)                    // 현재 시간과 서버에서 받아온 시간의 차이 계산
        val minutes = duration.standardMinutes                              // 차이를 분 단위로 변환
        val resultDate = when {
            minutes < 60 -> {
                "${minutes}분 전"
            }
            minutes < 1440 -> {
                val hours = minutes / 60
                val remainingMinutes = minutes % 60
                "${hours}시간 ${remainingMinutes}분 전"
            }
            else -> {
                val days = minutes / 1440
                "${days}일 전"
            }
        }


        binding.textNickname.setText(datas.commentList[position].nickname)
        binding.textChildInfo.setText("${datas.commentList[position].childAge}세 ${tempChildGender} ${tempChildPersonality}")
        binding.textDate.setText(resultDate)
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

            clickListener(datas.commentList[position].commentId, position , likeState)
        }
    }
}