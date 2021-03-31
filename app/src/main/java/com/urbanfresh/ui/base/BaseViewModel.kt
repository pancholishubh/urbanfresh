package com.urbanfresh.ui.base


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

import io.reactivex.disposables.CompositeDisposable



abstract class BaseViewModel<N : Any> : ViewModel() {


    private val isLoading = ObservableBoolean(false)
    protected val disposable = CompositeDisposable()

    var navigator: N? = null

    /**
     * This method is used to clear disposable.
     */
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    /**
     * This method is used to set loading
     * status observable boolean variable.
     */
    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }


}
