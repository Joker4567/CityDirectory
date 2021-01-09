package com.anufriev.presentation.fellowAdd

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

interface FellowAddRouterLogic {
    fun routeToBack()
}

class FellowAddRouter(private val fragment: Fragment) : FellowAddRouterLogic {

    override fun routeToBack() {
        fragment
            .findNavController()
            .navigateUp()
    }
}