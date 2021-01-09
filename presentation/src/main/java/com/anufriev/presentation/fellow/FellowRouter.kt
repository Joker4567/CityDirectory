package com.anufriev.presentation.fellow

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


interface FellowRouterLogic {
    fun routeToAddFellow()
}

class FellowRouter(private val fragment: Fragment) : FellowRouterLogic {

    override fun routeToAddFellow() {
        val action =
            FellowFragmentDirections.actionFellowFragmentToFellowAddFragment()
        fragment
            .findNavController()
            .navigate(action)
    }
}