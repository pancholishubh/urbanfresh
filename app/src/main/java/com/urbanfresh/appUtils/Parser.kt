package com.urbanfresh.appUtils

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

object Parser {

    @Throws(Exception::class)
    fun parse(response: Response<ResponseBody>?, parseCallBack: ParseCallBack) {
        if (response != null && response.isSuccessful && response.code() == 200) {
            val output = ServerResponseHandler.getResponseBody(response)
            val `object` = JSONObject(output!!)
            if (`object`.optString("success").equals("true", ignoreCase = true)) {
                parseCallBack.onSuccess()
            } else if (`object`.optString("success").equals("false", ignoreCase = true)) {
                val temp = `object`.getJSONObject("error")
                parseCallBack.onServerError(temp.optString("message"))
            }
        } else if (response != null) {
            if (response.code() == 400 || response.code() == 500 || response.code() == 404 || response.code() == 401) {
                when {
                    response.code() == 401 -> {
                        val output = ServerResponseHandler.getResponseBody(response)
                        val `object` = JSONObject(output!!)
                        val errorObject = `object`.getJSONObject("error")
                        parseCallBack.onSessionExpire(errorObject.optString("message"))
                    }
                    response.code() == 403 -> {
                        parseCallBack.onAppVersionUpdate(response.message())
                    }
                    else -> {
                        parseCallBack.onServerError(response.message())
                    }
                }
            } else {
                val responseStr = ServerResponseHandler.getResponseBody(response)
                val jsonObject = JSONObject(responseStr!!)
                parseCallBack.onServerError(ServerResponseHandler.checkJsonErrorBody(jsonObject))
            }
        }
    }

    fun parseErrorResponse(throwable: Throwable, networkResponseCallback: NetworkResponseCallback<*>) {
        if (throwable is HttpException) {

            try {
                val response = throwable.response()
                Log.e("RESS:- "," parse_response "+response.isSuccessful+" "+response.message())
                when {
                    response.code() == 401 -> {
                        networkResponseCallback.onSessionExpire(ServerResponseHandler.checkJsonErrorBody(JSONObject(response.errorBody()!!.string())))
                    }
                    response.code() == 403 -> {
                        networkResponseCallback.onAppVersionUpdate(ServerResponseHandler.checkJsonErrorBody(JSONObject(response.errorBody()!!.string())))
                    }
                    response.code() == 500 -> {
                        networkResponseCallback.onServerError(AppConstants.INTERNAL_SERVER_ERROR)
                    }
                    response.code() == 400 -> {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
//                        networkResponseCallback.onErrorMessage(ServerResponseHandler.getErrorCheck(jsonObject).toString() )
                            networkResponseCallback.onErrorMessage(jsonObject.getString("message"))

//                        networkResponseCallback.onErrorMessage(ServerResponseHandler.getErrorCheck(JSONObject(response.errorBody()!!.string())))
                    }
                    response.code() == 422 -> {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                         networkResponseCallback.onErrorMessage(ServerResponseHandler.getErrorCheck(jsonObject).toString() )
                     }
                    else -> {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        networkResponseCallback.onServerError(ServerResponseHandler.checkJsonErrorBody(jsonObject))
                    }
                }
            } catch (e: Exception) {

                networkResponseCallback.onDataNotFound()

            }

        } else {

            networkResponseCallback.onDataNotFound()
        }
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

        }

        return ""
    }


    fun getErrorMessageCase(error: Any, jObject: JSONObject): String {
        try {
            if (error is JSONObject) {
                if (jObject.has("error")) {
                    var errorJson: JSONObject = jObject.getJSONObject("error")
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
        }

        return ""
    }

    interface ParseCallBack {
        fun onSuccess()

        fun onSessionExpire(msg: String)

        fun onServerError(msg: String)

        fun onAppVersionUpdate(msg: String)
    }
}
