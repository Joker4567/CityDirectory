package com.anufriev.presentation.resultCall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anufriev.presentation.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_result_call.*
import org.jetbrains.anko.support.v4.toast


class ResultCallFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_result_call, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageViewPositive.setOnClickListener {
            toast("Спасибо, за отзыв. +1 к рейтингу")
            this.onDestroyView()
        }
        imageViewNegative.setOnClickListener {
            toast("Попробуйте позвонить в другую организацию. -1 к рейтингу")
            this.onDestroyView()
        }
    }
}