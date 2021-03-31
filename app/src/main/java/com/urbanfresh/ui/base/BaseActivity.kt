package com.urbanfresh.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.urbanfresh.MyApplication
import com.urbanfresh.R
import com.urbanfresh.appUtils.CommonNavigator
import com.urbanfresh.appUtils.CommonUtils
import com.urbanfresh.appUtils.CustomProgressDialog
import com.urbanfresh.appUtils.DialogConstant
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.disposables.CompositeDisposable
import java.util.*


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseFragment.Callback,
    CommonNavigator {

    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    private var permission: Array<String>? = null
    private var pDialog: CustomProgressDialog? = null
    private val disposable = CompositeDisposable()
    val myRequestCode = 1


    /**
     * This is used to check dynamic permission.
     */
    open fun hasPermissions(context: Context, vararg permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(
                context,
                it.toString()
            ) == PackageManager.PERMISSION_GRANTED
        }

    fun statusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }


    private var myLocale: Locale? = null


    /**
     * This is used to set application language dynamically.
     *
     * @param String language string.
     *
     */
    fun setLocale(appLanguage: String) {
        val getLanguage =
            Resources.getSystem().configuration.locale.getDisplayLanguage(Locale.getDefault())
        if (appLanguage != getLanguage) {
            myLocale = Locale(appLanguage)
            var cxt: Context = MyApplication.instance!!.applicationContext
            val resources = cxt.resources
            val configuration = resources.configuration
            Locale.setDefault(myLocale!!)
            configuration.setLocale(myLocale)
            if (Build.VERSION.SDK_INT >= 25) {
                cxt = cxt.applicationContext.createConfigurationContext(configuration)
                cxt = cxt.createConfigurationContext(configuration)
            }
            cxt.resources.updateConfiguration(
                configuration,
                resources.displayMetrics
            )
        }
    }


    /**
     * This is used to set application language dynamically.
     *
     * @param String language string.
     * @param Context is instance of current activity and class.
     */
    @SuppressWarnings("deprecation")
    fun setLocale(appLanguage: String, context: Context) {
        val locale: Locale = Locale(appLanguage)
        val config = Configuration(context.resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )


    }


    /**
     * When an activity first call or launched then this method
     * is responsible to create the activity.
     *
     * @param savedInstanceState load all data from savedInstanceState.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        permission = null
        performDataBinding()
        viewDataBinding!!.lifecycleOwner = this
    }


    /**
     * This this method is used to hide device keyboard.
     *
     * @param Context is current class activity or classes.
     */
    open fun hideKeyboard(activity: Activity) {
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /**
     * This this method is used to check required permission.
     *
     * @param Array is permissions list.
     */
    fun checkRequiredPermission(permission: Array<String>) {
        this.permission = permission
        checkPermissions(*permission)
    }


    /**
     * called when required permission is granted to notify in child class need to override this
     */
    protected open fun invokedWhenPermissionGranted() {}

    /**
     * called when required permission is not or already granted to notify in child class need to override this
     */
    open fun invokedWhenNoOrAlreadyPermissionGranted() {}

    /**
     * check the permission
     *
     * @param permission
     */
    private fun checkPermissions(vararg permission: String) {

        if (Build.VERSION.SDK_INT >= 23) {

            var result: Int
            val listPermissionsNeeded = ArrayList<String>()
            for (p in permission) {
                result = ContextCompat.checkSelfPermission(this, p)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p)
                }
            }
            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 111)
            } else {
                invokedWhenNoOrAlreadyPermissionGranted()
            }
        } else {
            invokedWhenNoOrAlreadyPermissionGranted()
        }
    }


    /**
     * This this method is used to grant permission.
     *
     * @param IntArray no fo permission
     * @param Array list of permission
     * @param Int it is request code.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted()
        } else if (requestCode == 111) {
            invokedWhenDeniedWithResult(grantResults)
        }
    }

    /**
     * called when all required permission is checked and granted
     */
    private fun allPermissionsGranted() {
        invokedWhenPermissionGranted()
    }

    /**
     * check and show denied permission to notify in child class need to Override this
     *
     * @param grantResults
     */
    protected open fun invokedWhenDeniedWithResult(grantResults: IntArray) {}

    /**
     * check all permission granted
     *
     * @param grantResults
     * @return
     */
    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }


    /**
     * This this method is used to hide soft keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /**
     * This this method is used for data binding.
     */
    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }


    /*invoked when permission granted*/
    protected open fun rxPermissionGranted() {

    }

    /*invoked when permission denied*/
    protected open fun rxPermissionDenied() {

    }

    /* invoked when permission with ask never again*/
    protected open fun rxPermissionDeniedAskNeverAgain() {

    }


    /**
     * This this method is used to set status bar color.
     *
     * @param Int it is color code.
     *
     */
    fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark
            window.statusBarColor =
                ContextCompat.getColor(this, color)// set status background white
        }
    }


    /**
     * This this method is used to hide status bar.
     */
    fun hideStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    /**
     * This is BaseActivity override method.
     */
    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard(this)
        disposable.dispose()
    }


    /**
     * This is BaseActivity override method.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
    }


    /**
     * This is BaseActivity override method for showing progress dialog.
     */
    override fun showProgress() {
        if (pDialog == null) {
            pDialog = CustomProgressDialog(this)
        }
        pDialog!!.showProgressDialog()
    }


    /**
     * This method is used to hide progress.
     */
    override fun hideProgress() {
        if (pDialog != null) {
            pDialog!!.hideProgressDialog()
        }
    }


    /**
     * This method is used set root view visibility.
     *
     */
    override fun setRootViewVisibility() {}


    /**
     * This method is used get string resource from string.xml.
     *
     * @param Int string value name
     */
    override fun getStringResource(id: Int): String {
        return resources.getString(id)
    }


    /**
     * This method is used get integer resource .
     *
     * @param Int passing value
     */
    override fun getIntegerResource(id: Int): Int {
        return resources.getInteger(id)
    }


    /**
     * This BaseActivity method is override method.
     */
    override fun onBackClick() {
        onBackPressed()
    }


    /**
     * This method is used to check internet connection.
     */
    fun checkIfInternetOn(tryAgainClick: () -> Unit?): Boolean {
        return if (CommonUtils.isInternetOn(this)) {
            true
        } else {
            DialogConstant.noInternetDialog(this, tryAgainClick = {
                tryAgainClick.invoke()
            })
            false
        }
    }


    /**
     * This method is used to set image
     * to imageview using url.
     *
     * @param String image url.
     * @param Context current activity or classes.
     * @param Int setting image id.
     */
    fun ImageView.setImage(
        url: String,
        context: Context,
        placeHolderId: Int = R.drawable.ic_launcher_background
    ) {
        Glide.with(context).load(url).placeholder(placeHolderId).into(this)
    }


    /**
     * This method is used to set image using url
     * on circle image view.
     *
     * @param String image url.
     * @param Context current activity or classes.
     * @param Int setting image id.
     */
    fun CircleImageView.setImage(
        url: String,
        context: Context,
        placeHolderId: Int = R.mipmap.ic_launcher
    ) {
        Glide.with(context).load(url).placeholder(placeHolderId).into(this)
    }


    /***************/
}

