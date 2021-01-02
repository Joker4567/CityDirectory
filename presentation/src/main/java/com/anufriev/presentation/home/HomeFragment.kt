package com.anufriev.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.storage.Pref
import com.anufriev.presentation.R
import com.anufriev.presentation.delegates.itemOrgList
import com.anufriev.presentation.infoCompany.InfoCompanyFragment
import com.anufriev.presentation.resultCall.ResultCallFragment
import com.anufriev.utils.common.KCustomToast
import com.anufriev.utils.ext.gone
import com.anufriev.utils.ext.observeLifeCycle
import com.anufriev.utils.ext.setData
import com.anufriev.utils.ext.show
import com.anufriev.utils.platform.BaseFragment
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            }, {
                //Вывод описания в customToast
                InfoCompanyFragment(it.name, "Описание: ${it.description}").show(
                    supportFragmentManager,
                    "tag2"
                )
            }, {
                InfoCompanyFragment(
                    "Рейтинг",
                    "\"Рейтинг формируется исходя из доступности сервиса +1, если ваш заказ приняли, -1 если нет и оставленных отзывов службе. Положительный +5, отрицательный -5\""
                ).show(
                    supportFragmentManager,
                    "tag2"
                )
            })
        )
    }

    private fun bind() {
        toolbar.gone()
        pullToRefresh_home.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        pullToRefresh_home.setColorSchemeColors(Color.WHITE)
        pullToRefresh_home.setOnRefreshListener {
            getGeo(false)
            KCustomToast.infoToast(
                requireActivity(),
                "Информация обновлена",
                KCustomToast.GRAVITY_BOTTOM
            )
            pullToRefresh_home.isRefreshing = false
        }
        observeLifeCycle(screenViewModel.works, ::handleWorks)
        tvCityChange.setOnClickListener {
            //Запрос на смену города
            getGeo()
            if(getGPS())
                KCustomToast.infoToast(
                    requireActivity(),
                    "Отправлен запрос на смену города",
                    KCustomToast.GRAVITY_BOTTOM
                )
        }
        if (Pref(requireContext()).city == null) {
            getGeo()
        } else {
            getGeo(false)
        }
    }

    private fun setupRecyclerView() {
        with(rvOrg) {
            adapter = orgListAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ScaleInAnimator()
            setHasFixedSize(true)
        }
    }

    private fun handleWorks(works: List<OrganizationDaoEntity>?) {
        tvInfoCity.show()
        works?.let {
            if (it.isEmpty())
                tvCityChange.text = ""
            else
                tvInfoCity.gone()
            orgListAdapter.setData(works)
            if(Pref(requireContext()).city == null)
                tvCityChange.text = "Название города"
            else
                tvCityChange.text = Pref(requireContext()).city.toString()
        }
    }

    private fun callPhone(org: OrganizationDaoEntity) {
        val isCallPhonePermissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
        if (isCallPhonePermissionGranted) {
            idOrg = org.id
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:${org.phoneNumber}")
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

    private var idOrg: Int = 0
    private var callPhone = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CODE_PHONE) {
            callPhone = true
        }
    }

    override fun onStart() {
        super.onStart()
        if(callPhone){
            //вызываем bottomSheet для оценки вызова
            val fragment = ResultCallFragment.newInstance(idOrg)
            fragment.show(supportFragmentManager, "review")
            callPhone = false
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
                KCustomToast.infoToast(
                    requireActivity(),
                    "Нажмите [Заказать]",
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
        if (requestCode == CODE_MAP) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                getGeo()
            } else {
                val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (needRationale) {
                    toast("Для получения текущего местоположения, требуется разрешение!")
                }
            }
        }
    }

    private fun getGeo(change: Boolean = true) {
        if (change) {
            val isLocationPermissionGranted = ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (isLocationPermissionGranted && getGPS()) {
                CoroutineScope(Dispatchers.IO).launch {
                    LocationServices.getFusedLocationProviderClient(requireContext())
                        .getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER, null)
                        .addOnSuccessListener {
                            screenViewModel.getOrg(it.latitude, it.longitude, requireContext())
                            KCustomToast.infoToast(
                                requireActivity(),
                                "Координаты получены, город установлен",
                                KCustomToast.GRAVITY_BOTTOM
                            )
                        }
                        .addOnCanceledListener { toast("Запрос локации был отменен") }
                        .addOnFailureListener { toast("Запрос локации завершился неудачно") }
                }
            }
            else if(!getGPS()){
                KCustomToast.infoToast(
                    requireActivity(),
                    "Включите геолокацию, для определения города!",
                    KCustomToast.GRAVITY_CENTER
                )
            }
            else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    CODE_MAP
                )
            }
        } else {
            if(Pref(requireContext()).city == null && !getGPS()){
                KCustomToast.infoToast(
                    requireActivity(),
                    "Включите геолокацию, для определения города!",
                    KCustomToast.GRAVITY_CENTER
                )
                tvInfoCity.text = "Включите геолокацию, для определения города!"
            }
            else if(Pref(requireContext()).city == null)
            {
                KCustomToast.infoToast(
                    requireActivity(),
                    "Нажмите на 'Название города'",
                    KCustomToast.GRAVITY_CENTER
                )
            }
            else {
                tvInfoCity.text = "Ваш город будет добавлен в ближайщее время"
                screenViewModel.getOrg(Pref(requireContext()).city.toString())
            }
        }
    }

    private fun getGPS() : Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        private const val CODE_MAP = 1023;
        private const val RESULT_CODE_PHONE = 4213
        private const val PERMISSION_REQUEST_CODE_PHONE = 4313
    }
}