package com.example.sharedtalewithttsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.FragmentAllTaleBinding
import com.example.sharedtalewithttsapp.databinding.FragmentMyTaleBinding
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel2
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HOME_SCREEN_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.CreateTaleListAdapter
import com.example.sharedtalewithttsapp.viewholder.TaleListAdapter


class AllTaleFragment : Fragment() {
    private var _binding : FragmentAllTaleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "AllTaleFragment - onViewCreated() called");
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "AllTaleFragment - onStart() called");
        super.onStart()
        // 프래그먼트 시작시 통신, 홈 화면 정보 불러오기
        val homeScreenInfo2 = HomeScreenRequestModel2(userId = AppData.instance.getUserId(), childId = AppData.instance.getChildId())
        RetrofitManager.instance.homeScreen2(homeScreenInfo2, completion = {
                httpResponseState, homeScreen2Response ->

            when(httpResponseState){
                HTTP_RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "홈 화면 요청2 api 호출 성공 : ${homeScreen2Response}")

                    when(homeScreen2Response?.state){
                        HOME_SCREEN_RESPONSE_STATE.SUCCESS -> {
                            Log.d(Constants.TAG, "불러오기 성공")

                            // 여기서부터 조회수 높은 동화 출력
                            binding.mostViewTaleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            binding.mostViewTaleRecyclerView.adapter = TaleListAdapter(homeScreen2Response.mostViewTale, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(requireContext(), TaleProfileActivity::class.java)
                                for(i in homeScreen2Response.mostViewTale){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, requireContext())
                            binding.mostViewTaleRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))
                            // 조회수 높은 동화 출력 끝   //

                            // 여기서부터 최근 창작 동화 출력
                            binding.recentlyCreateTaleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            binding.recentlyCreateTaleRecyclerView.adapter = CreateTaleListAdapter(homeScreen2Response.recentCreateTale, clickListener = {
                                    createTaleInfo ->

                                val intent: Intent = Intent(requireContext(), CreateTaleInfoActivity::class.java)
                                intent.putExtra("createTaleInfo", createTaleInfo)
                                startActivity(intent)

                            }, requireContext())
                            binding.recentlyCreateTaleRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))
                            // 최근 창작 동화 출력 끝   //

                        }
                        HOME_SCREEN_RESPONSE_STATE.FAIL -> {
                            Log.d(Constants.TAG, "불러오기 실패");
                        }
                    }
                }
                HTTP_RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "홈 화면2 요청 api 실패 : ${homeScreen2Response}");
                }
            }
        })
        //통신코드 끝
    }

    override fun onStop() {
        Log.d(TAG, "AllTaleFragment - onStop() called");
        super.onStop()
    }
    override fun onDestroyView() {
        Log.d(TAG, "AllTaleFragment - onDestroyView() called");
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.d(TAG, "AllTaleFragment - onDestroy() called");
        super.onDestroy()
    }

}