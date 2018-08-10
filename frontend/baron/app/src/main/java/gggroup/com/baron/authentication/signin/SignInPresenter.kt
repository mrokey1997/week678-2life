package gggroup.com.baron.authentication.signin

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import gggroup.com.baron.api.CallAPI
import gggroup.com.baron.entities.AuthResponse
import gggroup.com.baron.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInPresenter(internal var view: SignInContract.View) : SignInContract.Presenter {
    init {
        view.setPresenter(this)
    }

    override fun checkAccount(context: Context, email: String, password: String) {
        if (NetworkUtil.isOnline(context)) {
            CallAPI.createService()
                    .checkUser(email, password)
                    .enqueue(object : Callback<AuthResponse> {
                        override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                            view.onFailure(t?.message.toString())
                        }

                        override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                            if (response?.body()?.status == "true") {
                                SignInActivity.TOKEN = response.body()?.access_token
                                val preferences = context.getSharedPreferences("_2life", Context.MODE_PRIVATE).edit()
                                preferences.putString("TOKEN_USER", response.body()?.access_token).apply()
                                view.onResponse()
                            } else {
                                view.onFailure("Tài khoản hoặc mật khẩu không đúng")
                            }
                        }

                    })
        } else
            view.onFailure("Không có kết nối Internet")
    }

    override fun handleSignInResult(context: Context, completedTask: Task<GoogleSignInAccount>) {
        try {
            if (NetworkUtil.isOnline(context)) {
                val account = completedTask.getResult(ApiException::class.java)
                CallAPI.createService()
                        .signInWithGoogle(account.displayName.toString(), account.email.toString())
                        .enqueue(object : Callback<AuthResponse> {
                            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                                view.onFailure(t?.message.toString())
                            }

                            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                                SignInActivity.TOKEN = response?.body()?.access_token
                                val preferences = context.getSharedPreferences("_2life", Context.MODE_PRIVATE).edit()
                                preferences.putString("TOKEN_USER", response?.body()?.access_token).apply()
                                view.onResponse()
                                view.googleSignOut()
                            }
                        })
            } else
                view.onFailure("Không có kết nối Internet")
            //Toast.makeText(this, "Welcome " + account.displayName, Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
            // view.showNotification("signInResult:failed code=" + e.message.toString())
        }
    }
}