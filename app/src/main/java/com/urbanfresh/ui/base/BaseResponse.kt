package com.urbanfresh.ui.base

import com.urbanfresh.appUtils.NetworkResponseCallback
import io.reactivex.disposables.Disposable
import java.util.*


abstract class BaseResponse<T, K, V> {



    /**
     * This method is used to call api and handel response.
     *
     * @param HashMap contains values to send in api.
     * @param  vararg variable arguments can accept arbitrary number of values.
     * @param NetworkResponseCallback method which handel api response.
     */
    abstract fun doNetworkRequest(requestParam: HashMap<K, V>, vararg: Any, networkResponseCallback: NetworkResponseCallback<T>): Disposable
}
