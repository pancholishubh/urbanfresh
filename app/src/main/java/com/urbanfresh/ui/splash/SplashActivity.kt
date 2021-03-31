package com.urbanfresh.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.urbanfresh.BR
import com.urbanfresh.R
import com.urbanfresh.databinding.ActivitySplashBinding
import com.urbanfresh.ui.base.BaseActivity
import com.urbanfresh.ui.home.HomeActivity


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private lateinit var splashViewModel: SplashViewModel
    override val viewModel: SplashViewModel
        get() {
            splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
            return splashViewModel
        }

    override val bindingVariable: Int
        get() = BR.splashVM

    override val layoutId: Int
        get() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        statusBarTransparent()
        viewModel.init()
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            startSignInActivity()
        }
    }


    override fun startSignInActivity() {
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
    }


    override fun init() {
        mDelayHandler = Handler(Looper.getMainLooper())
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }


}