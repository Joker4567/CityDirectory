package com.anufriev.presentation.home

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anufriev.data.db.entities.OrganizationDaoEntity

interface HomeRouterLogic {
    fun routeToDetail(work: OrganizationDaoEntity)
}

class HomeRouter(private val fragment: Fragment) : HomeRouterLogic {

    override fun routeToDetail(work: OrganizationDaoEntity) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToFeedBackFragment(work)
        fragment
            .findNavController()
            .navigate(action)
    }
}