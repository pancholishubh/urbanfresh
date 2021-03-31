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
import com.urbanfresh.data.model.AdsBanner
import com.urbanfresh.databinding.RowItemAdsBinding
import com.urbanfresh.ui.base.BaseRecyclerAdapter

class AdsAdapter(
    private val context: Context, private var adsBannerList: ArrayList<AdsBanner>
) :
    BaseRecyclerAdapter<RowItemAdsBinding, Any, AdsAdapter.GetStaredViewHolder>() {


    override fun onCreateViewHolder(
        viewDataBinding: RowItemAdsBinding,
        parent: ViewGroup,
        viewType: Int
    ): GetStaredViewHolder {
        return GetStaredViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: GetStaredViewHolder, position: Int, type: Int) {
        holder.bindToDataVM(holder.bindingVariable, holder.viewModel)
        holder.bindTo(position)

        try {
            loadImage(
                holder.viewDataBinding.adsIV,
                AppConstants.BASE_URL + adsBannerList[position].banner_image
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
        return R.layout.row_item_ads
    }

    override fun getItemCount(): Int {
        return adsBannerList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class GetStaredViewHolder(mViewDataBinding: RowItemAdsBinding) :
        BaseViewHolder(mViewDataBinding) {


        override val viewModel: Any
            get() = Any()


        override val bindingVariable: Int
            get() = 0

    }


}