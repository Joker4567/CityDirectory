package com.anufriev.presentation.resultCall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anufriev.data.db.contract.FeedBackContract.Column.idOrg
import com.anufriev.presentation.R
import com.anufriev.utils.common.KCustomToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_result_call.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResultCallFragment() : BottomSheetDialogFragment() {

    val screenViewModel by viewModel<ResultCallViewModel>()
    var idOrg = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && requireArguments().containsKey("idOrg")) {
            idOrg = requireArguments().getInt("idOrg")
        }
    }

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
            screenViewModel.setRating(true, idOrg)
            KCustomToast.infoToast(
                requireActivity(),
                "Отлично! Рейтинг организации увеличен.",
                KCustomToast.GRAVITY_BOTTOM
            )
            this.onDestroyView()
            requireActivity().finish()
        }
        imageViewNegative.setOnClickListener {
            screenViewModel.setRating(false, idOrg)
            KCustomToast.infoToast(
                requireActivity(),
                "Рейтинг организации занижен! Звоним в другую.",
                KCustomToast.GRAVITY_BOTTOM
            )
            this.onDestroyView()
        }
    }
}