package com.anufriev.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.anufriev.data.db.entities.Organization
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemOrgList
import com.anufriev.presentation.resultCall.ResultCallFragment
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
            itemOrgList({
                //Вызываем организацию по текущему телефону
                callPhone(it)
            }, {
                //Открываем новый фрагмент для просмотра отзывов
                router.routeToDetail(it)
            })
        )
    }

    private fun bind(){
        toolbar.gone()
        pullToRefresh_home.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
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

    private fun callPhone(phone: String){
        val isCallPhonePermissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
        if (isCallPhonePermissionGranted) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phone")
            startActivityForResult(intent, RESULT_CODE_PHONE)
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CALL_PHONE
                ),
                PERMISSION_REQUEST_CODE_PHONE
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RESULT_CODE_PHONE){
            //вызываем bottomSheet для оценки вызова
            ResultCallFragment().show(supportFragmentManager, "tag")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE_PHONE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                toast("Нажмите [Заказать]")
            } else {
                val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )
                if (needRationale) {
                    toast("Для осуществления звонка, необходимо разрешение")
                }
            }
        }
    }

    companion object {
        private const val RESULT_CODE_PHONE = 4213
        private const val PERMISSION_REQUEST_CODE_PHONE = 4313
    }
}