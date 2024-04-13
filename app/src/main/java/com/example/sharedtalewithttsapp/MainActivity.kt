package com.example.sharedtalewithttsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityMainBinding
import com.example.sharedtalewithttsapp.glide.GlideManager
import com.example.sharedtalewithttsapp.model.httpmodel.LoginRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.sharedpreferences.LoginSaveSharedPreferencesModel
import com.example.sharedtalewithttsapp.sharedpreferences.SharedManager
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.LOGIN_STATE
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "MainActivity - onCreate() called");

        val currentUser = sharedManager.getLoginSave()
        Log.d(TAG, "currentUser = ${currentUser}");
        if(currentUser.isLoginSave){
            binding.editLoginId.setText(currentUser.userId)
            binding.editLoginPasswd.setText(currentUser.passwd)
            binding.cbLoginSave.isChecked = true
        }



        // '회원가입하기' 버튼을 눌렀을 떄
        binding.memberJoinBtn.setOnClickListener {
            Log.d(TAG, "MainActivity - 회원가입하기 버튼 클릭 됨");
            val intent: Intent = Intent(this, MemberJoinActivity::class.java)
            startActivity(intent)
        }
        
        // '로그인' 버튼을 눌렀을 떄
        binding.loginBtn.setOnClickListener {
            Log.d(TAG, "MainActivity - 로그인 버튼 클릭 됨");

            val loginInfo = LoginRequestModel(id = binding.editLoginId.text.toString(),
                                            passwd = binding.editLoginPasswd.text.toString())
            RetrofitManager.instance.memberLogin(loginInfo, completion = {
                    httpResponseState, loginResponse ->
                when(httpResponseState){
                    HTTP_RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "로그인 api 호출 성공 : ${loginResponse}")
                        
                        when(loginResponse?.state){
                            LOGIN_STATE.SUCCESS -> {
                                Log.d(TAG, "로그인 성공")
                                AppData.instance.setUserId(loginResponse.user.id)
                                AppData.instance.setUserNickname(loginResponse.user.nickname)
                                AppData.instance.setUserEmail(loginResponse.user.email)
                                AppData.instance.setUserMobile(loginResponse.user.mobile)
                                
                                val intent: Intent = Intent(this, SelectChildProfileActivity::class.java)
                                startActivity(intent)
                            }
                            LOGIN_STATE.FAIL -> {
                                Log.d(TAG, "로그인 실패")
                                AlertDialogManager.instance.simpleAlertDialog("아이디 또는 비밀번호가 일치하지 않습니다..!", this, null)
                            }
                        }
                    }
                    HTTP_RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "로그인 api 호출 실패 : ${loginResponse}");
                        AlertDialogManager.instance.simpleAlertDialog("네트워크 오류..!", this, null)
                    }
                }
            })
        }

    }// onCreate 코드 끝

    override fun onStart() {
        Log.d(TAG, "MainActivity - onStart() called");
        super.onStart()
    }

    override fun onPause() {
        Log.d(TAG, "MainActivity - onPause() called");
        super.onPause()
    }
    override fun onStop() {
        Log.d(TAG, "MainActivity - onStop() called");
        if(binding.cbLoginSave.isChecked){
            val currentUser = LoginSaveSharedPreferencesModel().apply {
                userId = binding.editLoginId.text.toString()
                passwd = binding.editLoginPasswd.text.toString()
                isLoginSave = true
            }
            Log.d(TAG, "currentUser = ${currentUser}");
            sharedManager.saveLoginSave(currentUser)
        }else{
            val currentUser = LoginSaveSharedPreferencesModel().apply {
                userId = ""
                passwd = ""
                isLoginSave = false
            }
            Log.d(TAG, "currentUser = ${currentUser}");
            sharedManager.saveLoginSave(currentUser)
        }
        super.onStop()
    }
    override fun onDestroy() {
        Log.d(TAG, "MainActivity - onDestroy() called");

        super.onDestroy()
    }
}