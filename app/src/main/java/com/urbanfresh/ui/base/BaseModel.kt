package com.urbanfresh.ui.base


import com.urbanfresh.appUtils.NetworkResponseCallback
import com.urbanfresh.data.local.AppPreference
import java.io.Serializable
import java.util.*


abstract class BaseModel<S, T> : Serializable {
    abstract fun doNetworkRequest(requestParam: HashMap<S, T>,
                                  mFitnessPreference: AppPreference, networkResponseCallback: NetworkResponseCallback<*>
    )
}

