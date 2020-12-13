package com.anufriev.presentation.feedBack

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.anufriev.data.db.entities.FeedBack
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemFeedBackList
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.ext.setData
import com.anufriev.utils.platform.BaseFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_feedback.toolbar
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FeedBackFragment : BaseFragment(R.layout.fragment_feedback) {
    override var toolbarTitle: String = "Отзывы"
    override val statusBarColor: Int = R.color.colorTransparent
    override val statusBarLightMode: Boolean = true
    override val setToolbar: Boolean = true
    override val setDisplayHomeAsUpEnabled: Boolean = true
    override val screenViewModel by viewModel<FeedBackViewModel>{
        parametersOf(args.org.feedback)
    }
    private lateinit var router: FeedBackRouter
    private val args: FeedBackFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = FeedBackRouter(this)
        bind()
        setupRecyclerView()
    }

    private fun bind(){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            router.backOrg()
        }
        btAddFeedBack.setOnClickListener {
            toast("Отзыв успешно добавлен")
        }
        observeLifeCycle(screenViewModel.feedBacks, ::handleWorks)
    }

    private fun handleWorks(list: List<FeedBack>?){
        list?.let {
            feedBackListAdapter.setData(list)
        }
    }

    private val feedBackListAdapter by lazy {
        ListDelegationAdapter(
            itemFeedBackList()
        )
    }

    private fun setupRecyclerView() {
        with(rvFeedBack) {
            adapter = feedBackListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }
}