package com.example.sharedtalewithttsapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityHomeScreenBinding
import com.example.sharedtalewithttsapp.databinding.HomeScreenNavigationHeaderBinding
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.LoginRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HOME_SCREEN_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.LOGIN_STATE
import com.example.sharedtalewithttsapp.utils.Logr
import com.example.sharedtalewithttsapp.viewholder.TaleListAdapter
import kotlin.math.log

class HomeScreenActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeScreenBinding
    lateinit var navigationBinding : HomeScreenNavigationHeaderBinding
    lateinit var toggle : ActionBarDrawerToggle
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
        toggle = ActionBarDrawerToggle(this, binding.homeScreenDrawer, binding.toolBar, R.string.drawer_open, R.string.drawer_close)
        //binding.homeScreenDrawer.addDrawerListener(toggle)
        toggle.syncState()  // ActionBarDrawerToggle의 상태를 동기화

        
        
        // 드로어 뷰에 기본적으로 보일 정보



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
                            binding.favoriteRecyclerView.adapter = TaleListAdapter(homeScreenResponse.favorites, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(this, TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.favorites){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, this)
                            binding.favoriteRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

                            // 즐겨찾기 한 동화 출력 끝   //

                            // 여기서부터 최근 본 동화 출력
                            binding.recentlyViewRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                            binding.recentlyViewRecyclerView.adapter = TaleListAdapter(homeScreenResponse.recentlyView, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(this, TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.recentlyView){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, this)
                            binding.recentlyViewRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

                            // 최근 본 동화 출력 끝   //

                            // 여기서부터 추천 동화 출력
                            binding.recommendRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                            binding.recommendRecyclerView.adapter = TaleListAdapter(homeScreenResponse.recommend, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(this, TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.recommend){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, this)
                            binding.recommendRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL))

                            // 추천 동화 출력 끝   //
                        }
                        HOME_SCREEN_RESPONSE_STATE.FAIL -> {
                            Log.d(Constants.TAG, "불러오기 실패");
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

        binding.homeScreenDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }
            override fun onDrawerOpened(drawerView: View) {
                Log.d(TAG, "토글 누름");

                val t = findViewById<TextView>(R.id.header_nickname)
                val t2 = findViewById<TextView>(R.id.header_child_name)
                t.text = AppData.instance.getUserNickname()
                t2.text = AppData . instance . getChildInfo ().childName
                /*
                navigationBinding = HomeScreenNavigationHeaderBinding.inflate(layoutInflater)
                navigationBinding.headerNickname.setText("set")
                 */
            }
            override fun onDrawerClosed(drawerView: View) {
            }
            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        
        // 네비게이션 뷰 아이템 선택 됐을 때
        binding.homeScreenNavigationView.setNavigationItemSelectedListener {
            
            when(it.itemId){
                // 동화 제작하기 버튼을 눌렀을 때
                R.id.navigation_make_tale -> {
                    val intent = Intent(this, CreateTaleActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_my_tale_list -> {
                    val intent = Intent(this, MyTaleListActivity::class.java)
                    startActivity(intent)
                }
                // 아이 변경 눌렀을 때
                R.id.navigation_change_child -> {
                    val intent = Intent(this, SelectChildProfileActivity::class.java)
                    startActivity(intent)
                }
                // 로그아웃 눌렀을 때
                R.id.navigation_logout -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                // 아이 삭제 눌렀을 때
                R.id.navigation_delete_child -> {
                    // 핸들러 등록
                    val eventHandler =
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            when (i) {
                                DialogInterface.BUTTON_POSITIVE -> {

                                    // 통신 코드 시작 //
                                    RetrofitManager.instance.deleteChild(AppData.instance.getChildId(), completion = {
                                            httpResponseState, httpResponseCode ->
                                        when(httpResponseState){
                                            HTTP_RESPONSE_STATE.OKAY -> {
                                                Log.d(TAG, "아이 삭제 api 호출 성공 : ${httpResponseCode}")
                                                val intent: Intent = Intent(this, SelectChildProfileActivity::class.java)
                                                finish()
                                                startActivity(intent)
                                            }
                                            HTTP_RESPONSE_STATE.FAIL -> {
                                                Log.d(TAG, "아이 삭제 api 호출 실패");
                                                AlertDialogManager.instance.simpleAlertDialog("네트워크 오류..!", this, null)
                                            }
                                        }
                                    })
                                    // 통신 코드 종료 //

                                }
                            }
                        }
                    AlertDialogManager.instance.ynAlertDialog("정말로 아이 삭제 하시겠습니까...??", this, eventHandler)

                }
                // 회원 탈퇴 눌렀을 때
                R.id.navigation_withdraw_member -> {
                    // 핸들러 등록
                    val eventHandler =
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            when (i) {
                                DialogInterface.BUTTON_POSITIVE -> {

                                    // 통신 코드 시작 //
                                    RetrofitManager.instance.withDraw(AppData.instance.getUserId(), completion = {
                                            httpResponseState, httpResponseCode ->
                                        when(httpResponseState){
                                            HTTP_RESPONSE_STATE.OKAY -> {
                                                Log.d(TAG, "회원 탈퇴 api 호출 성공 : ${httpResponseCode}")
                                                val intent: Intent = Intent(this, MainActivity::class.java)
                                                finish()
                                                startActivity(intent)
                                            }
                                            HTTP_RESPONSE_STATE.FAIL -> {
                                                Log.d(TAG, "회원탈퇴 api 호출 실패");
                                                AlertDialogManager.instance.simpleAlertDialog("네트워크 오류..!", this, null)
                                            }
                                        }
                                    })
                                    // 통신 코드 종료 //

                                }
                            }
                        }
                    AlertDialogManager.instance.ynAlertDialog("정말로 탈퇴 하시겠습니까...??", this, eventHandler)

                }

            }
            true
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