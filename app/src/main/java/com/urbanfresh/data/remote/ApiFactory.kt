package com.urbanfresh.data.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.urbanfresh.appUtils.AppConstants
import com.urbanfresh.data.local.AppPreference
import com.urbanfresh.data.local.PreferenceKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiFactory {

    private var retrofitWithHeader: Retrofit? = null
    private var retrofit: Retrofit? = null


    /**
     * This method is used to call api with access_token in header.
     */
    //todo have to change this method
    val clientWithHeader: Retrofit
        get() {

            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .addHeader(
                        "access_token",
                        AppPreference.getValue(PreferenceKeys.ACCESS_TOKEN)!!
                    )
                    .addHeader("timezone", "Asia/Kolkata")

                val request = requestBuilder.build()
                chain.proceed(request)


            }
            httpClientBuilder.connectTimeout(5, TimeUnit.MINUTES)
            httpClientBuilder.readTimeout(5, TimeUnit.MINUTES)
            httpClientBuilder.writeTimeout(5, TimeUnit.MINUTES)
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)

            if (retrofitWithHeader == null) {
                val gson = GsonBuilder().setLenient().create()
                retrofitWithHeader = Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
            return this.retrofitWithHeader!!
        }


    /**
     * This method is used to call api without access_token in header.
     */
    fun getClientWithoutHeader(): Retrofit {

        val httpClientBuilder = OkHttpClient.Builder()

        httpClientBuilder.addInterceptor { chain ->

            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.addHeader("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        httpClientBuilder.connectTimeout(5, TimeUnit.MINUTES)
        httpClientBuilder.readTimeout(5, TimeUnit.MINUTES)
        httpClientBuilder.writeTimeout(5, TimeUnit.MINUTES)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)

        if (retrofit == null) {
            val gson = GsonBuilder().setLenient().create()
            retrofit = Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return this.retrofit!!
    }


}