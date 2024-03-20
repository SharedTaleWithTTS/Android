package com.example.sharedtalewithttsapp.viewholder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.databinding.TaleListRecyclerViewBinding
import com.example.sharedtalewithttsapp.glide.GlideManager
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenResponseModel
import com.example.sharedtalewithttsapp.utils.Constants

class TaleListViewHolder(val binding: TaleListRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

class TaleListAdapter(private var datas: MutableList<TaleModel>, private val clickListener: (String) -> Unit, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        TaleListViewHolder(TaleListRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(Constants.TAG, "TaleListAdapter - position = ${position}");
        val binding = (holder as TaleListViewHolder).binding

        binding.title.setText(datas[position].title)
        GlideManager.instance.serverImageRequest(
            context,
            datas!![position].taleImage,
            500,
            500,
            binding.taleImage
        )


        binding.taleListRecyclerViewLayout.setOnClickListener {
            Log.d(Constants.TAG, "index = ${position} 클릭 됨")
            clickListener(datas[position].id)
        }
    }
}