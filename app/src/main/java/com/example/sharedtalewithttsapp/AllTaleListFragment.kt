package com.example.sharedtalewithttsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedtalewithttsapp.appdata.AppData
import com.example.sharedtalewithttsapp.databinding.FragmentAllTaleBinding
import com.example.sharedtalewithttsapp.databinding.FragmentAllTaleListBinding
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel2
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel3
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HOME_SCREEN_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.CreateTaleListAdapter
import com.example.sharedtalewithttsapp.viewholder.TaleListAdapter


class AllTaleListFragment : Fragment() {
    private var _binding : FragmentAllTaleListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTaleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        Log.d(TAG, "AllTaleListFragment - onStart() called");
        super.onStart()
        // 프래그먼트 시작시 통신, 홈 화면 정보 불러오기
        val homeScreenInfo3 = HomeScreenRequestModel3(userId = AppData.instance.getUserId(), childId = AppData.instance.getChildId())
        RetrofitManager.instance.homeScreen3(homeScreenInfo3, completion = {
                httpResponseState, homeScreen3Response ->

            when(httpResponseState){
                HTTP_RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "홈 화면 요청3 api 호출 성공 : ${homeScreen3Response}")

                    when(homeScreen3Response?.state){
                        HOME_SCREEN_RESPONSE_STATE.SUCCESS -> {
                            Log.d(Constants.TAG, "불러오기 성공")

                            // 여기서부터 전체 동화 출력
                            binding.allTaleRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
                            binding.allTaleRecyclerView.adapter = TaleListAdapter(homeScreen3Response.allTaleList, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(requireContext(), TaleProfileActivity::class.java)
                                for(i in homeScreen3Response.allTaleList){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, requireContext())
                            binding.allTaleRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))
                            // 전체 동화 출력 끝   //
                        }
                        HOME_SCREEN_RESPONSE_STATE.FAIL -> {
                            Log.d(Constants.TAG, "불러오기 실패");
                        }
                    }
                }
                HTTP_RESPONSE_STATE.FAIL -> {
                    Log.d(Constants.TAG, "홈 화면3 요청 api 실패 : ${homeScreen3Response}");
                }
            }
        })
        //통신코드 끝
    }

    override fun onDestroyView() {
        Log.d(Constants.TAG, "AllTaleFragment - onDestroyView() called");
        super.onDestroyView()
        _binding = null
    }

}