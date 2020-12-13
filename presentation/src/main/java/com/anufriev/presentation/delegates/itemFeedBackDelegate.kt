package com.anufriev.presentation.delegates

import android.graphics.Color
import com.anufriev.data.db.entities.FeedBack
import com.anufriev.presentation.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.item_feedback_list.view.*

fun itemFeedBackList() =
    adapterDelegateLayoutContainer<FeedBack, Any>(R.layout.item_feedback_list) {

        bind {
            containerView.tvDescriptionFeedBack.text = item.description
            if(item.state) {
                containerView.imageViewFeedBack.setImageResource(R.drawable.ic_round_thumb_up_alt_24)
            }
            else {
                containerView.imageViewFeedBack.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24)
            }
        }
    }