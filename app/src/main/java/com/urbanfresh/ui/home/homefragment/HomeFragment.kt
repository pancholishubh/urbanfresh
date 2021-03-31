package com.urbanfresh.ui.home.homefragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.urbanfresh.BR
import com.urbanfresh.R
import com.urbanfresh.appUtils.AppConstants
import com.urbanfresh.data.model.AdsBanner
import com.urbanfresh.data.model.categorydata
import com.urbanfresh.databinding.FragmentHomeBinding
import com.urbanfresh.ui.base.BaseFragment
import com.urbanfresh.ui.home.homeResponse.HomeResponse
import com.urbanfresh.ui.home.homefragment.adapter.AdsAdapter
import com.urbanfresh.ui.home.homefragment.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragViewModel>(),
    HomeFragNavigator {


    private var categoryList: ArrayList<categorydata>? = ArrayList()
    private var adsList: ArrayList<AdsBanner>? = ArrayList()

    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    private var categoryAdapter: CategoryAdapter? = null
    private var adsAdapter: AdsAdapter? = null


    private lateinit var homeFragViewModel: HomeFragViewModel


    override val bindingVariable: Int
        get() = BR.homeVM

    override val layoutId: Int
        get() = R.layout.fragment_home

    override val viewModel: HomeFragViewModel
        get() {
            homeFragViewModel = ViewModelProvider(this).get(HomeFragViewModel::class.java)
            return homeFragViewModel
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragViewModel.navigator = this
        init()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onHomeList(homeResponse: HomeResponse) {
        categoryList!!.clear()
        adsList!!.clear()
        adsList!!.addAll(homeResponse.components!![2].AdsBanner)
        categoryList!!.addAll(homeResponse.components!![1].categorydata)
        categoryAdapter!!.notifyDataSetChanged()
        adsAdapter!!.notifyDataSetChanged()
        Log.e("res >>>>", "" + homeResponse.components!!)
        loadImage(
            viewDataBinding!!.staticBanner,
            AppConstants.BASE_URL + homeResponse.components!![0].StaticBanner[0].banner_image
        )
    }


    override fun init() {
        initCategoryAdapter()
        initAdsAdapter()
        if (checkIfInternetOn(tryAgainClick = { init() })) {
            homeFragViewModel.homeInfoApi()
        }
    }

    private fun initCategoryAdapter() {
        gridLayoutManager =
            GridLayoutManager(activity!!, 3, GridLayoutManager.VERTICAL, false)
        rv_category.layoutManager = gridLayoutManager
        categoryAdapter = CategoryAdapter(activity!!, categoryList!!)
        rv_category.adapter = categoryAdapter

    }

    private fun initAdsAdapter() {
        linearLayoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        rv_ads.layoutManager = linearLayoutManager
        adsAdapter = AdsAdapter(activity!!, adsList!!)
        rv_ads.adapter = adsAdapter

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

}