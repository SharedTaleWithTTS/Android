package com.example.sharedtalewithttsapp.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ChildProfileRecyclerViewBinding
import com.example.sharedtalewithttsapp.databinding.SearchResultRecyclerViewBinding
import com.example.sharedtalewithttsapp.model.httpmodel.ChildProfileResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.SearchResponseModel
import com.example.sharedtalewithttsapp.utils.Constants

class SearchResultViewHolder(val binding: SearchResultRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

class SearchResultAdapter(private var datas: SearchResponseModel, private val clickListener: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        SearchResultViewHolder(SearchResultRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return datas.searchResult.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(Constants.TAG, "SearchResultAdapter - position = ${position}");
        val binding = (holder as SearchResultViewHolder).binding
        binding.textTaleTitle.text = datas.searchResult[position].title

        binding.recyclerViewLayout.setOnClickListener {
            Log.d(Constants.TAG, "index = ${position} 클릭 됨")
            clickListener(datas.searchResult[position].id)
        }
    }
}