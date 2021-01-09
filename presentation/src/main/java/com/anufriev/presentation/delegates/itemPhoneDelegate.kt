package com.anufriev.presentation.delegates

import com.anufriev.presentation.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_phone_list.view.*

fun itemPhoneList(
    callClick: (String) -> Unit
) =
    adapterDelegateLayoutContainer<String, Any>(R.layout.item_phone_list) {

        containerView.imageViewPhone.setOnClickListener { callClick.invoke(item) }
        bind {
            containerView.tvPhoneItem.text = item
        }
    }