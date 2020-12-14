package com.anufriev.presentation.delegates

import com.anufriev.data.db.entities.Organization
import com.anufriev.presentation.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_org_list.view.*

fun itemOrgList(callClick: (String) -> Unit, feedBackClick: (Organization) -> Unit, infoDescription: (Organization) -> Unit) =
    adapterDelegateLayoutContainer<Organization, Any>(R.layout.item_org_list) {

        //Вызов
        containerView.tvCall.setOnClickListener { callClick.invoke(item.phone) }
        containerView.imageViewCall.setOnClickListener { callClick.invoke(item.phone) }

        //Info
        containerView.imageViewInfo.setOnClickListener { infoDescription.invoke(item) }
        containerView.tvInfo.setOnClickListener { infoDescription.invoke(item) }

        //Отзывы
        containerView.imgViewFeedBack.setOnClickListener { feedBackClick.invoke(item) }
        containerView.tvFeedBack.setOnClickListener { feedBackClick.invoke(item) }

        bind {
            containerView.tvRating.text = "${item.rating}+"
            containerView.tvCompany.text = item.title
        }
    }

