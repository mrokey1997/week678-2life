package gggroup.com.baron.user.profile

import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.AllPosts
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.ResultGetUser
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileDetailPresenter(internal var view: ProfileDetailContract.View) : ProfileDetailContract.Presenter {
    override fun deletePost(Access_Token: String?, id: Int) {
        CallAPI.createService().deletePost(Access_Token, id)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        response?.body()?.status
                    }

                })
    }

    override fun getUserPosts(Access_Token: String?, page: Int) {
        CallAPI.createService().getUserPosts(Access_Token, page)
                .enqueue(object : Callback<AllPosts> {
                    override fun onFailure(call: Call<AllPosts>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<AllPosts>?, response: Response<AllPosts>?) {
                        view.onResponseUserPosts(response?.body()?.posts?.post)
                    }

                })
    }

    override fun getUser(Access_Token: String?) {
        CallAPI.createService().getUserInfo(Access_Token)
                .enqueue(object : Callback<ResultGetUser> {
                    override fun onFailure(call: Call<ResultGetUser>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ResultGetUser>?, response: Response<ResultGetUser>?) {
                        response?.body()?.let { view.onResponse(it) }
                    }
                }
                )
    }

    override fun updateUser(Access_Token: String?, full_name: String, phone_number: String) {
        CallAPI.createService().updateUser(Access_Token, full_name, phone_number)
                .enqueue(object : Callback<ResultGetUser> {
                    override fun onFailure(call: Call<ResultGetUser>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ResultGetUser>, response: Response<ResultGetUser>?) {
                        response?.body()?.status
                    }

                })
    }

    override fun updateAvatar(Access_Token: String?, avatar: File) {
        val requestBody = RequestBody.create(
                MediaType.parse("image/*"),
                avatar
        )
        val file = MultipartBody.Part.createFormData("avatar", avatar.name, requestBody)
        CallAPI.createService().updateAvatar(Access_Token, file)
                .enqueue(object : Callback<ResultGetUser> {
                    override fun onFailure(call: Call<ResultGetUser>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ResultGetUser>, response: Response<ResultGetUser>?) {
                        if (response?.body() != null) {
                            response.body()?.status
                        }

                    }

                })
    }

    init {
        view.setPresenter(this)
    }
}