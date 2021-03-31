package com.urbanfresh.data.model

class Components : ArrayList<ComponentsItem>()

data class ComponentsItem(
    val AdsBanner: List<AdsBanner>,
    val CarouselBanner: List<Any>,
    val StaticBanner: List<StaticBanner>,
    val categorydata: List<categorydata>,
    val name: String
)

data class StaticBanner(
    val banner_alt: String,
    val banner_description: String,
    val banner_id: Int,
    val banner_image: String,
    val banner_name: String,
    val url_id: Int,
    val url_type: String
)

data class categorydata(
    val category_description: String,
    val category_id: Int,
    val category_name: String,
    val category_picture: String
)

data class AdsBanner(
    val banner_alt: String,
    val banner_description: String,
    val banner_id: Int,
    val banner_image: String,
    val banner_name: String,
    val url_id: Int,
    val url_type: String
)