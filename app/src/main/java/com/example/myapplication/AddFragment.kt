package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddBinding

class AddFragment : Fragment(R.layout.fragment_add) {
  interface OnAddItemListener {
      fun onItemAdd(item : String)
  }
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        // '저장' 버튼 클릭 시
        binding.btnSave.setOnClickListener {
            val text = binding.addEditView.text.toString()
            if (text.isNotBlank()) {
                (activity as? OnAddItemListener)?.onItemAdd(text) // Activity에 전달
                requireActivity().supportFragmentManager.popBackStack() // 프래그먼트 뒤로가기
            } else {
                Toast.makeText(context, "내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // '취소' 버튼 클릭 시
        binding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack() // 프래그먼트 뒤로가기
        }

        return binding.root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}