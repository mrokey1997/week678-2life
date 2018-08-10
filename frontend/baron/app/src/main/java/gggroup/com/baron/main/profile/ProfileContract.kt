package gggroup.com.baron.main.profile

import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.ResultGetUser

interface ProfileContract {

    interface View {
        fun setPresenter(presenter: Presenter)

        fun showNotification(message: String?)

        fun onResponseUserInfo(resultGetUser: ResultGetUser?)

        fun onFailureUserInfo(message: String?)

        fun onResponseSignOut(response: BaseResponse?)

        fun onFailureSignOut(message: String?)
    }

    interface Presenter {
        fun getUserInfo(token: String?)

        fun signOut(token: String?)
    }
}