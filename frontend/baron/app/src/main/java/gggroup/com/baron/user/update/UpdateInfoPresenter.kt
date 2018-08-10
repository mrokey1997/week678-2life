package gggroup.com.baron.user.update

import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.ResultGetUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateInfoPresenter(internal var view: UpdateInfoContract.View) : UpdateInfoContract.Presenter {
    override fun updateUser(Access_Token: String, full_name: String, phone_number: String) {
        CallAPI.createService().updateUser(Access_Token, full_name, phone_number)
                .enqueue(object : Callback<ResultGetUser> {
                    override fun onFailure(call: Call<ResultGetUser>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ResultGetUser>, response: Response<ResultGetUser>?) {
                        if (response?.body() != null) {
                            view.onResponse(response.body()?.status)
                        }

                    }
                })
    }

    init {
        view.setPresenter(this)
    }
}