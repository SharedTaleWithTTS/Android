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
import com.example.sharedtalewithttsapp.viewholder.HomeScreenViewPagerAdapter
import com.example.sharedtalewithttsapp.viewholder.TaleListAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.log

class HomeScreenActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeScreenBinding
    lateinit var navigationBinding : HomeScreenNavigationHeaderBinding
    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeScreenActivity - onCreate() called");

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "아이랑"

        setSupportActionBar(binding.toolBar)    // 툴바 표시
        toggle = ActionBarDrawerToggle(this, binding.homeScreenDrawer, binding.toolBar, R.string.drawer_open, R.string.drawer_close)
        //binding.homeScreenDrawer.addDrawerListener(toggle)
        toggle.syncState()  // ActionBarDrawerToggle의 상태를 동기화

        val fragmentAdapter = HomeScreenViewPagerAdapter(this)

        fragmentAdapter.addFragment(MyTaleFragment())       // 프래그먼트 추가
        fragmentAdapter.addFragment(AllTaleFragment())       // 프래그먼트 추가
        fragmentAdapter.addFragment(AllTaleListFragment())       // 프래그먼트 추가
        binding.viewPager2.adapter = fragmentAdapter        // 뷰 페이저에 적용
        binding.viewPager2.isUserInputEnabled = false       // 화면 슬라이드 제거
        // 탭 이름 설정
        val tabNameList = listOf<String>("내 동화", "이런 동화도 찾아보세요", "전체 동화 목록")
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            // 여기서 각 탭의 제목을 설정할 수 있습니다.

            tab.text = tabNameList[position]
        }.attach()




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
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "HomeScreenActivity - onStart() called");
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