package gggroup.com.baron.authentication.signup

import android.content.Context

interface SignUpContract {
    interface View {
        fun showNotification(message: String)

        fun setPresenter(presenter: Presenter)

        fun onResponse()

        fun onFailure(t: String?)

        fun setClick()

        fun alertDisplayer(message: String)
    }

    interface Presenter {
        fun postUser(context: Context, username: String, phone: String, email: String, password: String)

        fun verification(code: String, email: String)
    }
}