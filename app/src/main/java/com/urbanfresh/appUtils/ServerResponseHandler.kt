package com.urbanfresh.appUtils

import androidx.annotation.StringRes
import com.urbanfresh.MyApplication
import com.urbanfresh.R

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader


object ServerResponseHandler {

    fun getString(@StringRes stringId: Int): String {
        return MyApplication.instance!!.getString(stringId)
    }

    fun getHttpCodeError(statusCode: Int): String {

        var error = "Error"

        when (statusCode) {

            400 -> error = getString(R.string.https_400_error)
            401 -> error = getString(R.string.http_401_error)
            403 -> error = getString(R.string.http_403_error)
            404 -> error = getString(R.string.http_404_error)
            500 -> error = getString(R.string.http_500_error)
        }
        return error
    }

    fun checkJsonErrorBody(jObject: JSONObject): String {
        try {
            if (jObject.has("error")) {
                val error = jObject.get("error")
                return getJsonErrorBody(error, jObject)
            } else {
                return jObject.optString("message")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    fun checkJsonSuccessBody(jObject: JSONObject): String {
        try {
            if (jObject.has("success")) {
                return jObject.optString("success")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    fun getResponseBody(response: Response<ResponseBody>): String? {

        var reader: BufferedReader? = null
        var output: String? = null
        try {

            if (response.body() != null) {
                reader = BufferedReader(InputStreamReader(response.body()!!.byteStream()))
            } else if (response.errorBody() != null) {
                reader = BufferedReader(InputStreamReader(response.errorBody()!!.byteStream()))
            }
            output = reader!!.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return output
    }

    fun getErrorCheck(jObject: JSONObject): String {

        try {
            if (jObject.has("error")) {
                val error = jObject.get("error")
                return getErrorMessageCase(error, jObject)
            } else {
                return jObject.optString("message")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    fun getErrorMessageCase(error: Any, jObject: JSONObject): String {
        try {
            if (error is JSONObject) {
                if (jObject.has("error"))
                {
                    var errorJson : JSONObject = jObject.getJSONObject("error")
                    return errorJson.getString("message")
                } else {
                    return jObject.getString("message")
                }
            } else if (error is JSONArray) {
                try {
                    return error.getJSONObject(0).optString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (je: Exception) {
            je.printStackTrace()
        }

        return ""
    }


    fun getJsonErrorBody(error: Any, jObject: JSONObject): String {
        try {

            if (error is JSONObject) {
                var message = ""

                if (error.has("message")) {
                    val msgObj = error.get("message")
                    if (msgObj is JSONObject) {
                        message = msgObj.optString("message")
                    }else if (msgObj is String){
                        message = msgObj
                    }
                }
                return message
            } else if (error is JSONArray) {

                try {
                    return error.getJSONObject(0).optString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        } catch (je: Exception) {
            je.printStackTrace()
        }

        return ""
    }
}
