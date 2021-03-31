package com.urbanfresh.appUtils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.urbanfresh.R
import java.util.*

object DialogConstant {


    /******************************/
    fun noInternetDialog(context: Context, tryAgainClick: () -> Unit?) {
        val dialogInternet = Dialog(context, R.style.MyApplicationDialog)
        dialogInternet.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogInternet.setCanceledOnTouchOutside(true)
        Objects.requireNonNull<Window>(dialogInternet.window).setBackgroundDrawableResource(R.color.colorWhite)
        dialogInternet.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        dialogInternet.setContentView(R.layout.no_internet_layout)
        dialogInternet.setCancelable(true)
        var noDataFound = dialogInternet.findViewById<LinearLayout>(R.id.noDataFound)
        var tryAgain = dialogInternet.findViewById<TextView>(R.id.tryAgain)

        noDataFound.setOnClickListener {
//            dialogInternet.dismiss()
        }
        tryAgain.setOnClickListener {
            tryAgainClick.invoke()
            dialogInternet.dismiss()
        }

        dialogInternet.show()
    }


}
