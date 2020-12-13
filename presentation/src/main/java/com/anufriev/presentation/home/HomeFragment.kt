package com.anufriev.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.anufriev.data.db.entities.Organization
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemOrgList
import com.anufriev.utils.ext.gone
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.ext.setData
import com.anufriev.utils.platform.BaseFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment(R.layout.fragment_home) {
    override var toolbarTitle: String = "Организации"
    override val statusBarColor: Int = R.color.colorTransparent
    override val statusBarLightMode: Boolean = true
    override val setToolbar: Boolean = false
    override val setDisplayHomeAsUpEnabled: Boolean = false
    override val screenViewModel by viewModel<HomeViewModel>()
    private lateinit var router: HomeRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = HomeRouter(this)
        setupRecyclerView()
        bind()
    }

    private val orgListAdapter by lazy {
        ListDelegationAdapter(
            itemOrgList ({
                //Вызываем организацию по текущему телефону
            },{
                //Открываем новый фрагмент для просмотра отзывов
                router.routeToDetail(it)
            })
        )
    }

    private fun bind(){
        toolbar.gone()
        pullToRefresh_home.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        pullToRefresh_home.setColorSchemeColors(Color.WHITE)
        pullToRefresh_home.setOnRefreshListener {
            toast("Информация обновлена")
            pullToRefresh_home.isRefreshing = false
        }
        observeLifeCycle(screenViewModel.works, ::handleWorks)
    }

    private fun setupRecyclerView() {
        with(rvOrg) {
            adapter = orgListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    private fun handleWorks(works: List<Organization>?){
        works?.let {
            orgListAdapter.setData(works)
        }
    }
}