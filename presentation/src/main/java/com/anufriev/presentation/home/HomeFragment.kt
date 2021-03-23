package com.anufriev.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.anufriev.utils.ext.*
import com.anufriev.utils.platform.BaseFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
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
    private var isStart = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = HomeRouter(this)
        setupRecyclerView()
        bind()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        removeContact()
    }

    private val orgListAdapter by lazy {
        ListDelegationAdapter(
            itemOrgList({
                //Переходим на страницу вызова телефона
                router.routeToPhone(it)
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
            }, ::shareApp)
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
            if (getGPS(requireContext()))
                KCustomToast.infoToast(
                    requireActivity(),
                    "Отправлен запрос на смену города",
                    KCustomToast.GRAVITY_BOTTOM
                )
        }
        if (!isStart) {
            if (Pref(requireContext()).city == null) {
                getGeo()
            } else {
                getGeo(false)
            }
        }
        isStart = true
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
            removeContact()
            if (Pref(requireContext()).city == null)
                tvCityChange.text = "Название города"
            else
                tvCityChange.text = Pref(requireContext()).city.toString()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
            if (isLocationPermissionGranted && getGPS(requireContext())) {
                CoroutineScope(Dispatchers.IO).launch {
                    LocationServices.getFusedLocationProviderClient(requireContext())
                        .getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER, null)
                        .addOnSuccessListener {
                            it?.let {
                                screenViewModel.getOrg(
                                    it.latitude, it.longitude, requireContext()
                                ) { city ->
                                    KCustomToast.infoToast(
                                        requireActivity(),
                                        "Город: $city установлен",
                                        KCustomToast.GRAVITY_BOTTOM
                                    )
                                }
                            }
                        }
                        .addOnCanceledListener { toast("Запрос локации был отменен") }
                        .addOnFailureListener { toast("Запрос локации завершился неудачно") }
                }
            } else if (!getGPS(requireContext())) {
                KCustomToast.infoToast(
                    requireActivity(),
                    "Включите геолокацию, для определения города!",
                    KCustomToast.GRAVITY_CENTER
                )
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    CODE_MAP
                )
            }
        } else {
            if (Pref(requireContext()).city == null && !getGPS(requireContext())) {
                KCustomToast.infoToast(
                    requireActivity(),
                    "Включите геолокацию, для определения города!",
                    KCustomToast.GRAVITY_CENTER
                )
                tvInfoCity.text = "Включите геолокацию, для определения города!"
            } else if (Pref(requireContext()).city == null) {
                KCustomToast.infoToast(
                    requireActivity(),
                    "Нажмите на 'Название города'",
                    KCustomToast.GRAVITY_CENTER
                )
            } else {
                tvInfoCity.text = "Ваш город будет добавлен в ближайщее время"
                screenViewModel.getOrg(Pref(requireContext()).city.toString())
                removeContact()
            }
        }
    }

    private fun shareApp(org: OrganizationDaoEntity) {
        val text =
            "${org.name} ${Pref(requireContext()).city} уже в Едином городском справочнике такси. \nПриложение в Google Play: https://play.google.com/store/apps/details?id=com.anufriev.city"
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun removeContact() {
        val isContactPermissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        if (isContactPermissionGranted) {
            screenViewModel.removeContact()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ),
                PERMISSION_REQUEST_CONTACT
            )
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext())
        return if (status != ConnectionResult.SUCCESS) {
            Log.e("", "Error")
            false
        } else {
            Log.i("", "Google play services updated")
            true
        }
    }


    companion object {
        private const val CODE_MAP = 1023;
        private const val PERMISSION_REQUEST_CONTACT = 1233;
    }
}