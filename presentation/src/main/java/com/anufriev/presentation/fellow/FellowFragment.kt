package com.anufriev.presentation.fellow

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.anufriev.data.db.entities.FellowDaoEntity
import com.anufriev.data.storage.Pref
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemFellowList
import com.anufriev.utils.common.KCustomToast
import com.anufriev.utils.ext.gone
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.ext.setData
import com.anufriev.utils.platform.BaseFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_fellow.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FellowFragment : BaseFragment(R.layout.fragment_fellow) {
    override var toolbarTitle: String = "Попутчики"
    override val statusBarColor: Int = R.color.colorTransparent
    override val statusBarLightMode: Boolean = true
    override val setToolbar: Boolean = false
    override val setDisplayHomeAsUpEnabled: Boolean = false
    override val screenViewModel by viewModel<FellowViewModel>()
    private lateinit var router: FellowRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = FellowRouter(this)
        setupRecyclerView()
        bind()
    }

    private val fellowListAdapter by lazy {
        ListDelegationAdapter(
            itemFellowList()
        )
    }

    private fun bind() {
        toolbar.gone()
        pullToRefresh_fellow.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        pullToRefresh_fellow.setColorSchemeColors(Color.WHITE)
        pullToRefresh_fellow.setOnRefreshListener {
            screenViewModel.getFellowList(Pref(requireContext()).city.toString())
            //Запрос попутчиков список
            KCustomToast.infoToast(
                requireActivity(),
                "Информация обновлена",
                KCustomToast.GRAVITY_BOTTOM
            )
            pullToRefresh_fellow.isRefreshing = false
        }
        floatActionBtAddRecord.setOnClickListener {
            router.routeToAddFellow()
        }
        observeLifeCycle(screenViewModel.fellowList, ::handleFellowList)
    }

    override fun onStart() {
        super.onStart()
        screenViewModel.getFellowList(Pref(requireContext()).city.toString())
    }

    private fun setupRecyclerView() {
        with(rvFellow) {
            adapter = fellowListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    private fun handleFellowList(fellows: List<FellowDaoEntity>?) {
        fellows?.let {
            fellowListAdapter.setData(fellows)
        }
    }
}