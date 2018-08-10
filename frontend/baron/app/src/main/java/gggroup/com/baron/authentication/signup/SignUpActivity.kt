package gggroup.com.baron.authentication.signup

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import gggroup.com.baron.R
import kotlinx.android.synthetic.main.activity_signup.*


class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    private var presenter: SignUpContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        presenter = SignUpPresenter(this)

        setClick()
    }

    override fun setClick() {
        btn_sign_up.setOnClickListener {
            // Handle username
            when {
                edt_username.text.isEmpty() -> showNotification("Tên người dùng không được bỏ trống")
                edt_phone.text.isEmpty() -> showNotification("Số điện thoại không được bỏ trống")
                edt_email.text.isEmpty() -> showNotification("Địa chỉ email không được bỏ trống")
                edt_password.text.isEmpty() -> showNotification("Mật khẩu không được bỏ trống")
                edt_check_password.text.isEmpty() -> showNotification("Xác nhận mật khẩu không được bỏ trống")
                edt_password.text.length < 6 -> showNotification("Mật khẩu phải dài hơn 6 ký tự")
                edt_password.text.toString() != edt_check_password.text.toString() -> showNotification("Mật khẩu và mật khẩu xác nhận không giống nhau")
                else -> {
                    btn_sign_up.startAnimation()
                    presenter?.postUser(this, edt_username.text.toString(),
                            edt_phone.text.toString(),
                            edt_email.text.toString(),
                            edt_password.text.toString())
                }
            }
        }
    }

    override fun onResponse() {
        result(btn_sign_up,
                ContextCompat.getColor(this@SignUpActivity, R.color.green),
                BitmapFactory.decodeResource(resources, R.drawable.ic_done), "Đăng ký thành công")
    }

    override fun onFailure(t: String?) {
        result(btn_sign_up,
                ContextCompat.getColor(this@SignUpActivity, R.color.colorAccent),
                BitmapFactory.decodeResource(resources, R.drawable.ic_error), "Đăng ký thất bại. Lỗi: $t")
    }

    override fun showNotification(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: SignUpContract.Presenter) {
        this.presenter = presenter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(0, R.anim.exit)
        finish()
    }

    private fun result(circularProgressButton: CircularProgressButton,
                       fillColor: Int, bitmap: Bitmap, messenger: String) {
        val doneAnimationRunnable = {
            circularProgressButton.doneLoadingAnimation(
                    fillColor,
                    bitmap)
        }
        //done
        with(Handler()) {

            //end animation
            postDelayed(doneAnimationRunnable, 1000)

            postDelayed({ showNotification(messenger) }, 1000)
            if (messenger == "Đăng ký thành công")
                postDelayed({
                    this@SignUpActivity.overridePendingTransition(0, R.anim.exit)
                    finish()
                }, 2000)
            else
                postDelayed({ circularProgressButton.revertAnimation() }, 2000)
        }
    }

    override fun alertDisplayer(messenge: String) {
        val builder = AlertDialog.Builder(this@SignUpActivity)
                .setTitle("Xác thực email")
                .setMessage(messenge)
        val input = EditText(this@SignUpActivity)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        builder.setView(input) // uncomment this line

        builder.setPositiveButton("OK") { dialog, _ ->
            //dialog.cancel()
            val code = input.text.toString()
            val email = edt_email.text.toString()
            presenter?.verification(code, email)

//            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
        }
        val ok = builder.create()
        ok.show()
    }
}