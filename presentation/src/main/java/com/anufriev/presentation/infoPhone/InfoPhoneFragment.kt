package com.anufriev.presentation.infoPhone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemFeedBackList
import com.anufriev.presentation.delegates.itemPhoneList
import com.anufriev.presentation.home.HomeFragment
import com.anufriev.presentation.resultCall.ResultCallFragment
import com.anufriev.utils.common.KCustomToast
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.ext.setData
import com.anufriev.utils.platform.BaseFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_feedback.rvFeedBack
import kotlinx.android.synthetic.main.fragment_feedback.toolbar
import kotlinx.android.synthetic.main.fragment_info_phone.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.Exception

class InfoPhoneFragment : BaseFragment(R.layout.fragment_info_phone) {
    override var toolbarTitle: String = "Контакты"
    override val statusBarColor: Int = R.color.colorTransparent
    override val statusBarLightMode: Boolean = true
    override val setToolbar: Boolean = true
    override val setDisplayHomeAsUpEnabled: Boolean = true
    override val screenViewModel by viewModel<InfoPhoneViewModel>{
        parametersOf(args.org)
    }
    private lateinit var router: InfoPhoneRouter
    private val args: InfoPhoneFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = InfoPhoneRouter(this)
        bind()
        setupRecyclerView()
    }

    private fun bind(){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            router.backOrg()
        }
        observeLifeCycle(screenViewModel.phoneList, ::handlePhone)
        infoTxtWeb.text = "Сайт: ${args.org.web}"
        infoTxtWeb.setOnClickListener {
            try {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(args.org.web))
                startActivity(i)
            } catch (ex:Exception){
                KCustomToast.infoToast(
                    requireActivity(),
                    "Ошибка открытия ссылки",
                    KCustomToast.GRAVITY_BOTTOM
                )
            }
        }
    }

    private fun handlePhone(list: List<String>?){
        list?.let {
            phoneListAdapter.setData(list)
        }
    }

    private val phoneListAdapter by lazy {
        ListDelegationAdapter(
            itemPhoneList {
                //Осуществляем вызов по телефону
                callPhone(it)
            }
        )
    }

    private fun setupRecyclerView() {
        with(rvPhone) {
            adapter = phoneListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    private fun callPhone(phone: String) {
        val isCallPhonePermissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
        if (isCallPhonePermissionGranted) {
            idOrg = args.org.id
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phone")
            startActivityForResult(intent, RESULT_CODE_PHONE)
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CALL_PHONE
//                    Manifest.permission.READ_CALL_LOG,
//                    Manifest.permission.WRITE_CALL_LOG
                ),
                PERMISSION_REQUEST_CODE_PHONE
            )
        }
    }

    private var idOrg: Int = 0
    private var callPhone = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CODE_PHONE) {
            callPhone = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE_PHONE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                KCustomToast.infoToast(
                    requireActivity(),
                    "Нажмите на интересующий номер",
                    KCustomToast.GRAVITY_BOTTOM
                )
            } else {
                val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )
                if (needRationale) {
                    KCustomToast.infoToast(
                        requireActivity(),
                        "Для осуществления звонка, необходимо разрешение",
                        KCustomToast.GRAVITY_BOTTOM
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (callPhone) {
            //TODO Активировать после релиза
            //screenViewModel.removeCallLog(idOrg)
            //вызываем bottomSheet для оценки вызова
            val fragment = ResultCallFragment.newInstance(idOrg)
            fragment.show(supportFragmentManager, "review")
            callPhone = false

        }
    }

    companion object {
        private const val RESULT_CODE_PHONE = 4213
        private const val PERMISSION_REQUEST_CODE_PHONE = 4313
    }
}