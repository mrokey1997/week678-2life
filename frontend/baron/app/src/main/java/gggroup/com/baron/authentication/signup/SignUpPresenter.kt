package gggroup.com.baron.authentication.signup

import android.content.Context
import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.AuthResponse
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpPresenter(internal var view: SignUpContract.View) : SignUpContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun postUser(context: Context, username: String, phone: String, email: String, password: String) {
        if (NetworkUtil.isOnline(context)) {
            CallAPI.createService()
                    .postUser(username, email, password, phone)
                    .enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                            if (response?.body()?.status != "true")
                                view.onFailure("Tài khoản đã tồn tại")
                            else
                                view.alertDisplayer("Mã xác nhận vừa được gửi tới email của bạn. Vui nhập mã xác thực và hoàn tất quá trình đăng ký.")
                        }

                        override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                            view.onFailure(t?.message.toString())
                        }
                    })
        } else
            view.showNotification("Không có kết nối Internet")
    }

    override fun verification(code: String, email: String) {
        CallAPI.createService().verification(email, code)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        if (response?.body()?.status == "true") {
                            view.onResponse()
                        } else {
                            //view.onFailure("Mã xác thực không đúng")
                            view.alertDisplayer("Mã xác thực không đúng. Vui lòng nhập lại.")
                        }
                    }
                })
    }
}