package com.anufriev.presentation.resultCall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anufriev.data.repository.OrganizationRepository
import com.anufriev.data.repository.OrganizationRepositoryImp
import com.anufriev.presentation.R
import com.anufriev.presentation.home.HomeViewModel
import com.anufriev.utils.common.KCustomToast
import com.anufriev.utils.platform.ErrorHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_result_call.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResultCallFragment(id: Int) : BottomSheetDialogFragment() {

    val screenViewModel by viewModel<ResultCallViewModel>()

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
            screenViewModel.setRating(true, id)
            KCustomToast.infoToast(
                requireActivity(),
                "Отлично! Рейтинг организации увеличен.",
                KCustomToast.GRAVITY_BOTTOM
            )
            this.onDestroyView()
            requireActivity().finish()
        }
        imageViewNegative.setOnClickListener {
            screenViewModel.setRating(false, id)
            KCustomToast.infoToast(
                requireActivity(),
                "Рейтинг организации занижен! Звоним в другую.",
                KCustomToast.GRAVITY_BOTTOM
            )
            this.onDestroyView()
        }
    }
}