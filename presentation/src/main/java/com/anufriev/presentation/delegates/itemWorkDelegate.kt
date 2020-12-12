package com.anufriev.presentation.delegates

import com.anufriev.presentation.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer

fun itemWorkList(clickListener: (Object) -> Unit) =
    adapterDelegateLayoutContainer<Object, Any>(R.layout.item_work_list) {

        containerView.setOnClickListener { clickListener.invoke(item) }

        bind {
            //containerView.tvObj.text = item.nameObj
        }
    }

