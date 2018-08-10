package gggroup.com.baron.posts

import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.AllPosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPostPresenter(internal var view: ListPostContract.View) : ListPostContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun getItemSearch(city: String?, district: String?, min_price: Float?, max_price: Float?, type: Int?, sort: Int?, page: Int) {
        view.showShimmerAnimation()
        CallAPI.createService().search(city, district, min_price, max_price, type, sort, page)
                .enqueue(object : Callback<AllPosts> {
                    override fun onFailure(call: Call<AllPosts>?, t: Throwable?) {
                        view.onFailure("Lỗi không xác định")
                    }

                    override fun onResponse(call: Call<AllPosts>?, response: Response<AllPosts>?) {
                        if (response?.body()?.posts?.post != null) {
                            view.onResponse(response.body()?.posts?.post)
                        }
                    }

                })
    }

    override fun getItemByType(type: Int?) {
        view.showShimmerAnimation()
        CallAPI.createService().searchByType(type)
                .enqueue(object : Callback<AllPosts> {
                    override fun onFailure(call: Call<AllPosts>?, t: Throwable?) {
                        view.onFailure("Lỗi không xác định")
                    }

                    override fun onResponse(call: Call<AllPosts>?, response: Response<AllPosts>?) {
                        if (response?.body()?.posts?.post != null) {
                            view.onResponse(response.body()?.posts?.post)
                        }
                    }

                })
    }
}