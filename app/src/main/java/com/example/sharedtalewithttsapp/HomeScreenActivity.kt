package com.example.sharedtalewithttsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityHomeScreenBinding
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HOME_SCREEN_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.TaleListAdapter

class HomeScreenActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeScreenActivity - onCreate() called");

    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "HomeScreenActivity - onStart() called");

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "아이랑"


        setSupportActionBar(binding.toolBar)    // 툴바 표시

        // 액티비티 시작시 통신, 홈 화면 정보 불러오기
        val homeScreenInfo = HomeScreenRequestModel(userId = AppData.instance.getUserId(), childId = AppData.instance.getChildId())
        RetrofitManager.instance.homeScreen(homeScreenInfo, completion = {
                httpResponseState, homeScreenResponse ->

            when(httpResponseState){
                HTTP_RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "홈 화면 요청 api 호출 성공 : ${homeScreenResponse}")

                    when(homeScreenResponse?.state){
                        HOME_SCREEN_RESPONSE_STATE.SUCCESS -> {
                            Log.d(Constants.TAG, "불러오기 성공")

                            // 여기서부터 즐겨찾기 한 동화 출력
                            binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                            binding.favoriteRecyclerView.adapter = TaleListAdapter(homeScreenResponse.favorites!!, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(this, TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.favorites){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                        intent.putExtra("favoritesState", true)
                                    }
                                }
                                startActivity(intent)
                            }, this)
                            binding.favoriteRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

                            // 즐겨찾기 한 동화 출력 끝   //
                        }
                        HOME_SCREEN_RESPONSE_STATE.FAIL -> {
                            Log.d(Constants.TAG, "불러오기 실패")
                        }
                    }
                }
                HTTP_RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "홈 화면 요청 api 실패 : ${homeScreenResponse}");
                }
            }
        })
        //통신코드 끝

        // 검색 버튼을 눌렀을 때
        binding.searchBtn.setOnClickListener {
            val intent: Intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    } // onStart

    override fun onResume() {
        Log.d(TAG, "HomeScreenActivity - onResume() called");
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "HomeScreenActivity - onPause() called");
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "HomeScreenActivity - onStop() called");
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "HomeScreenActivity - onDestroy() called");
        super.onDestroy()
    }

}