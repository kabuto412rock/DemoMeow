package com.blogspot.zongjia.demomeow.presentation.favorites.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.blogspot.zongjia.demomeow.R
import com.blogspot.zongjia.demomeow.presentation.favorites.FavoriteCatsViewModel
import kotlinx.android.synthetic.main.fragment_dialog_clear_confirm.*
import org.koin.android.viewmodel.ext.android.getViewModel

/**
 * A simple [Fragment] subclass.
 */
class DialogClearConfirmFragment : DialogFragment() {

    private val viewModel by lazy {
        requireParentFragment().getViewModel<FavoriteCatsViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_clear_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 點擊 清空的按鈕，呼叫viewModel中清除貓咪資料表內容的方法
        confirmClearCatButton.setOnClickListener{
            viewModel.clearCats()
            dismiss()
        }
        // 點擊 取消清空的按鈕，自動關閉當前的Dialog Fragment
        cancelClearCatButton.setOnClickListener{
            dismiss()
        }

    }

}
