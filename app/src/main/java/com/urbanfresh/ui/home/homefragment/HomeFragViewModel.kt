package com.urbanfresh.ui.home.homefragment


import com.urbanfresh.appUtils.NetworkResponseCallback
import com.urbanfresh.data.local.AppPreference
import com.urbanfresh.ui.base.BaseViewModel
import com.urbanfresh.ui.home.homeResponse.HomeResponse


class HomeFragViewModel : BaseViewModel<HomeFragNavigator>() {

    fun homeInfoApi() {

        //navigator!!.showProgress()
        disposable.add(
            HomeResponse().doNetworkRequest(prepareRequestHashMap(),
                AppPreference,
                object : NetworkResponseCallback<HomeResponse> {
                    override fun onResponse(weather: HomeResponse) {
                       // navigator!!.hideProgress()

                        navigator!!.onHomeList(weather)

                    }

                    override fun onFailure(message: String) {
                        navigator!!.hideProgress()
                        //navigator!!.showNetworkError(navigator!!.getStringResource(R.string.http_some_other_error))
                    }

                    override fun onServerError(error: String) {
                        navigator!!.hideProgress()
                        /*if (error != "")
                            navigator!!.showNetworkError(error)
                        else
                            navigator!!.showNetworkError(navigator!!.getStringResource(R.string.http_some_other_error))*/
                    }

                    override fun onSessionExpire(error: String) {
                        navigator!!.hideProgress()
                    }

                    override fun onAppVersionUpdate(msg: String) {
                        navigator!!.hideProgress()

                    }

                    override fun onErrorMessage(erroeMessage: String) {
                        navigator!!.hideProgress()
                    }

                })
        )
    }

    var hashMap = HashMap<String, Any>()
    private fun prepareRequestHashMap(): java.util.HashMap<String, Any> {

        hashMap["categoty_id"] = "2"
        return hashMap
    }

}