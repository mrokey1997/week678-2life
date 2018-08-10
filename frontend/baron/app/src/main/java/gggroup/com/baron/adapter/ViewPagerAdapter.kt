package gggroup.com.baron.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import gggroup.com.baron.R
import gggroup.com.baron.entities.ImagesUrlDetailPost

class ViewPagerAdapter(private val context: Context,private val list_url: MutableList<ImagesUrlDetailPost>?) : PagerAdapter() {

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_card, container, false)

        val url = list_url!![position]

        val imgCard = view.findViewById<ImageView>(R.id.img_card)
        Glide.with(context).load("https:${url.image}").into(imgCard)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list_url?.size ?: 10

}