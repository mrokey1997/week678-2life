package gggroup.com.baron.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import gggroup.com.baron.R
import gggroup.com.baron.authentication.signin.SignInActivity
import gggroup.com.baron.entities.BaseResponse
import gggroup.com.baron.entities.ResultGetUser
import gggroup.com.baron.user.profile.ProfileDetailActivity

class ProfileFragment : Fragment(), ProfileContract.View {
    private var presenter: ProfileContract.Presenter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvFullname: TextView
    private lateinit var imgAvatar: ImageView

    init {
        presenter = ProfilePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvFullname = view.findViewById(R.id.tv_full_name)
        imgAvatar = view.findViewById(R.id.avatar)
        progressBar=view.findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        val profileDetail = view.findViewById<ConstraintLayout>(R.id.profile_detail)
        val logout = view.findViewById<ConstraintLayout>(R.id.logout)

        val token = context?.getSharedPreferences("_2life", Context.MODE_PRIVATE)
                ?.getString("TOKEN_USER", "")
        presenter.getUserInfo(token)

        profileDetail.setOnClickListener {
            startActivity(Intent(this.context, ProfileDetailActivity::class.java))
        }
        imgAvatar.setOnClickListener {
            startActivity(Intent(this.context, ProfileDetailActivity::class.java))
        }
        logout?.setOnClickListener {
            val builder = this.context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("Đăng xuất")
            builder?.setMessage("Bạn có chắc chắn muốn đăng xuất không?")
            builder?.setPositiveButton("Đăng xuất"){
                dialogInterface, i ->  presenter.signOut(token)
            }
            builder?.setNegativeButton("Hủy"){
                dialogInterface, i ->
            }
            builder?.show()
        }
        return view
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.presenter = presenter
    }

    override fun showNotification(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    override fun onResponseUserInfo(resultGetUser: ResultGetUser?) {
        progressBar.visibility=View.GONE
        if (resultGetUser != null) {
            if(resultGetUser.user?.avatar?.contains("https")!!){
                context?.let {
                    Glide.with(it)
                            .load(resultGetUser.user?.avatar)
                            .into(imgAvatar)
                }
            }else{
                context?.let {
                    Glide.with(it)
                            .load("https:"+resultGetUser.user?.avatar)
                            .into(imgAvatar)
                }
            }

            tvEmail.text = resultGetUser.user?.email
            tvPhone.text = resultGetUser.user?.phone_number
            tvFullname.text = resultGetUser.user?.full_name
        }

    }

    override fun onFailureUserInfo(message: String?) {
        showNotification(message)
    }

    override fun onResponseSignOut(response: BaseResponse?) {
        if (response?.status == "true") {
            context?.getSharedPreferences("_2life", Context.MODE_PRIVATE)?.edit()
                    ?.putString("TOKEN_USER", "empty")?.apply()
            val intent = Intent(this.context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else showNotification("Có lỗi xảy ra")
    }

    override fun onFailureSignOut(message: String?) {
        showNotification(message)
    }
}