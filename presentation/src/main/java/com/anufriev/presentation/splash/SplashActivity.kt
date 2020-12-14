package com.anufriev.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.anufriev.presentation.MainActivity
import com.anufriev.presentation.R
import com.anufriev.utils.ext.gone
import com.anufriev.utils.ext.observe
import com.anufriev.utils.ext.observeEvent
import com.anufriev.utils.ext.show
import com.anufriev.utils.platform.BaseActivity
import com.anufriev.utils.platform.State
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity(R.layout.activity_splash) {

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override val statusBarLightMode: Boolean
        get() = false

    override fun initInterface(savedInstanceState: Bundle?) {
        startApp()
    }

    private fun startApp() {
        val newIntent = Intent(this, MainActivity::class.java)
        startActivity(newIntent)
        finish()
    }
}