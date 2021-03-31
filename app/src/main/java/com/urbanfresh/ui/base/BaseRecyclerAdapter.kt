package com.urbanfresh.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerAdapter<T : ViewDataBinding, V : Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {



    /**
     * This method is called when adapter created and is used to initialize
     * your view holder and returning the layout id resource.
     *
     * @param ViewGroup parent view group into which the view to be inserted.
     * @param viewType get actual layout id.
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(createDataBinding(parent, viewType), parent, viewType)
    }


    /**
     * This method is used for bind view to the adapter
     * this is where we will pass our data to our ViewHolder.
     *
     * @param holder view holder to update view which is bind with holder.
     * @param position position of the item to bind to this View.
     *
     */
    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position, 1)
    }


    /**
     * This method is used for bind view to the adapter
     * this is where we will pass our data to our ViewHolder.
     *
     * @param holder view holder to update view which is bind with holder.
     * @param position position of the item to bind to this View.
     * @param type get actual layout id.
     *
     */
    abstract fun onBindViewHolder(holder: VH, position: Int, type: Int)


    /**
     * This method is called when adapter created and is used to initialize
     * your view holder and returning the layout id resource.
     *
     * @param onCreateViewHolder binding data with view.
     * @param parent parent view group into which the view to be inserted.
     * @param viewType get actual layout id.
     *
     */
    abstract fun onCreateViewHolder(viewDataBinding: T, parent: ViewGroup, viewType: Int): VH


    /**
     * This method is used to get view layout.
     *
     * @param viewType get actual layout id.
     * @return Int view layout id.
     */
    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int


    /**
     * This method is used to get view layout.
     *
     * @param parent parent view group into which the view to be inserted.
     * @param viewType get actual layout id.
     * @return T any type view holder object can return.
     */
    private fun createDataBinding(parent: ViewGroup, viewType: Int): T {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayoutId(viewType), parent, false)
    }

    abstract inner class BaseViewHolder(mViewDataBinding: T) : RecyclerView.ViewHolder(mViewDataBinding.root) {
        var viewDataBinding: T
            internal set

        abstract val bindingVariable: Int
        abstract val viewModel: V

        init {
            this.viewDataBinding = mViewDataBinding
        }

        open fun bindTo(position: Int) {}

        fun bindToDataVM(vm: Int, objecct: Any) {
            viewDataBinding.setVariable(vm, objecct)
            viewDataBinding.executePendingBindings()
        }
    }



    /**
     * This method is used to find click on layout item.
     *
     * @param position view item position which clicked.
     */
    fun onItemClicked(position: Int) {}

    /**
     * This method is used to remove view layout.
     *
     * @param position layout item position which want to remove.
     */
    fun onItemRemoved(position: Int) {}

    /**
     * This method is used to get detail of particular item layout.
     *
     * @param position layout item position.
     */
    fun onItemDetail(position: Int) {}

    /**
     * This method is used to cancelled.
     *
     * @param position layout item position.
     */
    fun onItemCancelled(position: Int) {}

    /**
     * This method is used refresh item.
     *
     * @param position layout item position.
     */
    fun onItemRefreshed(position: Int) {}

    /**
     * This method is used for get event on item.
     *
     * @param vararg any type value.
     */
    fun onItemEvent(vararg event: Any) {}
}