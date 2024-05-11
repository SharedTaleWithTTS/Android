package com.example.sharedtalewithttsapp.viewholder

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtalewithttsapp.utils.Constants.TAG

// 리사이클러뷰를 파라미터로 받았다.
class ItemTouchCallback(private val recyclerView: RecyclerView) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){

    // 아이템을 클릭해서 이동할 때
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,  // 이동하려는 아이템의 현재 위치
        target: RecyclerView.ViewHolder   // 이동하려는 곳
    ): Boolean {
        Log.d(TAG, "ItemTouchCallback - onMove() called");
        (recyclerView.adapter as CreateTaleAdapter).moveItem(viewHolder.layoutPosition, target.layoutPosition)
        return true
    }
    // 아이템을 스와이프 할떄
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d(TAG, "ItemTouchCallback - onSwiped() called");
        (recyclerView.adapter as CreateTaleAdapter).removeItem(viewHolder.layoutPosition)
    }
    // 아이템을 선택하면 호출 -> 예제에서는 선택시 아이템의 배경색을 변경
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        Log.d(TAG, "ItemTouchCallback - onSelectedChanged() called");
        super.onSelectedChanged(viewHolder, actionState)
    }

    // 선택 해제시 호출 -> 예제에서는 선택을 해제시 배경색을 복구
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        Log.d(TAG, "ItemTouchCallback - clearView() called");
        super.clearView(recyclerView, viewHolder)
    }

}