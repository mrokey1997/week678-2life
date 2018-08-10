package gggroup.com.baron.user.password


interface ChangePasswordContract {
    interface View {
        fun setPresenter(presenter: Presenter)
        fun onResponse(status: String?)
        fun showNotification(message: String)
    }

    interface Presenter {
        fun changePassword(token: String, password: String, newPasswrod: String)
    }
}

