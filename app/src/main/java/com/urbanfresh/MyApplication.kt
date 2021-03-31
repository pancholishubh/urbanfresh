package com.urbanfresh

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.urbanfresh.data.local.AppPreference
import com.urbanfresh.data.local.LoginPreference

import org.json.JSONArray
import org.json.JSONObject


class MyApplication : Application() {

    private var instance: MyApplication? = null
    lateinit var mContext: Context


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppPreference.getInstance(this)
        LoginPreference.getInstance(this)

    }

    companion object {
        @get:Synchronized
        var instance: MyApplication? = MyApplication()
            private set
    }




    /************************* For Socket Chat End ***********************/

    lateinit var chatListener: ChatListener
    fun registerListner(chatListener: ChatListener) {
        this.chatListener = chatListener
    }


    /*****************************/

    /**
     * This class is used to chat listener
     */
    interface ChatListener {

        fun getChatResponse(jsonObject: JSONObject){}
        fun chatCount(jsonObject: JSONArray, count: Int){}
    }

}
