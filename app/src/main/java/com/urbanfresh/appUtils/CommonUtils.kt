package com.urbanfresh.appUtils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log


import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {

    /*** method for email validation ***/

    const val FORMAT_1 = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_2 = "dd MMM yyyy hh:mm aaa"
    const val FORMAT_3 = "hh:mm"
    const val FORMAT_4 = "hh:mm aaa"
    const val FORMAT_5 = "yyyy-MM-dd"
    const val FORMAT_6 = "dd MMM yyyy"

    const val FORMAT_7 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FORMAT_8 = "dd-MMMM-yyyy"
    const val FORMAT_9 = "MMM dd, yyyy"
    const val FORMAT_UTC = "dd-MM-yyyy hh:mm a"

    fun isDateInBetweenIncludingEndPoints(min: Date, max: Date, date: Date): Boolean {
        return !(date.before(min) || date.after(max))
    }


    fun getDateFormat(dateInput: String, dateOutput: String, _date: String): String {
        return try {
            @SuppressLint("SimpleDateFormat") val inputFormat = SimpleDateFormat(dateInput, Locale.ENGLISH)
            @SuppressLint("SimpleDateFormat") val outputFormat = SimpleDateFormat(dateOutput, Locale.ENGLISH)
            val date = inputFormat.parse(_date)
            outputFormat.format(date!!)
        } catch (e: Exception) {
           Log.e("", "Response of Application exception in parsing are " + e.toString())
            ""
        }
    }

    /*** method for check network connection ***/

    fun isInternetOn(context: Context?): Boolean {
        if (context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            /*           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                           val nw = connectivityManager.activeNetwork ?: return false
                           val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

                           return when {
                               actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                               actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                               //for other device how are able to connect with Ethernet
                               actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                               //for check internet over Bluetooth
                               actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                               else -> false
                           }
                       } else {*/
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
            //}
        }
        return false


    }

    fun isStringDataNullOrBlank(str: String?): String {

        val valueToReturn = ""
        return if (str == null || str == "null") {
            valueToReturn
        } else {
            str
        }

    }

    fun clearAllActivity(context: Context, cls: Class<*>) {
        val intent = Intent(context, cls)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        (context as Activity).startActivity(intent)
        (context as Activity).finish()
    }

}

