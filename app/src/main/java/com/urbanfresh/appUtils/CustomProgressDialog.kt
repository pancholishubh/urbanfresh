package com.urbanfresh.appUtils


import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.urbanfresh.R


class CustomProgressDialog : Dialog {
    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, theme: Int) : super(context, theme)

    fun hideProgressDialog() {
        try {
            if (pDialog != null) {
                if (pDialog!!.isShowing)
                    pDialog!!.dismiss()
                pDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressDialog() {
        try {
            if (pDialog == null) {
               createProgressDialog(context)
                pDialog!!.setCanceledOnTouchOutside(false)
                pDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createProgressDialog(context: Context): CustomProgressDialog {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflator.inflate(R.layout.dialog_custom_progres, null)
        pDialog = CustomProgressDialog(context, R.style.CustomProgress)
        (pDialog as CustomProgressDialog).setContentView(view)

        return pDialog as CustomProgressDialog
    }

    companion object {
        private var pDialog: Dialog? = null
    }
}

