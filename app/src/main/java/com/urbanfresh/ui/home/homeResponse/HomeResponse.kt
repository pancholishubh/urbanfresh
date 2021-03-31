package com.urbanfresh.ui.home.homeResponse

import Api
import com.urbanfresh.appUtils.NetworkResponseCallback
import com.urbanfresh.appUtils.Parser
import com.urbanfresh.data.model.Components
import com.urbanfresh.data.remote.ApiFactory
import com.urbanfresh.ui.base.BaseResponse

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class HomeResponse : BaseResponse<HomeResponse, String, Any>() {

    var components: Components? = null

    override fun doNetworkRequest(
        requestParam: HashMap<String, Any>,
        vararg: Any,
        networkResponseCallback: NetworkResponseCallback<HomeResponse>
    ): Disposable {
        val api = ApiFactory.getClientWithoutHeader().create(Api::class.java)


        return api.getWeather(requestParam["categoty_id"].toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ networkResponseCallback.onResponse(it) },
                { throwable -> Parser.parseErrorResponse(throwable, networkResponseCallback) })
    }


}
