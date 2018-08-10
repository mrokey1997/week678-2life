package gggroup.com.baron.detail

import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.AllPosts
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.DetailPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPresenter(internal var view: DetailContract.View) : DetailContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun getDetailPost(id: Int) {
        CallAPI.createService()
                .getDetailPost(id)
                .enqueue(object : Callback<DetailPost> {
                    override fun onResponse(call: Call<DetailPost>?, response: Response<DetailPost>?) {
                        if (response?.body() != null)
                            view.onResponseDetailPost(response.body())
                    }

                    override fun onFailure(call: Call<DetailPost>?, t: Throwable?) {
                        view.onFailure(t?.message)
                    }
                })
    }

    override fun recommend(city: String?, district: String?, min_price: Float?, max_price: Float?, type: Int?) {
        CallAPI.createService()
                .searchRecommend(city.toString(), district.toString(), min_price, max_price, type)
                .enqueue(object : Callback<AllPosts> {
                    override fun onResponse(call: Call<AllPosts>?, response: Response<AllPosts>?) {
                        if (response?.body()?.posts?.post != null) {
                            view.onResponseRecommend(response.body()?.posts?.post!!)
//                            val allPost = response.body()?.posts?.post
//                            val posts: ArrayList<OverviewPost> = ArrayList()
//                            val size = allPost?.size!!
//                            for (j in size - 1 downTo 0 step 1) {
//                                if (allPost[j].id != ID) {
//                                    posts.add(allPost[j])
//                                    if (posts.size == 5) {
//                                        view.onResponseRecommend(posts)
//                                        return
//                                    }
//                                    break
//                                }
//                            }
                        }
                    }

                    override fun onFailure(call: Call<AllPosts>?, t: Throwable?) {
                        view.onFailure(t?.message)
                    }
                })
    }

    override fun savePost(token: String?, id: String?) {
        CallAPI.createService()
                .savePost(token, id)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        if (response?.body() != null)
                            view.onResponseSavePost()
                    }

                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                        view.onFailure(t?.message)
                    }
                })
    }

    override fun unSavePost(token: String?, id: String?) {
        CallAPI.createService()
                .unSavePost(token, id)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        if (response?.body() != null)
                            view.onResponseUnSavePost()
                    }

                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                        view.onFailure(t?.message)
                    }
                })
    }

    override fun checkVoted(token: String?, id: String?) {
        CallAPI.createService()
                .checkVoted(token, id)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                        if (response?.body() != null) {
                            view.onResponseCheckVoted(response.body()?.status)
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                        view.onFailure(t?.message)
                    }
                })
    }
//    override fun recommendWithAI(city: String?, district: String?, min_price: Float?, max_price: Float?, type: Int?,URL: String,ID: Int) {
//        CallAPI.createService()
//                .search(city.toString(), district.toString(), min_price, max_price, type)
//                .enqueue(object : Callback<AllPosts> {
//                    override fun onResponse(call: Call<AllPosts>?, response: Response<AllPosts>?) {
//                        if (response?.body()?.posts?.post != null) {
//                            val result = view.getImage(URL)
//                            val allPost = response.body()?.posts?.post
//                            val posts: ArrayList<OverviewPost> = ArrayList()
//                            val size = allPost?.size!!
//                            for (j in size-1 downTo  0 step 1) {
//                                for (i in 0 until result.size) {
//                                    val isEqual = view.checkImage("https:${allPost[i].image}",result[0],result[1])
//                                    if(isEqual && allPost[j].id != ID){
//                                        posts.add(allPost[j])
//                                        if(posts.size==5) {
//                                            view.onResponseRecommend(posts)
//                                            return
//                                        }
//                                        break
//                                    }
//                                }
//                            }
//                            view.onResponseRecommend(posts)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<AllPosts>?, t: Throwable?) {
//                        view.onFailure(t?.message)
//                    }
//                })
//    }
}