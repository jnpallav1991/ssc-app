package com.sales.ssc.utils


import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import java.io.File


object Constant {
    const val NO_INTERNET = "No Internet, Please check your network connection."
    var IS_NETWORK_AVAILABLE: Boolean = true
    const val SERVER_NOT_RESPONDING = "Server not responding"

    const val JAVA_NET_EXCEPTION = "java.net.ConnectException"

    const val PDF_NAME = "print_output.pdf"

    const val DATE_FORMAT = "dd MMM yyyy"

    //const val DATE_FORMAT_SERVER = "MM/dd/yyyy"

    const val TIME_FORMAT_SERVER = "HH:mm"

    const val TIME_FORMAT_12 = "hh:mm aa"

    object SubUrl {

        const val INSERT_RETURN = "InsertReturn"

        const val INSERT_SALE_DETAILS = "InsertSaleDetails"

        const val INSERT_PRODUCT_MASTER = "InsertProductMaster"

        const val GET_PRODUCTS = "GetProducts"

        const val SCAN_PRODUCT = "GetProductDetailsByID"

        const val GET_SALES_PRODUCT_BY_ID = "GetSaleProductDetailsByID"

        const val GET_SALES_PRODUCT_BY_DATE = "GetSaleProductByDate"

        const val GET_SALES_RETURN_BY_DATE = "GetSaleReturnByDate"

        const val GET_SALES_RETURN_BY_ID = "GetSaleProductReturnDetailsByID"

        const val USER_LOGIN = "Login"
    }

    /**
     * This method is for showing Alert OR Error Dialog message of API Response
     *
     * @param context
     * @param title
     * @param msg
     */
    fun alertDialog(context: Context?, title: String, msg: String) {
        try {
            val dialogBuilder = AlertDialog.Builder(context!!)
            //val inflater = context.layoutInflater
            dialogBuilder.setCancelable(false)
            //val dialogView = inflater.inflate(R.layout.custom_alert_dialog, null)
            //dialogBuilder.setView(dialogView)

            //dialogBuilder.setTitle("Custom dialog")
            dialogBuilder.setMessage(msg)
            dialogBuilder.setPositiveButton("Ok", { dialog, whichButton ->
                dialog.dismiss()
            })

            val b = dialogBuilder.create()
            b.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openKeyBoard(context: Context,view: View)
    {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyBoard(context: Context,view: View)
    {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun openFile(context: Context, url: File) {
        // Create URI
        //Uri uri = Uri.fromFile(url);

        //val path = context.filesDir
       // val newFile = File(path, "123.pdf")
        val contentUri =
            FileProvider.getUriForFile(context, "com.sales.ssc.fileprovider", url)
        //val urlString: String = url.toString().toLowerCase()
        val intent = Intent(Intent.ACTION_VIEW)

        // PDF file
        intent.setDataAndType(contentUri, "application/pdf")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
}