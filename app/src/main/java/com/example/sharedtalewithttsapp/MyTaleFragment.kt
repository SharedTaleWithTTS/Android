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
import com.example.sharedtalewithttsapp.databinding.FragmentMyTaleBinding
import com.example.sharedtalewithttsapp.model.TaleModel
import com.example.sharedtalewithttsapp.model.httpmodel.HomeScreenRequestModel
import com.example.sharedtalewithttsapp.retrofit.RetrofitManager
import com.example.sharedtalewithttsapp.utils.Constants
import com.example.sharedtalewithttsapp.utils.Constants.TAG
import com.example.sharedtalewithttsapp.utils.HOME_SCREEN_RESPONSE_STATE
import com.example.sharedtalewithttsapp.utils.HTTP_RESPONSE_STATE
import com.example.sharedtalewithttsapp.viewholder.TaleListAdapter

class MyTaleFragment : Fragment() {
    private var _binding : FragmentMyTaleBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyTaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "MyTaleFragment - onViewCreated() called");
    }
    override fun onStart() {
        Log.d(TAG, "MyTaleFragment - onStart() called");
        super.onStart()
        // 프래그먼트 시작시 통신, 홈 화면 정보 불러오기
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
                            binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            binding.favoriteRecyclerView.adapter = TaleListAdapter(homeScreenResponse.favorites, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(requireContext(), TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.favorites){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, requireContext())
                            binding.favoriteRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))

                            // 즐겨찾기 한 동화 출력 끝   //

                            // 여기서부터 최근 본 동화 출력
                            binding.recentlyViewRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            binding.recentlyViewRecyclerView.adapter = TaleListAdapter(homeScreenResponse.recentlyView, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(requireContext(), TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.recentlyView){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, requireContext())
                            binding.recentlyViewRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))

                            // 최근 본 동화 출력 끝   //

                            // 여기서부터 추천 동화 출력
                            binding.recommendRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            binding.recommendRecyclerView.adapter = TaleListAdapter(homeScreenResponse.recommend, clickListener = {
                                    taleId ->
                                var taleModel : TaleModel

                                val intent: Intent = Intent(requireContext(), TaleProfileActivity::class.java)
                                for(i in homeScreenResponse.recommend){
                                    if(taleId == i.id) {
                                        taleModel = i
                                        intent.putExtra("taleInfo", taleModel)
                                    }
                                }
                                startActivity(intent)
                            }, requireContext())
                            binding.recommendRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))

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
    }
    override fun onStop() {
        Log.d(TAG, "MyTaleFragment - onStop() called");
        super.onStop()
    }
    override fun onDestroyView() {
        Log.d(TAG, "MyTaleFragment - onDestroyView() called");
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.d(TAG, "MyTaleFragment - onDestroy() called");
        super.onDestroy()
    }

}