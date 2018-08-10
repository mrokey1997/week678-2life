package gggroup.com.baron.authentication.signin

import android.content.Context
import android.graphics.Bitmap
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

interface SignInContract {
    interface View {
        fun init()

        fun setPresenter(presenter: Presenter)

        fun getAccount()

        fun showNotification(message: String)

        fun onResponse()

        fun onFailure(t: String)

        fun resultLoading(circularProgressButton: CircularProgressButton,
                          fillColor: Int, bitmap: Bitmap,
                          messenger: String)

        fun googleSignOut()
    }

    interface Presenter {
        fun checkAccount(context: Context, email: String, password: String)

        fun handleSignInResult(context: Context, completedTask: Task<GoogleSignInAccount>)
    }
}