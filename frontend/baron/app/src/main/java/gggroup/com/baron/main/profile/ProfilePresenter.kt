package gggroup.com.baron.main.profile

import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.ResultGetUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(internal var view: ProfileContract.View) : ProfileContract.Presenter {
    override fun getUserInfo(token: String?) {
        CallAPI.createService()
                .getUserInfo(token)
                .enqueue(object : Callback<ResultGetUser> {
                    override fun onResponse(call: Call<ResultGetUser>?, response: Response<ResultGetUser>?) {
                        if (response?.body() != null) {
                            view.onResponseUserInfo(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ResultGetUser>?, t: Throwable?) {
                        view.onFailureUserInfo(t?.message)
                    }
                })
    }

    override fun signOut(token: String?) {
        CallAPI.createService()
                .signOut(token)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        if (response?.body() != null)
                            view.onResponseSignOut(response.body())
                    }

                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                        view.onFailureSignOut(t?.message)
                    }
                })
    }

    init {
        view.setPresenter(this)
    }
}