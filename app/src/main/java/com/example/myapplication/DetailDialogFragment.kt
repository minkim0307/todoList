package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.FragmentDetailDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    interface OnItemEditedListener{
        fun onItemEdited(original : String, updated: String)
    }

    private var _binding: FragmentDetailDialogBinding? = null
    private val binding get() = _binding

    private var originalText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        originalText = arguments?.getString("text")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = FragmentDetailDialogBinding.inflate(LayoutInflater.from(context))
        dialogBinding.detailEditView.setText(originalText)
        dialogBinding.btnEdit.setOnClickListener {
            val updatedText = dialogBinding.detailEditView.text.toString()
            if(updatedText.isNotBlank() && updatedText != originalText){
                (activity as? OnItemEditedListener)?.onItemEdited(originalText!!, updatedText)
            }
            dismiss()
        }
        dialogBinding.btnOk.setOnClickListener { dismiss() }
        return AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object{
        fun newInstance(text: String): DetailDialogFragment{
            val fragment = DetailDialogFragment()
            val bundle = Bundle()
            bundle.putString("text", text)
            fragment.arguments = bundle
            return fragment
        }
    }



}