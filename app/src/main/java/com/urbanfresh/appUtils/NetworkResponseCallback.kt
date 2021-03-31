package com.urbanfresh.appUtils

interface NetworkResponseCallback<T> {
    fun onResponse(`object`: T)

    fun onFailure(message: String)

    fun onServerError(error: String)

    fun onSessionExpire(error: String)

    fun onAppVersionUpdate(msg: String)

    fun navigateToOtp(msg: String) {}

    fun onErrorMessage (erroeMessage :String){}

    fun onDataNotFound (){}

}
