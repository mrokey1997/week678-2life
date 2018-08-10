package gggroup.com.baron.main.home

import gggroup.com.baron.entities.OverviewPost

interface HomeContract {
    interface View {
        fun showNotification(message: String?)

        fun setPresenter(presenter: Presenter)

        fun onResponse(posts: ArrayList<OverviewPost>?)

        fun onFailure(message: String?)

        fun showShimmerAnimation()

        fun hideShimmerAnimation()

        fun actionSearch(type: Int)
    }

    interface Presenter {
        fun getNewPosts()
    }
}