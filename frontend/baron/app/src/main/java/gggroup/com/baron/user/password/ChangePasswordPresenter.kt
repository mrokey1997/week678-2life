package gggroup.com.baron.user.password

import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordPresenter(internal var view: ChangePasswordContract.View) : ChangePasswordContract.Presenter {
    override fun changePassword(token: String, password: String, newPasswrod: String) {
        CallAPI.createService().changePassword(token, password, newPasswrod)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        view.onResponse(response?.body()?.status)
                    }

                })
    }

    init {
        view.setPresenter(this)
    }
}