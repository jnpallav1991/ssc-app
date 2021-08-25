package com.sales.ssc.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sales.ssc.R
import com.sales.ssc.response.UserDetail
import kotlinx.android.synthetic.main.custom_alert_dialog.*
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*

class
CustomAlertDialog(context: Context, check: Boolean) : AlertDialog(context) {

    private val TAG = CustomAlertDialog::class.java.simpleName

    private var buttonClick: ButtonClick? = null

    /**
     * Creating and returns single instance
     *
     * @return
     */
    companion object {
        private var message: String? = null

        private var positiveButtonTxt: String? = null

        private var negativeButtonTxt: String? = null

        private var isNegativeButton: Boolean = false

        fun newInstance(
            context: Context, msg: String, positiveButtonText: String,
            negativeButton: Boolean, negativeButtonText: String
        ): CustomAlertDialog {
            message = msg
            positiveButtonTxt = positiveButtonText
            negativeButtonTxt = negativeButtonText
            isNegativeButton = negativeButton
            val alertDialog = CustomAlertDialog(context)

            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return alertDialog
        }
    }

    // ALLOWS YOU TO SET LISTENER && INVOKE THE OVERIDING METHOD
    // FROM WITHIN ACTIVITY
    fun setOnAlertClickListener(buttonClick: ButtonClick) {
        this.buttonClick = buttonClick
    }

    fun showCustomerView() {
        groupCustomer.visibility = View.VISIBLE
    }

    interface ButtonClick {
        fun onPositiveButtonClick(userDetail: UserDetail)

        fun onNegativeButtonClick()
    }

    constructor(context: Context) : this(context, true) {
        //setContentView(R.layout.custom_alert_dialog);
        // Get the layout inflater
        val inflater = layoutInflater
        // Inflate and set the layout for the dialog
        val view = inflater.inflate(R.layout.custom_alert_dialog, null)
        setView(view)
        setCancelable(false)


        if (isNegativeButton) {
            view.negativeButton!!.visibility = View.VISIBLE
        } else {
            view.negativeButton!!.visibility = View.GONE
        }

        view.tvMsg!!.text = message
        view.positiveButton!!.text = positiveButtonTxt
        view.negativeButton!!.text = negativeButtonTxt

        try {
            view.positiveButton!!.setOnClickListener {
                //dismiss()
                val userDetail = UserDetail()
                userDetail.customerName = edCustomerName.text.toString().trim()
                userDetail.mobileNumber =edCustomerNumber.text.toString()
                userDetail.city = edCustomerCity.text.toString().trim()
                buttonClick!!.onPositiveButtonClick(userDetail)
            }

            view.negativeButton!!.setOnClickListener {
                //dismiss()
                buttonClick!!.onNegativeButtonClick()
            }

            view.imageClose!!.setOnClickListener {
                //dismiss()
                buttonClick!!.onNegativeButtonClick()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        show()
    }

}