package com.example.sharedtalewithttsapp.viewholder

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.HomeScreenActivity
import com.example.sharedtalewithttsapp.MemberJoinActivity

import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ChildProfileRecyclerViewBinding
import com.example.sharedtalewithttsapp.model.httpmodel.ChildProfileResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import kotlin.math.log



class ChildProfileViewHolder(val binding: ChildProfileRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

class ChildProfileAdapter(private var datas: ChildProfileResponseModel, private val clickListener: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ChildProfileViewHolder(ChildProfileRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return datas.childProfileList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = (holder as ChildProfileViewHolder).binding
        binding.textChildName.text = datas.childProfileList[position].childName
        
        binding.recyclerViewLayout.setOnClickListener {
            Log.d(TAG, "index = ${position} 클릭 됨")
            AppData.instance.setChildId(datas.childProfileList[position].childId)
            AppData.instance.setChildInfo(datas.childProfileList[position])
            Log.d(TAG, "현재 AppData childId : ${AppData.instance.getChildId()}");
            clickListener()
        }
    }
}