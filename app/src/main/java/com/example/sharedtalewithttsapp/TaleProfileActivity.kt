package com.example.sharedtalewithttsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.ActivityTaleProfileBinding
import com.example.sharedtalewithttsapp.glide.GlideManager
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.FavoritesRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.RateRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.StateCheckRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.StateCheckResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.TaleLikeRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.FAVORITES_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.RATE_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.STATE_CHECK_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.TALE_LIKE_RESPONSE_STATE

class TaleProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityTaleProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaleProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "TaleProfileActivity - onCreate() called");

        title = ""

        val taleInfo = intent.getParcelableExtra<TaleModel>("taleInfo") // intent 가져오기
        var stateCheck : StateCheckResponseModel? = null        // 통신으로 받을 모델
        var likeState : Boolean = false
        var favoritesState : Boolean = false


        // 상태 체크 위한 통신
        val stateCheckInfo = StateCheckRequestModel(childId = AppData.instance.getChildId(), taleId = taleInfo!!.id)
        
        RetrofitManager.instance.stateCheck(stateCheckInfo,
            completion = { httpResponseState, stateCheckResponse ->
                when (httpResponseState) {
                    HTTP_RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "상태 check api 호출 성공 : ${stateCheckResponse}")

                        when (stateCheckResponse?.state) {
                            STATE_CHECK_RESPONSE_STATE.SUCCESS -> {
                                Log.d(TAG, "상태 check 성공")
                                stateCheck = stateCheckResponse
                                binding.textMyRating.setText("내 평점 : ${stateCheck!!.rate}")
                                likeState = stateCheck!!.likeCheck.toBoolean()    // 좋아요 상태 구분
                                favoritesState = stateCheck!!.favoritesCheck.toBoolean()
                                if(likeState){
                                    binding.likeBtn.setImageResource(R.drawable.like_activate)
                                }
                                if(favoritesState){
                                    binding.favoritesBtn.setImageResource(R.drawable.favorites_activate)
                                }
                                binding.ratingBar.rating = stateCheck!!.rate.toFloat()
                            }
                            STATE_CHECK_RESPONSE_STATE.FAIL -> {
                                Log.d(TAG, "상태 check 실패")
                            }
                        }
                    }
                    HTTP_RESPONSE_STATE.FAIL -> {
                        Log.d(TAG, "상태 check api 호출 실패 : ${stateCheckResponse}");
                    }
                }
            })
        // 통신 끝



        setSupportActionBar(binding.toolBar)    // 툴바 표시
        supportActionBar?.setDisplayHomeAsUpEnabled(true)                   // 뒤로가기표시
        binding.toolBar.setNavigationOnClickListener { onBackPressed() }    // 뒤로가기 클릭시 뒤로가기
        
        // 초기 화면 정보 표시
        binding.textTitle.setText("${taleInfo?.title}")
        binding.textLike.setText("좋아요 : ${taleInfo?.like}")
        binding.textReviews.setText("댓글 수 : ${taleInfo?.reviews}")
        binding.textViews.setText("조회수 : ${taleInfo?.views}")
        binding.textRate.setText("평점 : ${taleInfo?.rate}")



        GlideManager.instance.serverImageRequest(
            this,
            taleInfo!!.taleImage,
            500,
            500,
            binding.imageTale
        )

        // 동화 읽기 버튼을 눌렀을 때
        binding.startTaleReadingBtn.setOnClickListener {
            Log.d(TAG, "동화 읽기 버튼 누름");
            val intent: Intent = Intent(this, ReadingTaleActivity::class.java)
            intent.putExtra("taleId", taleInfo?.id)
            startActivity(intent)
        }

        // 댓글 작성 버튼을 눌렀을 때
        binding.commentBtn.setOnClickListener {
            Log.d(TAG, "댓글 작성 버튼 누름");
            val intent: Intent = Intent(this, EditCommentActivity::class.java)
            intent.putExtra("taleId", taleInfo?.id)
            startActivity(intent)
        }

        // 좋아요 버튼을 눌렀을 때
        binding.likeBtn.setOnClickListener {
            Log.d(TAG, "좋아요 누름");

            val taleLike = TaleLikeRequestModel(childId = AppData.instance.getChildId(),
                                                taleId = taleInfo.id,
                                                commentId = "")
            RetrofitManager.instance.addTaleLike(taleLike,
                completion = { httpResponseState, taleLikeResponse ->
                    when (httpResponseState) {
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(TAG, "좋아요 추가 api 호출 성공 : ${taleLikeResponse}")

                            when (taleLikeResponse?.state) {
                                TALE_LIKE_RESPONSE_STATE.SUCCESS -> {
                                    Log.d(TAG, "좋아요 변경 성공")
                                    if(likeState){
                                        binding.likeBtn.setImageResource(R.drawable.like_disabled)
                                        binding.textLike.setText("좋아요 : ${taleLikeResponse.likeNum}")
                                        likeState = false
                                    }else{
                                        binding.likeBtn.setImageResource(R.drawable.like_activate)
                                        binding.textLike.setText("좋아요 : ${taleLikeResponse.likeNum}")
                                        likeState = true
                                    }
                                }
                                TALE_LIKE_RESPONSE_STATE.FAIL -> {
                                    Log.d(TAG, "좋아요 변경 실패")
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "좋아요 추가 api 호출 실패 : ${taleLikeResponse}");
                        }
                    }
                })
        }

        // 즐겨찾기 버튼을 눌렀을 때
        binding.favoritesBtn.setOnClickListener {
            Log.d(TAG, "즐찾 버튼 누름");
            val favorites = FavoritesRequestModel(childId = AppData.instance.getChildId(),
                                                taleId = taleInfo.id)
            RetrofitManager.instance.favorites(favorites,
                completion = { httpResponseState, favoritesResponse ->
                    when (httpResponseState) {
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(TAG, "즐찾 api 호출 성공 : ${favoritesResponse}")

                            when (favoritesResponse?.state) {
                                FAVORITES_RESPONSE_STATE.SUCCESS -> {
                                    Log.d(TAG, "즐찾 변경 성공")
                                    if(favoritesState){
                                        binding.favoritesBtn.setImageResource(R.drawable.favorites_disabled)
                                        favoritesState = false
                                    }else{
                                        binding.favoritesBtn.setImageResource(R.drawable.favorites_activate)
                                        favoritesState = true
                                    }
                                }
                                FAVORITES_RESPONSE_STATE.FAIL -> {
                                    Log.d(TAG, "즐찾 변경 실패")
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "즐찾 api 호출 실패 : ${favoritesResponse}");
                        }
                    }
                })
        }

        // 평점바를 눌렀을 때
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            binding.textMyRating.setText("내 평점 : ${fl}")

            val rateInfo = RateRequestModel(childId = AppData.instance.getChildId(),
                                            taleId = taleInfo.id,
                                            rate = fl.toString())
            RetrofitManager.instance.rate(rateInfo,
                completion = { httpResponseState, rateResponse ->
                    when (httpResponseState) {
                        HTTP_RESPONSE_STATE.OKAY -> {
                            Log.d(TAG, "평점 갱신 api 호출 성공 : ${rateResponse}")

                            when (rateResponse?.state) {
                                RATE_RESPONSE_STATE.SUCCESS -> {
                                    Log.d(TAG, "평점 갱신 성공")
                                    binding.textRate.setText("평점 : ${rateResponse.avgRate}")
                                }
                                RATE_RESPONSE_STATE.FAIL -> {
                                    Log.d(TAG, "평점 갱신 실패")
                                }
                            }
                        }
                        HTTP_RESPONSE_STATE.FAIL -> {
                            Log.d(TAG, "평점 갱신 api 호출 실패 : ${rateResponse}");
                        }
                    }
                })
        }


    }
}