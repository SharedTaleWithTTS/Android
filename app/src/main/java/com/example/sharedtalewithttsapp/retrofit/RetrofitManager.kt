package com.example.sharedtalewithttsapp.retrofit

import android.util.Log
import com.example.sharedtalewithttsapp.model.httpmodel.AddChildRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.AddChildResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.ChildProfileRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.ChildProfileResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.CommentListRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.CommentListResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.FavoritesRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.FavoritesResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.LoginRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.LoginResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TestReadingTaleResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.IdCheckModel
import com.example.sharedtalewithttsapp.model.httpmodel.IdCheckState
import com.example.sharedtalewithttsapp.model.httpmodel.MemberJoinModel
import com.example.sharedtalewithttsapp.model.httpmodel.MemberJoinState
import com.example.sharedtalewithttsapp.model.httpmodel.MyTaleListRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.MyTaleListResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.NicknameCheckModel
import com.example.sharedtalewithttsapp.model.httpmodel.NicknameCheckState
import com.example.sharedtalewithttsapp.model.httpmodel.RateRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.RateResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.SearchRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.SearchResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.StateCheckRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.StateCheckResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.TaleLikeRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.TaleLikeResponseModel
import com.example.sharedtalewithttsapp.model.httpmodel.WriteCommentRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.WriteCommentResponseModel
import com.example.sharedtalewithttsapp.utils.API
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {

    // 싱글턴 적용
    // 레트로핏 메니저를 가져올 때 인스턴스만 가져온다
    companion object {
        val instance = RetrofitManager()
    }

    // 레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
    // 아이디 중복 체크 api 호출
    fun idCheck(id: IdCheckModel, completion: (HTTP_RESPONSE_STATE, IdCheckState?) -> Unit){
        val call = iRetrofit?.idCheck(id) ?: return

        call.enqueue(object : retrofit2.Callback<IdCheckState>{
            override fun onResponse(call: Call<IdCheckState>,
                                    response: Response<IdCheckState>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<IdCheckState>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }
    // 닉네임 중복 체크 api 호출
    fun nicknameCheck(nickname: NicknameCheckModel, completion: (HTTP_RESPONSE_STATE, NicknameCheckState?) -> Unit){
        val call = iRetrofit?.nicknameCheck(nickname) ?: return

        call.enqueue(object : retrofit2.Callback<NicknameCheckState>{
            override fun onResponse(call: Call<NicknameCheckState>,
                                    response: Response<NicknameCheckState>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<NicknameCheckState>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 회원가입 api 호출      // completion을 활용하여 비동기로 나중에 알려줌
    fun memberJoin(member: MemberJoinModel, completion: (HTTP_RESPONSE_STATE, MemberJoinState?) -> Unit){
        val call = iRetrofit?.memberJoin(member) ?: return

        call.enqueue(object : retrofit2.Callback<MemberJoinState>{
            override fun onResponse(call: Call<MemberJoinState>,
                                    response: Response<MemberJoinState>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<MemberJoinState>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }
    // 로그인 api 호출
    fun memberLogin(loginInfo: LoginRequestModel, completion: (HTTP_RESPONSE_STATE, LoginResponseModel?) -> Unit){
        val call = iRetrofit?.memberLogin(loginInfo) ?: return

        call.enqueue(object : retrofit2.Callback<LoginResponseModel>{
            override fun onResponse(call: Call<LoginResponseModel>,
                                    response: Response<LoginResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }
    // 동화 읽기 test api 호출
    fun readingTale(readingTaleInfo: TestReadingTaleRequestModel, completion: (HTTP_RESPONSE_STATE, TestReadingTaleResponseModel?) -> Unit){
        val call = iRetrofit?.readingTale(readingTaleInfo) ?: return

        call.enqueue(object : retrofit2.Callback<TestReadingTaleResponseModel>{
            override fun onResponse(call: Call<TestReadingTaleResponseModel>,
                                    response: Response<TestReadingTaleResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<TestReadingTaleResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 아이 추가 api 호출
    fun addChild(addChildInfo: AddChildRequestModel, completion: (HTTP_RESPONSE_STATE, AddChildResponseModel?) -> Unit){
        val call = iRetrofit?.addChild(addChildInfo) ?: return

        call.enqueue(object : retrofit2.Callback<AddChildResponseModel>{
            override fun onResponse(call: Call<AddChildResponseModel>,
                                    response: Response<AddChildResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager - onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<AddChildResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 아이 프로필 목록 api 호출
    fun childProfileList(childProfileList: ChildProfileRequestModel, completion: (HTTP_RESPONSE_STATE, ChildProfileResponseModel?) -> Unit){
        val call = iRetrofit?.childProfileList(childProfileList) ?: return

        call.enqueue(object : retrofit2.Callback<ChildProfileResponseModel>{
            override fun onResponse(call: Call<ChildProfileResponseModel>,
                                    response: Response<ChildProfileResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager - onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<ChildProfileResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }
    // 검색 api 호출
    fun search(searchList: SearchRequestModel, completion: (HTTP_RESPONSE_STATE, SearchResponseModel?) -> Unit){
        val call = iRetrofit?.search(searchList) ?: return

        call.enqueue(object : retrofit2.Callback<SearchResponseModel>{
            override fun onResponse(call: Call<SearchResponseModel>,
                                    response: Response<SearchResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<SearchResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 댓글 작성 api 호출
    fun writeComment(comment: WriteCommentRequestModel, completion: (HTTP_RESPONSE_STATE, WriteCommentResponseModel?) -> Unit){
        val call = iRetrofit?.writeComment(comment) ?: return

        call.enqueue(object : retrofit2.Callback<WriteCommentResponseModel>{
            override fun onResponse(call: Call<WriteCommentResponseModel>,
                                    response: Response<WriteCommentResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<WriteCommentResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 동화 종아요 추가 api 호출
    fun addTaleLike(taleLike: TaleLikeRequestModel, completion: (HTTP_RESPONSE_STATE, TaleLikeResponseModel?) -> Unit){
        val call = iRetrofit?.addTaleLike(taleLike) ?: return

        call.enqueue(object : retrofit2.Callback<TaleLikeResponseModel>{
            override fun onResponse(call: Call<TaleLikeResponseModel>,
                                    response: Response<TaleLikeResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<TaleLikeResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

    // 동화 댓글 목록 api 호출
    fun commentList(commentList: CommentListRequestModel, completion: (HTTP_RESPONSE_STATE, CommentListResponseModel?) -> Unit){
        val call = iRetrofit?.commentList(commentList) ?: return

        call.enqueue(object : retrofit2.Callback<CommentListResponseModel>{
            override fun onResponse(call: Call<CommentListResponseModel>,
                                    response: Response<CommentListResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<CommentListResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }


    // 즐겨찾기 추가 api 호출
    fun favorites(favorites: FavoritesRequestModel, completion: (HTTP_RESPONSE_STATE, FavoritesResponseModel?) -> Unit){
        val call = iRetrofit?.favorites(favorites) ?: return

        call.enqueue(object : retrofit2.Callback<FavoritesResponseModel>{
            override fun onResponse(call: Call<FavoritesResponseModel>,
                                    response: Response<FavoritesResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<FavoritesResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 홈 화면 요청 api 호출
    fun homeScreen(homeScreenInfo: HomeScreenRequestModel, completion: (HTTP_RESPONSE_STATE, HomeScreenResponseModel?) -> Unit){
        val call = iRetrofit?.homeScreen(homeScreenInfo) ?: return

        call.enqueue(object : retrofit2.Callback<HomeScreenResponseModel>{
            override fun onResponse(call: Call<HomeScreenResponseModel>,
                                    response: Response<HomeScreenResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<HomeScreenResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

    // 평점 갱신 요청 api 호출
    fun rate(rateInfo: RateRequestModel, completion: (HTTP_RESPONSE_STATE, RateResponseModel?) -> Unit){
        val call = iRetrofit?.rate(rateInfo) ?: return

        call.enqueue(object : retrofit2.Callback<RateResponseModel>{
            override fun onResponse(call: Call<RateResponseModel>,
                                    response: Response<RateResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<RateResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

    // 상태 체크 api 호출
    fun stateCheck(stateCheckInfo: StateCheckRequestModel, completion: (HTTP_RESPONSE_STATE, StateCheckResponseModel?) -> Unit){
        val call = iRetrofit?.stateCheck(stateCheckInfo) ?: return

        call.enqueue(object : retrofit2.Callback<StateCheckResponseModel>{
            override fun onResponse(call: Call<StateCheckResponseModel>,
                                    response: Response<StateCheckResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called ")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<StateCheckResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }
    // 회원 탈퇴
    fun withDraw(id: String, completion: (HTTP_RESPONSE_STATE, Int?) -> Unit){
        val call = iRetrofit?.withDraw(id) ?: return

        call.enqueue(object : retrofit2.Callback<Void>{
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

    // 아이 삭제
    fun deleteChild(id: String, completion: (HTTP_RESPONSE_STATE, Int?) -> Unit){
        val call = iRetrofit?.deleteChild(id) ?: return

        call.enqueue(object : retrofit2.Callback<Void>{
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

    // 이미지 전송
    fun sendImage(image: MutableList<MultipartBody.Part?>, mp3: MutableList<MultipartBody.Part?>, userId : String, title : String, completion: (HTTP_RESPONSE_STATE, Int?) -> Unit){
        val call = iRetrofit?.sendImage(image,mp3, userId, title) ?: return

        call.enqueue(object : retrofit2.Callback<Void>{
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

    // 사용자 제작동화 리스트 api 호출
    fun myTaleList(myTaleListInfo: MyTaleListRequestModel, completion: (HTTP_RESPONSE_STATE, MyTaleListResponseModel?) -> Unit){
        val call = iRetrofit?.myTaleList(myTaleListInfo) ?: return

        call.enqueue(object : retrofit2.Callback<MyTaleListResponseModel>{
            override fun onResponse(call: Call<MyTaleListResponseModel>,
                                    response: Response<MyTaleListResponseModel>
            ) {
                Log.d(TAG, "RetrofitManager -onResponse() called")

                completion(HTTP_RESPONSE_STATE.OKAY, response.body())
            }

            override fun onFailure(call: Call<MyTaleListResponseModel>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(HTTP_RESPONSE_STATE.FAIL, null)
            }
        })
    }

}