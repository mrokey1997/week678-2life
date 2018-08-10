package gggroup.com.baron.detail

import gggroup.com.baron.entities.DetailPost
import gggroup.com.baron.entities.OverviewPost

interface DetailContract {
    interface View {
        fun showNotification(message: String?)

        fun setPresenter(presenter: Presenter)

        fun onResponseDetailPost(post: DetailPost?)

        fun onResponseRecommend(posts: ArrayList<OverviewPost>)

        fun onResponseSavePost()

        fun onResponseUnSavePost()

        fun onResponseCheckVoted(status: String?)

        fun onFailure(message: String?)
//

    }

    interface Presenter {
        fun getDetailPost(id: Int)

        fun recommend(city: String?, district: String?, min_price: Float?, max_price: Float?, type: Int?)

        fun savePost(token: String?, id: String?)

        fun unSavePost(token: String?, id: String?)

        fun checkVoted(token: String?, id: String?)

        //fun recommendWithAI(city: String?, district: String?, min_price: Float?, max_price: Float?, type: Int?,URL: String,ID: Int)
    }
}