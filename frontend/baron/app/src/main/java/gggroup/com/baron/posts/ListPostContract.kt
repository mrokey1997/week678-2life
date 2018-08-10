package gggroup.com.baron.posts

import gggroup.com.baron.entities.OverviewPost

interface ListPostContract {
    interface View {
        fun showNotification(message: String?)

        fun setPresenter(presenter: Presenter)

        fun onResponse(posts: ArrayList<OverviewPost>?)

        fun onFailure(message: String?)

        fun showShimmerAnimation()

        fun hideShimmerAnimation()
    }

    interface Presenter {
        fun getItemSearch(city: String?, district:String?, min_price:Float?, max_price:Float?, type:Int?, sort: Int?, page: Int)

        fun getItemByType(type: Int?)
    }
}