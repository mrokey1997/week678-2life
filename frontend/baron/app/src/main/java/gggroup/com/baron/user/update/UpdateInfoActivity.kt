package gggroup.com.baron.user.update

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import gggroup.com.baron.R
import gggroup.com.baron.user.profile.ProfileDetailActivity
import kotlinx.android.synthetic.main.activity_update_info.*

class UpdateInfoActivity : AppCompatActivity(), UpdateInfoContract.View {
    override fun onResponse(status: String?) {
        if (status == "true") {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ProfileDetailActivity::class.java))
        } else Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
    }

    private var presenter: UpdateInfoContract.Presenter? = null
    override fun setPresenter(presenter: UpdateInfoContract.Presenter) {
        this.presenter = presenter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_info)
        presenter = UpdateInfoPresenter(this)
        btn_update.setOnClickListener {
            val token = getSharedPreferences("_2life", Context.MODE_PRIVATE).getString("TOKEN_USER", "")

            (presenter as UpdateInfoPresenter).updateUser(token, edt_name.text.toString(), edt_phone.text.toString())
        }
    }

}
