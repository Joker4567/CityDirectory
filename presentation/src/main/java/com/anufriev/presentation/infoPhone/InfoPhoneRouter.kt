package com.anufriev.presentation.infoPhone

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

interface InfoPhoneRouterLogic {
    fun backOrg()
}

class InfoPhoneRouter(private val fragment: Fragment) : InfoPhoneRouterLogic {

    override fun backOrg() {
        fragment
            .findNavController()
            .navigateUp()
    }
}
