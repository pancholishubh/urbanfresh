package com.urbanfresh.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.urbanfresh.BR
import com.urbanfresh.R
import com.urbanfresh.databinding.ActivityHomeBinding
import com.urbanfresh.ui.base.BaseActivity
import com.urbanfresh.ui.home.homefragment.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HomeNavgator {


    override val bindingVariable: Int
        get() = BR.homeVM
    override val layoutId: Int
        get() = R.layout.activity_home
    override val viewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        init()
    }


    override fun init() {
        replaceFragment(HomeFragment())
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_explore -> {
                    replaceFragment(HomeFragment())
                }
                R.id.navigation_fav -> {
                }
                R.id.navigation_shop -> {
                }
                R.id.navigation_cart -> {
                }
                R.id.navigation_account -> {
                }

            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()
    }

}