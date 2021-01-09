package com.anufriev.presentation.fellowAdd

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anufriev.data.storage.Pref
import com.anufriev.presentation.R
import com.anufriev.utils.common.KCustomToast
import com.anufriev.utils.ext.isPhoneValid
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_fellow.*
import kotlinx.android.synthetic.main.fragment_feedback.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FellowAddFragment : BaseFragment(R.layout.fragment_add_fellow) {
    override var toolbarTitle: String = "Добавить запись"
    override val statusBarColor: Int = R.color.colorTransparent
    override val statusBarLightMode: Boolean = true
    override val setToolbar: Boolean = true
    override val setDisplayHomeAsUpEnabled: Boolean = true
    override val screenViewModel by viewModel<FellowAddViewModel>()
    private lateinit var router: FellowAddRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = FellowAddRouter(this)
        bind()
    }

    private fun bind() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            router.routeToBack()
        }
        btAddFellow.setOnClickListener {
            if(etMessagePeople.text.isNotEmpty() && etPhoneFellow.text.isNotEmpty()
                && etPhoneFellow.text.toString().isPhoneValid()) {
                screenViewModel.addRecord(
                    etMessagePeople.text.toString().trim(),
                    Pref(requireContext()).city.toString(),
                    etPhoneFellow.text.toString().trim()
                )
            } else {
                KCustomToast.infoToast(
                    requireActivity(),
                    "Не все поля были заполнены",
                    KCustomToast.GRAVITY_BOTTOM
                )
            }
        }
        observeLifeCycle(screenViewModel.success, {
            KCustomToast.infoToast(
                requireActivity(),
                it!!,
                KCustomToast.GRAVITY_BOTTOM
            )
            router.routeToBack()
        })
    }
}