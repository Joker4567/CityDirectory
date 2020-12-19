package com.anufriev.presentation.delegates

import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.presentation.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_org_list.view.*

fun itemOrgList(
    callClick: (OrganizationDaoEntity) -> Unit,
    feedBackClick: (OrganizationDaoEntity) -> Unit,
    infoDescription: (OrganizationDaoEntity) -> Unit,
    ratingClick:(String) -> Unit) =
    adapterDelegateLayoutContainer<OrganizationDaoEntity, Any>(R.layout.item_org_list) {

        //Вызов
        containerView.imageViewCall.setOnClickListener { callClick.invoke(item) }

        //Rating
        containerView.tvRating.setOnClickListener { ratingClick.invoke("") }
        containerView.tvRateText.setOnClickListener { ratingClick.invoke("") }

        //Info
        containerView.imageViewInfo.setOnClickListener { infoDescription.invoke(item) }
        containerView.tvInfo.setOnClickListener { infoDescription.invoke(item) }

        //Отзывы
        containerView.imgViewFeedBack.setOnClickListener { feedBackClick.invoke(item) }
        containerView.tvFeedBack.setOnClickListener { feedBackClick.invoke(item) }

        bind {
            if(item.rating > 0)
                containerView.tvRating.text = "${item.rating}+"
            else if(item.rating <= 0)
                containerView.tvRating.text = "${item.rating}"
            containerView.tvCompany.text = item.name
        }
    }

