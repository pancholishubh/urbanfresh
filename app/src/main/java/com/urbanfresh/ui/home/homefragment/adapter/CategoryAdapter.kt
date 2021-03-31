package com.urbanfresh.ui.home.homefragment.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.urbanfresh.R
import com.urbanfresh.appUtils.AppConstants
import com.urbanfresh.data.model.categorydata
import com.urbanfresh.databinding.RowItemCategoryBinding
import com.urbanfresh.ui.base.BaseRecyclerAdapter

class CategoryAdapter(
    private val context: Context, private var categoryList: ArrayList<categorydata>
) :
    BaseRecyclerAdapter<RowItemCategoryBinding, Any, CategoryAdapter.GetStaredViewHolder>() {


    override fun onCreateViewHolder(
        viewDataBinding: RowItemCategoryBinding,
        parent: ViewGroup,
        viewType: Int
    ): GetStaredViewHolder {
        return GetStaredViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: GetStaredViewHolder, position: Int, type: Int) {
        holder.bindToDataVM(holder.bindingVariable, holder.viewModel)
        holder.bindTo(position)

        try {
            holder.viewDataBinding.categoryName.text = categoryList[position].category_name
            loadImage(
                holder.viewDataBinding.categoryIV,
                AppConstants.BASE_URL + categoryList[position].category_picture
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    private fun loadImage(view: ImageView, imageUrl: String) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.splash_bg)
            .error(R.mipmap.splash_bg)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()

        Glide.with(view.context)
            .load(imageUrl).apply(options)
            .into(view)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_item_category
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class GetStaredViewHolder(mViewDataBinding: RowItemCategoryBinding) :
        BaseViewHolder(mViewDataBinding) {


        override val viewModel: Any
            get() = Any()


        override val bindingVariable: Int
            get() = 0

    }


}