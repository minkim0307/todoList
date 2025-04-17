package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRecyclerViewBinding

class MyAdapter(
    val datas: MutableList<Pair<String, Boolean>>,
    val onDeleteClick: (Pair<String, Boolean>) -> Unit,
    val onItemClick: (Pair<String, Boolean>) -> Unit,
    val onCheckChanged: (Boolean) -> Unit // 체크 상태 변경 콜백
) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("MyAdapter", "Binding data: ${datas[position].first} || ${datas[position].second}")

        holder.bind(
            datas[position],
            onDeleteClick,
            onItemClick,
            onCheckChanged = { isChecked ->
                // 체크 상태 변경 시 데이터 업데이트
                datas[position] = Pair(datas[position].first, isChecked)
                // 아이템 상태 변경 후 UI 갱신
                notifyItemChanged(position)
                // 체크 상태가 변경되면 onCheckChanged 콜백 호출
                onCheckChanged(isChecked)
            })
    }

}
