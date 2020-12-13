package com.anufriev.presentation.feedBack

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

interface FeedBackRouterLogic {
    fun backOrg()
}

class FeedBackRouter(private val fragment: Fragment) : FeedBackRouterLogic {

    override fun backOrg() {
        fragment
            .findNavController()
            .navigateUp()
    }
}