package gggroup.com.baron.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import gggroup.com.baron.R
import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.OverviewPost
import gggroup.com.baron.utils.HashMapUtils
import kotlinx.android.synthetic.main.item_rv_user_post.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class PostAdapter(private var posts: ArrayList<OverviewPost>, private val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private val NEW_POST = 1
    private val SAVED_POST = 2
    private val USER_POST=3
    private var type : Int = 0

    var itemClickListener : IItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (type) {
            NEW_POST -> ViewHolder(layoutInflater.inflate(R.layout.item_rv_newpost, parent, false))
            SAVED_POST -> ViewHolder(layoutInflater.inflate(R.layout.item_rv_saved_post, parent, false))
            USER_POST ->ViewHolder(layoutInflater.inflate(R.layout.item_rv_user_post,parent,false))
            else -> ViewHolder(layoutInflater.inflate(R.layout.item_rv_post, parent,false))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        if (type == SAVED_POST)
            holder.tvTitle.text = post.title
        holder.tvAddressDistrict.text = post.address?.district
        holder.tvAddressStreet.text = post.address?.add_detail
        val formatter = DecimalFormat("#,###,###")
        holder.tvPrice.text = "${formatter.format(post.price?.times(1000000))} VNĐ/tháng"
        holder.tvUtils.text = getUtilsRoom(post.detail_ids)
        holder.tvObject.text =
                if (type == NEW_POST || type == SAVED_POST) "• ${HashMapUtils.sex[post.sex]?.toUpperCase()}"
                else HashMapUtils.sex[post.sex]
        holder.tvType.text =
                if (type == NEW_POST || type == SAVED_POST) HashMapUtils.typeHouse[post.type_house]?.toUpperCase()
                else HashMapUtils.typeHouse[post.type_house]
        holder.tvArea.text =
                if (type == NEW_POST || type == SAVED_POST) "• ${post.area}m²"
                else "${post.area}m²"
        Glide.with(context).load("https:${post.image?.image}").into(holder.imgMain)
        if(type==USER_POST) holder.imgDelete
        holder.imgDelete?.setOnClickListener {

            val token = context.getSharedPreferences("_2life", Context.MODE_PRIVATE)
                    .getString("TOKEN_USER", "")

            val builder = this.context.let { it1 -> AlertDialog.Builder(it1) }
            builder.setTitle("Xóa Bài Đăng")
            builder.setMessage("Bạn có chắc chắn muốn xóa không?")
            builder.setPositiveButton("Xóa"){
                _, _ ->   removeAt(position)
                deletePost(token,post.id!!)
        }
            builder.setNegativeButton("Hủy"){
                _, _ ->
            }
            builder.show()
        }
    }

    private fun getUtilsRoom(utils : List<String>?) = when (utils?.size) {
        0 -> ""
        1 -> utils[0]
        2 -> "${utils[0]}, ${utils[1]}"
        else -> "${utils!![0]}, ${utils[1]},..."
    }


    override fun getItemCount(): Int {
        return posts.size
    }

    fun setType(type : Int) {
        this.type = type
        notifyDataSetChanged()
    }

    fun setData(posts: ArrayList<OverviewPost>) {
        if (itemCount == 0)
            this.posts = posts
        else this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun clearData() {
        posts.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: IItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val tvTitle = view.tv_title as TextView
        val tvAddressDistrict = view.tv_address_district as TextView
        val tvAddressStreet = view.tv_address_street as TextView
        val tvPrice = view.tv_price as TextView
        val tvUtils = view.tv_utils as TextView
        val tvObject = view.tv_object as TextView
        val tvType = view.tv_type as TextView
        val tvArea = view.tv_area as TextView
        val imgMain = view.img_main as ImageView
        val imgDelete= view.img_delete
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            itemClickListener?.onClickItem(posts[adapterPosition], imgMain)
        }
    }

    fun removeAt(position: Int) {
        posts.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position)
    }
    fun deletePost(Access_Token: String?, id: Int) {
        CallAPI.createService().deletePost(Access_Token,id)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {

                    }
                    override fun onResponse(call: Call<BaseResponse>?, response: Response<BaseResponse>?) {
                       if(response?.body()?.status=="true")
                        Toast.makeText(context,"Xóa thành công",Toast.LENGTH_LONG).show()
                        else Toast.makeText(context,"Có lỗi xảy ra",Toast.LENGTH_LONG).show()
                    }

                })
    }
}