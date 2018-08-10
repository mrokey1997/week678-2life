package gggroup.com.baron.post

import android.os.Handler
import android.os.Looper
import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.authentication.signin.SignInActivity
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.District
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostPresenter(internal var view: PostContract.View) : PostContract.Presenter {
    val hanoi: LinkedList<String> = LinkedList()
    val hochiminh: LinkedList<String> = LinkedList()

    init {
        view.setPresenter(this)
    }

    override fun getAllDistrict() {
        view.show(false)
        CallAPI.createService()
                .getDistrict(1)
                .enqueue(object : Callback<ArrayList<District>> {
                    override fun onResponse(call: Call<ArrayList<District>>?, response: Response<ArrayList<District>>?) {
                        Thread {
                            val result = response?.body()
                            if (result != null) {
                                for (i in 0 until result.size) {
                                    hanoi.add(result[i].name)
                                }
                            }
                            val handler = Handler(Looper.getMainLooper())
                            handler.post {
                                getHCM()
                            }
                        }.start()
                    }

                    override fun onFailure(call: Call<ArrayList<District>>?, t: Throwable?) {}
                })

    }

    fun getHCM() {
        CallAPI.createService()
                .getDistrict(2)
                .enqueue(object : Callback<ArrayList<District>> {
                    override fun onResponse(call: Call<ArrayList<District>>?, response: Response<ArrayList<District>>?) {
                        Thread {
                            val result = response?.body()
                            if (result != null) {
                                for (i in 0 until result.size) {
                                    hochiminh.add(result[i].name)
                                }
                            }
                            val handler2 = android.os.Handler(Looper.getMainLooper())
                            handler2.post {
                                view.setSpinnerDistrict(hanoi)
                                view.show(true)
                            }
                        }.start()
                    }

                    override fun onFailure(call: Call<ArrayList<District>>?, t: Throwable?) {
                    }
                })
    }

    override fun getDistrict(id: Int) {
        if (id == 0)
            view.setSpinnerDistrict(hanoi)
        else
            view.setSpinnerDistrict(hochiminh)
    }

    override fun post(title: String, price: Float, area: Float, description: String, phone: String,
                      type_house: Int, sex: Int, quantity: Int, utils: ArrayList<String>, city: String,
                      district: String, address: String, files: ArrayList<File>?, category: String) {
        view.isPost(true)
        Thread {
            var surveyImagesParts = arrayOfNulls<MultipartBody.Part>(0)
            //val images:  ArrayList<MultipartBody.Part> = ArrayList()
            // create RequestBody instance from file
            if (files != null) {
                surveyImagesParts = arrayOfNulls(files.size)
                for (i in 0 until files.size) {
                    val requestFile = RequestBody.create(
                            MediaType.parse("image/*"),
                            files[0]
                    )
                    // MultipartBody.Part is used to send also the actual file name
                    surveyImagesParts[i] = MultipartBody.Part.createFormData("attachments[][image]", files[0].name, requestFile)
                }
            }
            val myTitle = RequestBody.create(MediaType.parse("text/plain"), title)
            val myCategory = RequestBody.create(MediaType.parse("text/plain"), category)
            val myDescription = RequestBody.create(MediaType.parse("text/plain"), description)
            val myPhone = RequestBody.create(MediaType.parse("text/plain"), phone)
            val myCity = RequestBody.create(MediaType.parse("text/plain"), city)
            val myDistrict = RequestBody.create(MediaType.parse("text/plain"), district)
            val myAddress = RequestBody.create(MediaType.parse("text/plain"), address)
            val myUtils: Array<RequestBody?> = arrayOfNulls(utils.size)
            for (i in 0 until utils.size) {
                val itemUtils = RequestBody.create(MediaType.parse("text/plain"), utils[i])
                myUtils[i] = itemUtils
            }
            val handler = android.os.Handler(Looper.getMainLooper())
            handler.post {
                CallAPI.createService().post(SignInActivity.TOKEN, myTitle, myCategory, price, area, myDescription, myPhone,
                        type_house, sex, quantity, myUtils, myCity, myDistrict, myAddress, surveyImagesParts)
                        .enqueue(object : Callback<BaseResponse> {
                            override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                                if (response?.body()?.status == "true") {
                                    view.showNotification("success")
                                    view.isPost(false)
                                }
                            }

                            override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {}
                        })
            }
        }.start()

    }
}