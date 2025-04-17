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
    val onCheckChanged: () -> Unit // <-- 체크 변경 콜백 추가
) :
    RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bind(
            datas[position],
            onDeleteClick,
            onItemClick,
            onCheckChanged = { ischecked ->
                datas[position] = Pair(datas[position].first, ischecked)
                notifyItemChanged(position)
                onCheckChanged ()
            })
    }

}
