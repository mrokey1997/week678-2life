package gggroup.com.baron.post

import com.esafirm.imagepicker.model.Image
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

interface PostContract {
    interface View {
        fun showNotification(message: String)

        fun setPresenter(presenter: Presenter)

        fun getImage()

        fun onResponse(message: String)

        fun onFailure(message: String)

        fun displayImg(images: ArrayList<Image>?)

        fun setSpinnerDistrict(districts: LinkedList<String>)

        fun show(isShow: Boolean)

        fun onClick()

        fun post()

        fun isPost(isPost: Boolean)

        fun getCategory(image: Image): String
    }

    interface Presenter {

        fun getDistrict(id: Int)

        fun getAllDistrict()

        fun post(title: String, price: Float, area: Float, description: String, phone: String,
                 type_house: Int, sex: Int, quantity: Int, utils: ArrayList<String>, city: String, district: String, address: String, files: ArrayList<File>?, category: String)
    }
}