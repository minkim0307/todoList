package com.example.myapplication

import android.graphics.Paint
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRecyclerViewBinding

class MyViewHolder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: Pair<String, Boolean>,
             onDeleteClick: (Pair<String, Boolean>) -> Unit,
             onItemClick: (Pair<String, Boolean>) -> Unit,
             onCheckChanged: (Boolean) -> Unit
    )

    {
        binding.itemData.text = item.first
        binding.itemCheckBox.setOnCheckedChangeListener(null)  // 리스너 초기화
        binding.itemCheckBox.isChecked = item.second

        // 체크박스 상태에 따라 취소선 그리기
        binding.itemData.paintFlags = if (item.second){
            binding.itemData.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            binding.itemData.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        // 삭제 버튼 클릭 시
        binding.mainDelete.setOnClickListener {
            onDeleteClick(item)
        }
        // 아이템 클릭 시
        binding.root.setOnClickListener {
            onItemClick(item)
        }
        // 체크박스 상태 변경 시 onCkeckedChanged 호출
        binding.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.itemData.paintFlags = if (isChecked) {
                binding.itemData.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.itemData.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            onCheckChanged(isChecked)
        }
    }
}