package com.example.sharedtalewithttsapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.alertdialog.AlertDialogManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityCreateTaleBinding
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.sharedpreferences.CreateTaleSaveSharedPreferences
import com.example.sharedtalewithttsapp.sharedpreferences.PreferenceHelper.get
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.CreateTaleAdapter
import com.example.sharedtalewithttsapp.viewholder.CreateTaleModel
import com.example.sharedtalewithttsapp.viewholder.ItemTouchCallback
import com.example.sharedtalewithttsapp.viewholder.PickImageListAdapter
import com.example.sharedtalewithttsapp.viewholder.PickVoiceListAdapter
import com.example.sharedtalewithttsapp.viewholder.PickVoiceListModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException


class CreateTaleActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateTaleBinding
    // click 메서드 상수
    private val ON_IMAGE_CLICKED = 100
    private val ON_VOICE_CLICKED = 101
    private var targetPosition : Int = 0

    private lateinit var sharedPreferences: CreateTaleSaveSharedPreferences
    private var makeTaleState : Boolean = false

    private lateinit var adapter: CreateTaleAdapter
    private lateinit var imageAdapter: PickImageListAdapter
    private lateinit var voiceAdapter: PickVoiceListAdapter


    private val pickImage = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()){ uris ->
        if(uris != null){
            val newList = imageAdapter.currentList.toMutableList()
            for (uri in uris) {

                // 선택한 각 Uri에 대해 지속적인 접근 권한을 얻습니다.
                try {
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: SecurityException) {
                    Log.d(TAG, "권한 요청 실패 : ${e}");
                }
                newList.add(uri)
            }
            imageAdapter.submitList(newList)
        }
    }
    private val pickAudio = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()){ uris ->
        Log.d(TAG, "uris : ${uris}");
        if(uris != null){
            val newList = voiceAdapter.currentList.toMutableList()
            for (uri in uris) {
                // 선택한 각 Uri에 대해 지속적인 접근 권한을 얻습니다.
                try {
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: SecurityException) {
                    Log.d(TAG, "권한 요청 실패 : ${e}");
                }
                val audioTitle = getAudioTitle(uri)
                newList.add(PickVoiceListModel(audioTitle, uri))
            }
            voiceAdapter.submitList(newList)
        }
    }
    private val recordAudioLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            showRenameDialog(uri)
        }
    }
    // 저장소의 오디오 제목 추출
    private fun getAudioTitle(uri: Uri): String {
        var title = ""
        val projection = arrayOf(MediaStore.Audio.Media.TITLE)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val titleIndex = it.getColumnIndex(MediaStore.Audio.Media.TITLE)

                title = it.getString(titleIndex) ?: "알 수 없는 오디오"
            }
        }
        return title
    }
    // 녹음 앱 찾고 실행
    private fun startRecording() {
        // 녹음을 위한 인텐트 생성
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        try {
            // 녹음 앱을 시작
            recordAudioLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            // 녹음 앱을 처리할 수 있는 앱이 없을 경우
            Toast.makeText(this, "녹음 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    // 녹음을 마친 녹음 파일에 제목 설정하기
    private fun showRenameDialog(uri: Uri?) {
        val context = this // 현재 Activity의 Context
        val editText = EditText(context).apply {
            hint = "제목을 입력하세요"
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("녹음 파일 제목 설정")
            .setView(editText)
            .setPositiveButton("저장", null) // null을 넣어 초기에는 클릭 리스너를 설정하지 않음
            .setNegativeButton("녹음 취소", null)
            .create()

        dialog.setOnShowListener {
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val title = editText.text.toString()
                if (title.isNotEmpty()) {
                    // 입력된 제목을 사용하고 대화 창을 닫음
                    saveRecording(title, uri)
                    dialog.dismiss()
                } else {
                    // 제목이 입력되지 않았을 때 사용자에게 알림
                    Toast.makeText(applicationContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }
    private fun saveRecording(title: String, uri: Uri?) {
        val newList = voiceAdapter.currentList.toMutableList()
        newList.add(PickVoiceListModel(title, uri))
        voiceAdapter.submitList(newList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CreateTaleActivity - onCreate() called");

        binding = ActivityCreateTaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "동화 제작하기"

        setSupportActionBar(binding.toolBar)    // 툴바 표시
        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기


        // RecyclerView Adapter 설정
        adapter = CreateTaleAdapter { position, clickType ->
            if(clickType == ON_IMAGE_CLICKED){
                Log.d(TAG, "position = ${position} 이미지 누름");
                targetPosition = position
                binding.imageListRecyclerView.visibility = View.VISIBLE
            }
            if(clickType == ON_VOICE_CLICKED){
                Log.d(TAG, "position = ${position} 음성 선택 버튼 누름");
                targetPosition = position
                binding.voiceListRecyclerView.visibility = View.VISIBLE
            }
        }

        imageAdapter = PickImageListAdapter( clickListener = { uri ->
            adapter.setImageAtPosition(targetPosition, uri)
            binding.imageListRecyclerView.visibility = View.INVISIBLE

        }, this)
        voiceAdapter = PickVoiceListAdapter( clickListener = { uri ->
            adapter.setVoiceAtPosition(targetPosition, uri)
            binding.voiceListRecyclerView.visibility = View.INVISIBLE
        }, this)



        binding.createTaleRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.createTaleRecyclerView.adapter = adapter
        binding.imageListRecyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.imageListRecyclerView.adapter = imageAdapter
        binding.voiceListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.voiceListRecyclerView.adapter = voiceAdapter
        // 콜백 등록
        val itemTouchCallback = ItemTouchHelper(ItemTouchCallback(binding.createTaleRecyclerView))
        itemTouchCallback.attachToRecyclerView(binding.createTaleRecyclerView)

        sharedPreferences = CreateTaleSaveSharedPreferences(this)

        //sharedPreferences.saveUriList(mutableListOf())      // 디버깅용 코드
        imageAdapter.submitList(sharedPreferences.getUriList())


        // 도움말 버튼 클릭 시
        binding.helpCreateTaleBtn.setOnClickListener {
            AlertDialogManager.instance.simpleAlertDialog(getString(R.string.help_create_tale),this,null)
        }
        // 이미지 불러오기 버튼 눌렀을 때
        binding.getImageBtn.setOnClickListener {
            pickImage.launch(arrayOf("image/*"))
        }
        // 음성 가져오기 버튼을 눌렀을 때
        binding.getVoiceBtn.setOnClickListener {
            pickAudio.launch("audio/*")
        }
        // 음성 녹음하기 버튼을 눌렀을 때
        binding.voiceRecodeBtn.setOnClickListener {
            startRecording()
        }
        // 장면 추가하기 버튼 눌렀을 때
        binding.addSceneBtn.setOnClickListener {
            adapter.addItem(CreateTaleModel(Uri.parse("android.resource://${packageName}/${R.drawable.btn_plus}"),null))
        }


        // 이미지와 오디오 업로드
        binding.createTaleBtn.setOnClickListener {
            Log.d(TAG, "영상 생성 버튼 누름");
            var taleImageList = mutableListOf<MultipartBody.Part?>()
            var taleVoiceList = mutableListOf<MultipartBody.Part?>()
            for(i in adapter.currentList){
                val bitmap = uriToBitmap(i.taleImage)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageBytes)
                val body = MultipartBody.Part.createFormData("image${i}", "image${i}.jpg", requestBody)
                taleImageList.add(body)

                i!!.taleVoice!!.let { uri ->
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        // MP3 파일을 바이트 배열로 읽습니다.
                        val mp3Bytes = inputStream.readBytes()
                        // MP3 파일의 바이너리 데이터를 요청 바디에 담습니다.
                        val requestBody = RequestBody.create("audio/*".toMediaTypeOrNull(), mp3Bytes)
                        // MultiPart 형식에 맞는 요청을 생성합니다.
                        val mp3RequestBody = MultipartBody.Part.createFormData("audio${i}", "audio${i}.mp3", requestBody)
                        taleVoiceList.add(mp3RequestBody)
                    }
                }
            }
            RetrofitManager.instance.sendImage(image = taleImageList,
                                                mp3 = taleVoiceList,
                                                userId = AppData.instance.getUserId(),
                                                title = binding.edtTitle.text.toString(),

                completion = { httpResponseState, response ->
                    when (httpResponseState) {
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(Constants.TAG, "이미지 전송 api 호출 성공 : ${response}")
                            // 이벤트 핸들러 등록
                            val eventHandler = DialogInterface.OnClickListener{ dialogInterface, i ->
                                when(i){
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        Log.d(TAG, "닫기버튼 누름")
                                        finish() // 뒤로가기
                                    }
                                }
                            }
                            sharedPreferences.saveUriList(mutableListOf())
                            makeTaleState = true
                            AlertDialogManager.instance.simpleAlertDialog("동화 생성이 완료되었습니다!", this, eventHandler)
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(Constants.TAG, "이미지 전송 api 호출 실패 : ${response}");
                        }
                    }
                })
            Toast.makeText(applicationContext, "영상을 만드는 중...", Toast.LENGTH_SHORT).show()

        }

    }// onCreate

    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onStop() {
        super.onStop()
        if(!makeTaleState){
            sharedPreferences.saveUriList(imageAdapter.currentList)
        }
    }






}