package gggroup.com.baron.paper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import gggroup.com.baron.R
import gggroup.com.baron.authentication.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_paper_onboarding.*
import java.util.ArrayList

class PaperOnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paper_onboarding)

        val fragmentManager = supportFragmentManager

        val onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding())

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment)
        fragmentTransaction.commit()

        tv_skip.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        onBoardingFragment.setOnRightOutListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

    }

    private fun getDataForOnBoarding(): ArrayList<PaperOnboardingPage>? {
        val scr1 = PaperOnboardingPage(
                "Tin cậy",
                "Phòng chính chủ, ảnh thật, liên hệ trực tiếp!",
                Color.parseColor("#678FB4"), R.drawable.mot, R.drawable.ic_star)
        val scr2 = PaperOnboardingPage(
                "Nhanh chóng",
                "Tính năng lọc thông minh giúp chọn phòng trong 5 phút!",
                Color.parseColor("#65B0B4"), R.drawable.hai, R.drawable.ic_tick)
        val scr3 = PaperOnboardingPage(
                "Đa dạng",
                "Hàng nghìn phòng mới cập nhập liên tục",
                Color.parseColor("#9B90BC"), R.drawable.ba, R.drawable.ic_dollar)

        val elements = ArrayList<PaperOnboardingPage>()
        elements.add(scr1)
        elements.add(scr2)
        elements.add(scr3)
        return elements
    }
}