package com.example.sharedtalewithttsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivitySearchBinding
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.SearchRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.SEARCH_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.ChildProfileAdapter
import com.example.sharedtalewithttsapp.viewholder.SearchResultAdapter

class SearchActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "SearchActivity - onCreate() called");

        setSupportActionBar(binding.toolBar)    // 툴바 표시
        initSearchView(this)                        // SearchView 표시

    }

    // SearchView 기본 동작 설정
    private fun initSearchView(context : SearchActivity) {

        binding.searchView.isSubmitButtonEnabled = true

        // return false = 키보드 내림, true = 키보드 유지
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(Constants.TAG, "검색 누름")

                val searchList = SearchRequestModel(type = "title", search = query)
                RetrofitManager.instance.search(searchList, completion = {
                        httpResponseState, searchResponse ->

                    when(httpResponseState){
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(Constants.TAG, "검색 api 호출 성공 : ${searchResponse}")

                            when(searchResponse?.state){
                                SEARCH_RESPONSE_STATE.SUCCESS -> {
                                    Log.d(Constants.TAG, "불러오기 성공")
                                    binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(context) // context = SearchActivity
                                    binding.searchResultRecyclerView.adapter = SearchResultAdapter(searchResponse!!, clickListener = {
                                        taleId ->
                                        var taleModel : TaleModel

                                        val intent: Intent = Intent(context, TaleProfileActivity::class.java)
                                        for(i in searchResponse.searchResult){
                                            if(taleId == i.id) {
                                                taleModel = i
                                                intent.putExtra("taleInfo", taleModel)
                                            }
                                        }
                                        startActivity(intent)
                                    })
                                }
                                SEARCH_RESPONSE_STATE.FAIL -> {
                                    Log.d(Constants.TAG, "불러오기 실패")
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(Constants.TAG, "검색 호출 실패 : ${searchResponse}");
                        }
                    }
                })


                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(Constants.TAG, "검색 텍스트 변경");
                Log.d(Constants.TAG, "검색 문자열 : ${newText}");

                return true
            }
        })
    }
}