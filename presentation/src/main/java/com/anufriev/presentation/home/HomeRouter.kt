package com.anufriev.presentation.home

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anufriev.data.db.entities.Organization

interface HomeRouterLogic {
    fun routeToDetail(work: Organization)
}

class HomeRouter(private val fragment: Fragment) : HomeRouterLogic {

    override fun routeToDetail(work: Organization) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToFeedBackFragment(work)
        fragment
            .findNavController()
            .navigate(action)
    }
}