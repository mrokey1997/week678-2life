package gggroup.com.baron.main.saved

import gggroup.com.baron.entities.OverviewPost

interface SavedContract {
    interface View{
        fun showNotification(message: String?)

        fun setPresenter(presenter: Presenter)

        fun onResponse(posts: ArrayList<OverviewPost>?)

        fun onFailure(message: String?)

        fun showShimmerAnimation()

        fun hideShimmerAnimation()
    }
    interface Presenter{
        fun getVote(token:String?)
    }
}