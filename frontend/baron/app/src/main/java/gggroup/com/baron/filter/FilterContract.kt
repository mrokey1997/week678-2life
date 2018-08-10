package gggroup.com.baron.filter

import java.util.*

interface FilterContract {
    interface View {
        fun setPresenter(presenter: Presenter)

        fun setSpinnerDistrict(districts: LinkedList<String>)

        fun onClick()

        fun show(isShow: Boolean)

        fun actionSearch()
    }

    interface Presenter {
        fun getDistrict(id: Int)

        fun getAllDistrict()
    }
}