package gggroup.com.baron.adapter

import android.widget.ImageView
import gggroup.com.baron.entities.OverviewPost

interface IItemClickListener {
    fun onClickItem(post : OverviewPost, animationView: ImageView)
}