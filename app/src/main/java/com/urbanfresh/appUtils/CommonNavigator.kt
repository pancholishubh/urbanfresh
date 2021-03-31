package com.urbanfresh.appUtils


interface CommonNavigator {

    fun init()

    fun showProgress()

    fun hideProgress()

    fun showNetworkError(error:String,isRedirect : Boolean){}


    fun setRootViewVisibility()

    fun getStringResource(id: Int): String

    fun getIntegerResource(id: Int): Int

    fun setStatusBarColor() {}


    fun onBackClick()





















}
