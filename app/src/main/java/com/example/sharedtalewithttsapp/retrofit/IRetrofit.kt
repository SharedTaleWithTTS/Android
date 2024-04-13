package com.example.sharedtalewithttsapp.retrofit


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
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface IRetrofit {

    @POST(API.ID_CHECK)
    fun idCheck(
        @Body id : IdCheckModel
    ): Call<IdCheckState>

    @POST(API.NICKNAME_CHECK)
    fun nicknameCheck(
        @Body nickname : NicknameCheckModel
    ): Call<NicknameCheckState>

    @POST(API.MEMBERJOIN)
    fun memberJoin(
        @Body member: MemberJoinModel
    ): Call<MemberJoinState>

    @POST(API.LOGIN)
    fun memberLogin(
        @Body loginInfo : LoginRequestModel
    ): Call<LoginResponseModel>

    @POST(API.READING_TALE)
    fun readingTale(
        @Body readingTale : TestReadingTaleRequestModel
    ): Call<TestReadingTaleResponseModel>

    @POST(API.ADD_CHILD)
    fun addChild(
        @Body addChildInfo : AddChildRequestModel
    ): Call<AddChildResponseModel>

    @POST(API.CHILD_PROFILE_REQUEST)
    fun childProfileList(
        @Body childProfileList : ChildProfileRequestModel
    ): Call<ChildProfileResponseModel>

    @POST(API.SEARCH)
    fun search(
        @Body searchList : SearchRequestModel
    ): Call<SearchResponseModel>

    @POST(API.WRITE_COMMENT)
    fun writeComment(
        @Body comment : WriteCommentRequestModel
    ): Call<WriteCommentResponseModel>

    @POST(API.ADD_TALE_LIKE)
    fun addTaleLike(
        @Body taleLike : TaleLikeRequestModel
    ): Call<TaleLikeResponseModel>

    @POST(API.COMMENT_LIST_REQUEST)
    fun commentList(
        @Body commentList : CommentListRequestModel
    ): Call<CommentListResponseModel>

    @POST(API.FAVORITES_REQUEST)
    fun favorites(
        @Body favorites : FavoritesRequestModel
    ): Call<FavoritesResponseModel>

    @POST(API.HOME_SCREEN_REQUEST)
    fun homeScreen(
        @Body homeScreenInfo : HomeScreenRequestModel
    ): Call<HomeScreenResponseModel>

    @POST(API.RATE_REQUEST)
    fun rate(
        @Body rateInfo : RateRequestModel
    ): Call<RateResponseModel>

    @POST(API.STATE_CHECK)
    fun stateCheck(
        @Body stateCheckInfo : StateCheckRequestModel
    ): Call<StateCheckResponseModel>

    @DELETE(API.WITHDRAW_MEMBER)
    fun withDraw(
        @Path("id") userId : String
    ): Call<Void>

    @DELETE(API.DELETE_CHILD)
    fun deleteChild(
        @Path("id") childId : String
    ): Call<Void>



}