package com.anufriev.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.anufriev.utils.ext.setupBottomWithNavController
import com.anufriev.utils.platform.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(R.layout.activity_main) {

    private var currentNavController: LiveData<NavController>? = null

    override fun initInterface(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()

    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.home
        )

        val controller = bottom_nav.setupBottomWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
