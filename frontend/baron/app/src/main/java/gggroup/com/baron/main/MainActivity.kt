package gggroup.com.baron.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import gggroup.com.baron.R
import gggroup.com.baron.main.home.HomeFragment
import gggroup.com.baron.main.profile.ProfileFragment
import gggroup.com.baron.main.saved.SavedFragment
import gggroup.com.baron.utils.OnPagerNumberChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import gggroup.com.baron.filter.FilterActivity


class MainActivity: AppCompatActivity(), OnPagerNumberChangeListener {

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var savedFragment: SavedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()

        startFragment(homeFragment)

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> startFragment(homeFragment)
                R.id.item_saved -> startFragment(savedFragment)
                R.id.item_profile -> startFragment(profileFragment)
                else -> true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_filter -> {
                startActivity(Intent(this,FilterActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun initFragment() {
        homeFragment = HomeFragment.newInstance()
        profileFragment = ProfileFragment()
        savedFragment = SavedFragment.newInstance()
    }

    private fun startFragment(fragment: Fragment?) : Boolean {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            return true
        }
        return false
    }

    override fun onPagerNumberChanged() {
        (homeFragment as OnPagerNumberChangeListener).onPagerNumberChanged()
    }
}
