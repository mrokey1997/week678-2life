package gggroup.com.baron.user.update

interface UpdateInfoContract {
    interface View {
        fun setPresenter(presenter: Presenter)
        fun onResponse(status: String?)
    }

    interface Presenter {
        fun updateUser(Access_Token: String, full_name: String, phone_number: String)
    }
}