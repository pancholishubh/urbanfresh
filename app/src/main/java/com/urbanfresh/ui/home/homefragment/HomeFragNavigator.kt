package com.urbanfresh.ui.home.homefragment


import com.urbanfresh.appUtils.CommonNavigator
import com.urbanfresh.ui.home.homeResponse.HomeResponse


interface HomeFragNavigator : CommonNavigator {

    fun onHomeList(homeResponse: HomeResponse)


}