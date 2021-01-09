package com.anufriev.presentation.delegates

import com.anufriev.data.db.entities.FellowDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.presentation.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_fellow.view.*

fun itemFellowList(
    callClick: (String) -> Unit
) =
    adapterDelegateLayoutContainer<FellowDaoEntity, Any>(R.layout.item_fellow) {

        containerView.ivPhoneFellow.setOnClickListener { callClick.invoke(item.phone) }

        bind {
            containerView.tvTextPeople.text = item.description
            containerView.tvDatePeople.text = item.date
            containerView.tvPhoneFellow.text = item.phone
        }
    }