package com.anufriev.presentation.home

import androidx.fragment.app.Fragment

interface HomeRouterLogic {
    fun routeToDetail(work: Object)
}

class HomeRouter(private val fragment: Fragment) : HomeRouterLogic {

    override fun routeToDetail(work: Object) {
//        val action =
//            HomeFragmentDirections.actionHomeFragmentToDetailWorkFragment(work)
//        fragment
//            .findNavController()
//            .navigate(action)
    }
}