package gggroup.com.baron.user.profile

import gggroup.com.baron.entities.OverviewPost
import gggroup.com.baron.entities.ResultGetUser
import java.io.File

interface ProfileDetailContract {
    interface View {
        fun setPresenter(presenter: Presenter)
        fun onResponse(resultGetUser: ResultGetUser)
        fun onResponseUserPosts(posts: ArrayList<OverviewPost>?)
    }

    interface Presenter {
        fun getUser(Access_Token: String?)
        fun updateUser(Access_Token: String?, full_name: String, phone_number: String)
        fun getUserPosts(Access_Token: String?, page: Int)
        fun updateAvatar(Access_Token: String?, avatar: File)
        fun deletePost(Access_Token: String?, id: Int)
    }
}