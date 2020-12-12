package com.anufriev.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemWorkList
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.ext.setData
import com.anufriev.utils.platform.BaseFragment
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment(R.layout.fragment_home) {
    override val toolbarTitle: String = "Организации"
    override val statusBarColor: Int = R.color.colorTransparent
    override val statusBarLightMode: Boolean = true
    override val setToolbar: Boolean = true
    override val setDisplayHomeAsUpEnabled: Boolean = false
    override val screenViewModel by viewModel<HomeViewModel>()
    private lateinit var router: HomeRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = HomeRouter(this)
        setupRecyclerView()
        bind()
    }

    private val worksListAdapter by lazy {
        ListDelegationAdapter(
            itemWorkList {
                router.routeToDetail(it)
            }
        )
    }

    private fun bind(){
        pullToRefresh_home.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        pullToRefresh_home.setColorSchemeColors(Color.WHITE)
        pullToRefresh_home.setOnRefreshListener {
            pullToRefresh_home.isRefreshing = false
        }
        observeLifeCycle(screenViewModel.works, ::handleWorks)
    }

    private fun setupRecyclerView() {
        with(rvWorks) {
            adapter = worksListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    private fun handleWorks(works: List<Object>?){
        works?.let {
            worksListAdapter.setData(works)
        }
    }
}