package com.urbanfresh.ui.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView

import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.urbanfresh.R
import com.urbanfresh.appUtils.CommonNavigator
import com.urbanfresh.appUtils.CommonUtils
import com.urbanfresh.appUtils.DialogConstant

import java.util.*


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(),
    CommonNavigator {


    private var baseActivity: BaseActivity<*, *>? = null
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

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


    /**
     * This method is called first even before onCreate(),
     * letting us know that your fragment has been attached to an activity.
     *
     * @param context instance of class.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
            activity!!.onFragmentAttached()
        }
    }

    /**
     * This method is used for creating the fragment and
     * initialize essential components of the fragment.
     *
     * @Bundle parameter in which the fragment can save data.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        setHasOptionsMenu(false)

    }


    /**
     * This method is used for draw its user interface for
     * the first time and return view.
     *
     * @Bundle parameter in which the fragment can save data.
     * @ViewGroup parent view group into which the view of fragment to be inserted.
     * @LayoutInflater can create view instance based on layout XML files.
     *
     * @return view return fragment view.
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding!!.lifecycleOwner = this
        mRootView = viewDataBinding!!.root
        return mRootView
    }


    /**
     * This method is called after onDestroy(), to notify that the
     * fragment has been disassociated from its hosting activity.
     */
    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }


    /**
     * This method is called after onCreateView() this is particularly useful
     * when inheriting the onCreateView() implementation but
     * we need to configure the resulting views.
     *
     * @Bundle parameter in which the fragment can save data.
     * @View  basic building block for user interface components.
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
        init()


    }


    /**
     * This method is used for check dynamic permission.
     *
     * @Array list of permissions.
     * @Context instance of current class or activity.
     *
     * @return check and return true or false.
     */
    open fun hasPermissions(context: Context, vararg permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(
                context,
                it.toString()
            ) == PackageManager.PERMISSION_GRANTED
        }


    /**
     * This method is used for set status bar color.
     *
     * @Int color id which want to set.
     */
    fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark
            activity!!.window.statusBarColor =
                ContextCompat.getColor(context!!, color)// set status background white
        }
    }


    /**
     * This method is used for hide soft keyboard.
     */
    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }


    /**
     * This method is used for show progress.
     */
    override fun showProgress() {
        baseActivity!!.showProgress()
    }


    /**
     * This method is used for hide progress.
     */
    override fun hideProgress() {
        baseActivity!!.hideProgress()
    }


    /**
     * This method is used for show alert dialog message for network error.
     *
     * @String error message from server.
     * @Boolean boolean value for is redirect.
     */
    override fun showNetworkError(error: String, isRedirect: Boolean) {
        baseActivity!!.showNetworkError(error, isRedirect)
    }


    /**
     * This method is used for set root visiblity.
     */
    override fun setRootViewVisibility() {

    }


    /**
     * This method is used to get string from string file.
     *
     * @param id is string name from string xml file.
     *
     */
    override fun getStringResource(id: Int): String {
        return resources.getString(id)
    }


    /**
     * This method is used to get resource file by using id.
     *
     * @param id is resource file id.
     */
    override fun getIntegerResource(id: Int): Int {
        return resources.getInteger(id)
    }


    /**
     * This method is used to go to back page.
     */
    override fun onBackClick() {

    }


    /**
     * This method is used to set image from url.
     *
     * @param url it is image url.
     * @param context instance of current class or activity.
     * @param placeHolderId it contains drawable resouurce.
     */
    fun ImageView.setImage(
        url: String,
        context: Context,
        placeHolderId: Int = R.mipmap.ic_launcher
    ) {
        Glide.with(context).load(url).placeholder(placeHolderId).into(this)
    }


    /**
     * This method is used to check internet connection.
     *
     * @param tryAgainClick method if connection disconnected try again.
     * @return Boolean return connect status true or false.
     */
    fun checkIfInternetOn(tryAgainClick: () -> Unit?): Boolean {
        return if (CommonUtils.isInternetOn(baseActivity!!)) {
            true
        } else {
            DialogConstant.noInternetDialog(baseActivity!!, tryAgainClick = {
                tryAgainClick.invoke()
            })
            false
        }
    }


    /**
     * This method is used to manage bottom navigation state.
     */
    fun manageBottomNavigationState() {
        Objects.requireNonNull(getActivity())!!.getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}
