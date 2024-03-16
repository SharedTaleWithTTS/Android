package com.example.sharedtalewithttsapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.databinding.ActivityMemberJoinBinding
import com.example.sharedtalewithttsapp.model.httpmodel.IdCheckModel
import com.example.sharedtalewithttsapp.model.httpmodel.MemberJoinModel
import com.example.sharedtalewithttsapp.model.httpmodel.NicknameCheckModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.IDCHECK_STATE
import com.example.sharedtalewithttsapp.utils.MEMBERJOIN_STATE
import com.example.sharedtalewithttsapp.utils.NICKNAMECHECK_STATE
import java.util.regex.Pattern


class MemberJoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemberJoinBinding

    // 입력 형식에 맞는 데이터 여부
    private var idFlag = false
    private var passwordFlag = false
    private var passwordCheckFlag = false
    private var nicknameFlag = false
    private var emailFlag = false
    private var mobileFlag = false
    // 중복 확인 변수
    var id_check_state : Boolean = false
    var nickname_state : Boolean = false
    var passwd_state : Boolean = false
    // 리스너 정의
    private val idListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.idTextInputLayout.error = "아이디를 입력해주세요."
                        binding.idCheckBtn.isEnabled = false
                        idFlag = false
                    }
                    !idRegex(s.toString()) -> {
                        binding.idTextInputLayout.error = "아이디 형식이 맞지 않습니다"
                        binding.idCheckBtn.isEnabled = false
                        idFlag = false
                    }
                    else -> {
                        binding.idTextInputLayout.error = null
                        binding.idCheckBtn.isEnabled = true
                        idFlag = true
                        id_check_state = false
                    }
                }
                flagCheck()
            }
        }
    }
    private val passwordListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.passwordTextInputLayout.error = "비밀번호를 입력해주세요."
                        binding.passwordRecheckTextInputLayout.error = "다시 확인해주세요"
                        passwordFlag = false
                        passwordCheckFlag = false
                    }
                    !passwordRegex(s.toString()) -> {
                        binding.passwordTextInputLayout.error = "비밀번호 형식이 일치하지 않습니다."
                        binding.passwordRecheckTextInputLayout.error = "다시 확인해주세요"
                        passwordFlag = false
                        passwordCheckFlag = false
                    }
                    else -> {
                        binding.passwordTextInputLayout.error = null
                        binding.passwordRecheckTextInputLayout.error = "다시 확인해주세요"
                        passwordCheckFlag = false
                        passwordFlag = true
                    }
                }
                flagCheck()
            }
        }
    }
    private val passwordCheckListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.passwordRecheckTextInputLayout.error = "비밀번호를 입력해주세요."
                        passwordCheckFlag = false
                    }
                    !passwordRegex(s.toString()) -> {
                        binding.passwordRecheckTextInputLayout.error = "비밀번호 형식이 일치하지 않습니다."
                        passwordCheckFlag = false
                    }
                    binding.passwordTextInputEditText.text.toString() == binding.passwordRecheckTextInputEditText.text.toString() -> {
                        binding.passwordRecheckTextInputLayout.error = null
                        passwordCheckFlag = true
                    }
                    else -> {
                        binding.passwordRecheckTextInputLayout.error = "비밀번호가 일치하지 않습니다"
                        passwordCheckFlag = false
                    }
                }
                flagCheck()
            }
        }
    }
    private val nicknameListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.nicknameTextviewInputLayout.error = "닉네임을 입력해주세요."
                        binding.nicknameCheckBtn.isEnabled = false
                        nicknameFlag = false
                    }
                    !nicknameRegex(s.toString()) -> {
                        binding.nicknameTextviewInputLayout.error = "닉네임 형식이 맞지 않습니다"
                        binding.nicknameCheckBtn.isEnabled = false
                        nicknameFlag = false
                    }
                    else -> {
                        binding.nicknameTextviewInputLayout.error = null
                        binding.nicknameCheckBtn.isEnabled = true
                        nicknameFlag = true
                        nickname_state = false
                    }
                }
                flagCheck()
            }
        }
    }
    private val emailListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.emailTextviewInputLayout.error = "이메일를 입력해주세요."
                        emailFlag = false
                    }
                    !emailRegex(s.toString()) -> {
                        binding.emailTextviewInputLayout.error = "이메일 형식이 맞지 않습니다"
                        emailFlag = false
                    }
                    else -> {
                        binding.emailTextviewInputLayout.error = null
                        emailFlag = true
                    }
                }
                flagCheck()
            }
        }
    }
    private val mobileListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.mobileTextviewInputLayout.error = "핸드폰 번호를 입력해주세요."
                        mobileFlag = false
                    }
                    !mobileRegex(s.toString()) -> {
                        binding.mobileTextviewInputLayout.error = "핸드폰 번호 형식이 맞지 않습니다"
                        mobileFlag = false
                    }
                    else -> {
                        binding.mobileTextviewInputLayout.error = null
                        mobileFlag = true
                    }
                }
                flagCheck()
            }
        }
    }
    // 영문자 존재 여부를 확인하는 메서드
    private fun hasAlphabet(string: String): Boolean {
        for (i in string.indices) {
            if (Character.isAlphabetic(string[i].code)) {
                return true
            }
        }
        return false
    }

    // 정규식
    var email_pattern: Pattern = Patterns.EMAIL_ADDRESS
    var mobile_pattern: Pattern = Patterns.PHONE
    fun idRegex(id: String): Boolean {
        if ((hasAlphabet(id)) && (id.length >= 5)) {
            return true
        }
        return false
    }
    fun passwordRegex(password: String): Boolean {
        if (password.length >= 5) {
            return true
        }
        return false
    }
    fun nicknameRegex(nickname: String): Boolean {
        return true
    }
    fun emailRegex(email: String): Boolean {
        return email_pattern.matcher(email).matches()
    }
    fun mobileRegex(mobile: String): Boolean{
        return mobile_pattern.matcher(mobile).matches()
    }

    // 모든 정규식 일치 시 버튼 활성화
    fun flagCheck() {
        binding.memberJoinBtn.isEnabled = idFlag && passwordFlag && passwordCheckFlag && nicknameFlag && emailFlag && mobileFlag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // TextInputLayout과 TextWatcher 연결
        // idListener
        binding.idTextInputLayout.editText?.addTextChangedListener(idListener)
        binding.idTextInputEditText.hint = "영문 소문자를 포함한 5~20자"
        binding.idTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.idTextInputEditText.hint = ""
            } else {
                binding.idTextInputEditText.hint = "영문 소문자를 포함한 5~20자"
            }
        }
        // passwdListener
        binding.passwordTextInputLayout.editText?.addTextChangedListener(passwordListener)
        binding.passwordTextInputEditText.hint = "5~20자"
        binding.passwordTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordTextInputEditText.hint = ""
            } else {
                binding.passwordTextInputEditText.hint = "5~20자"
            }
        }
        // passwdCheckListener
        binding.passwordRecheckTextInputLayout.editText?.addTextChangedListener(passwordCheckListener)
        binding.passwordRecheckTextInputEditText.hint = "다시 입력해 주세요"
        binding.passwordRecheckTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordRecheckTextInputEditText.hint = ""
                if(binding.passwordTextInputEditText.text.toString() == binding.passwordRecheckTextInputEditText.text.toString()){
                    binding.passwordRecheckTextInputLayout.error = null
                }
            } else {
                binding.passwordRecheckTextInputEditText.hint = "다시 입력해 주세요"
            }
        }
        // nicknameListener
        binding.nicknameTextviewInputLayout.editText?.addTextChangedListener(nicknameListener)
        binding.nicknameTextviewInputEditText.hint = "닉네임을 입력하세요"
        binding.nicknameTextviewInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.nicknameTextviewInputEditText.hint = ""
            } else {
                binding.nicknameTextviewInputEditText.hint = "닉네임을 입력하세요"
            }
        }
        // emailListener
        binding.emailTextviewInputLayout.editText?.addTextChangedListener(emailListener)
        binding.emailTextviewInputEditText.hint = "이메일을 입력하세요"
        binding.emailTextviewInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailTextviewInputEditText.hint = ""
            } else {
                binding.emailTextviewInputEditText.hint = "이메일을 입력하세요"
            }
        }
        // moblieListener
        binding.mobileTextviewInputLayout.editText?.addTextChangedListener(mobileListener)
        binding.mobileTextviewInputEditText.hint = "핸드폰 번호를 입력하세요 '-' 포함"
        binding.mobileTextviewInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.mobileTextviewInputEditText.hint = ""
            } else {
                binding.mobileTextviewInputEditText.hint = "핸드폰 번호을 입력하세요 '-' 포함"
            }
        }

        // 회원가입 화면에서 '아이디 중복확인' 버튼을 눌렀을 때
        binding.idCheckBtn.setOnClickListener {
            Log.d(TAG, "MemberJoinActivity - 아이디 중복 확인 버튼 클릭 됨");

            val id = IdCheckModel(id = binding.idTextInputEditText.text.toString())
            RetrofitManager.instance.idCheck(id, completion = {
                    httpResponseState, idCheckState ->

                // 통신 성공, 실패 여부
                when(httpResponseState){
                    HTTP_RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "아이디 중복 체크 api 호출 성공 : ${idCheckState}")

                        when(idCheckState?.state){
                            IDCHECK_STATE.ID_AVAILABLE -> {
                                Log.d(TAG, "아이디 사용가능 여부 : 허용");
                                id_check_state = true
                                // 알림창 팝업
                                AlertDialogManager.instance.simpleAlertDialog("사용 가능한 아이디입니다", this, null)
                            }
                            IDCHECK_STATE.ID_DUPLICATE -> {
                                Log.d(TAG, "아이디 사용가능 여부 : 중복");
                                // 알림창 팝업
                                AlertDialogManager.instance.simpleAlertDialog("사용 불가한 아이디입니다", this, null)
                            }
                        }
                    }
                    HTTP_RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "아이디 중복 체크 api 호출 실패 : ${idCheckState}")
                        Toast.makeText(this, "네트워크 통신 오류. 다시 시도해주세요...", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        // 회원가입 화면에서 '닉네임 중복확인' 버튼을 눌렀을 때
        binding.nicknameCheckBtn.setOnClickListener {
            Log.d(TAG, "MemberJoinActivity - 닉네임 중복 확인 버튼 클릭 됨");

            val nickname = NicknameCheckModel(nickname = binding.nicknameTextviewInputEditText.text.toString())
            RetrofitManager.instance.nicknameCheck(nickname, completion = {
                    httpResponseState, nicknameCheckState ->
                // 통신 성공, 실패 여부
                when(httpResponseState){
                    HTTP_RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "아이디 중복 체크 api 호출 성공 : ${nicknameCheckState}")

                        when(nicknameCheckState?.state){
                            NICKNAMECHECK_STATE.NICKNAME_AVAILABLE-> {
                                Log.d(TAG, "닉네임 사용가능 여부 : 허용");
                                nickname_state = true
                                // 알림창 팝업
                                AlertDialogManager.instance.simpleAlertDialog("사용 가능한 닉네임입니다", this, null)
                            }
                            NICKNAMECHECK_STATE.NICKNAME_DUPLICATE -> {
                                Log.d(TAG, "닉네임 사용가능 여부 : 중복");
                                // 알림창 팝업
                                AlertDialogManager.instance.simpleAlertDialog("사용 불가한 닉네임입니다", this, null)
                            }
                        }
                    }
                    HTTP_RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "아이디 중복 체크 api 호출 실패 : ${nicknameCheckState}")
                        Toast.makeText(this, " 네트워크 통신 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        // 회원가입 화면에서 '회원가입 취소' 버튼을 눌렀을 떄
        binding.memberJoinCancelBtn.setOnClickListener {
            Log.d(TAG, "MemberJoinActivity - 회원가입 취소 버튼 누름");
            // 이벤트 핸들러 등록
            val eventHandler = DialogInterface.OnClickListener{ dialogInterface, i ->
                when(i){
                    DialogInterface.BUTTON_POSITIVE -> {
                        Log.d(TAG, "회원가입 취소-> 예")
                        finish()    // 액티비티 종료
                    }
                    DialogInterface.BUTTON_NEGATIVE -> Log.d(TAG, "회원가입 취소-> 아니오")
                }
            }
            AlertDialogManager.instance.ynAlertDialog("회원가입을 취소하시겠습니까??", this, eventHandler)
        }
        // 회원가입 화면에서 '회원가입하기'버튼을 눌렀을 때
        binding.memberJoinBtn.setOnClickListener {
            Log.d(TAG, "MemberJoinActivity - 회원가입 버튼 클릭 됨")
            
            // 비밀번호 and 비밀번호 확인 일치 확인
            var passwd1 = binding.passwordTextInputEditText.text.toString()
            var passwd2 = binding.passwordRecheckTextInputEditText.text.toString()
            if(passwd1 == passwd2){
                passwd_state = true
            }

            // 비밀번호 일치, 아이디, 닉네임 중복 체크 확인 여부
            if(passwd_state && id_check_state && nickname_state){
                val member = MemberJoinModel(id = binding.idTextInputEditText.text.toString(),
                    passwd = binding.passwordTextInputEditText.text.toString(),
                    nickname = binding.nicknameTextviewInputEditText.text.toString(),
                    email = binding.emailTextviewInputEditText.text.toString(),
                    mobile = binding.mobileTextviewInputEditText.text.toString()
                )
                RetrofitManager.instance.memberJoin(member, completion = {
                        httpResponseState, memberJoinState ->
                    // 통신 성공, 실패 여부
                    when(httpResponseState){
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(TAG, "회원가입 api 호출 성공 : ${memberJoinState}");
                            when(memberJoinState?.state){
                                MEMBERJOIN_STATE.SUCCESS -> {
                                    Log.d(TAG, "회원가입 성공")
                                    val eventHandler = DialogInterface.OnClickListener{ dialogInterface, i ->
                                        when(i){
                                            DialogInterface.BUTTON_POSITIVE -> {
                                                val intent: Intent = Intent(this, MainActivity::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                    AlertDialogManager.instance.simpleAlertDialog("회원가입에 성공했습니다..!", this, eventHandler)
                                }
                                MEMBERJOIN_STATE.FAIL -> {
                                    Log.d(TAG, "회원가입 실패")
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Toast.makeText(this, " 네트워크 통신 오류", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "회원가입 api 호출 실패 : ${memberJoinState}");
                        }
                    }
                })
            }else{
                if(!id_check_state) AlertDialogManager.instance.simpleAlertDialog("아이디 중복 확인을 완료해 주세요", this, null)
                if(!nickname_state) AlertDialogManager.instance.simpleAlertDialog("닉네임 중복 확인을 완료해 주세요", this, null)
                if(!passwd_state) AlertDialogManager.instance.simpleAlertDialog("비밀번호가 서로 일치하지 않습니다", this, null)
            }

        }
    }

}
