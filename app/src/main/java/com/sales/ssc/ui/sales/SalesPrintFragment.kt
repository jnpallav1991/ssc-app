package com.sales.ssc.ui.sales

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sales.ssc.R
import com.sales.ssc.activity.MainActivity
import com.sales.ssc.adapter.MyPrintDocumentAdapter
import com.sales.ssc.adapter.PrintSalesListAdapter
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.response.ResponseById
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.response.SearchId
import com.sales.ssc.viewmodel.PrintViewModel
import kotlinx.android.synthetic.main.fragment_sales_print.*


class SalesPrintFragment : Fragment() {

    private lateinit var doseEntryList: ArrayList<ResponseById>
    private lateinit var responseByDate: ResponseByDate
    private lateinit var adapter: PrintSalesListAdapter
    private lateinit var printViewModel: PrintViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            responseByDate = it.getParcelable("responseByDate")!!
        }

        printViewModel = ViewModelProvider(this).get(PrintViewModel::class.java)

        /* val callback: OnBackPressedCallback =
             object : OnBackPressedCallback(true *//* enabled by default *//*) {
                override fun handleOnBackPressed() {
                    navController.navigate(R.id.action_fragment_3_to_fragment_1)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_print, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewSalesPrint.layoutManager = layoutManager

        val verticalDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.vertical_divider)
        verticalDecoration.setDrawable(verticalDivider!!)
        recyclerViewSalesPrint.addItemDecoration(verticalDecoration)

        doseEntryList = ArrayList()
        adapter = PrintSalesListAdapter(doseEntryList, context!!)
        recyclerViewSalesPrint.adapter = adapter

        if (responseByDate.isSalesResponse!!) {
            txtPrintDate.text = responseByDate.printSellingDate()
            txtSalesPerson.text = responseByDate.SalesPerson
            printViewModel.getPrintSalesResponse(SearchId(responseByDate.ID.toString()))
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    printSales(it)
                }
                )
            //salesPresenter.searchById(SearchId(responseByDate.ID.toString()))
        } else {
            txtPrintDate.text = responseByDate.printReturningDate()
            txtSalesPerson.text = responseByDate.CollectionPerson
            printViewModel.getPrintReturnSalesResponse(SearchId(responseByDate.ID.toString()))
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    printSales(it)
                }
                )
            //salesPresenter.searchById(SearchId(responseByDate.ID.toString()))
        }

        txtCustomer.text = responseByDate.CustomerName
        txtCustomerMobile.text = responseByDate.MobileNumber
        txtCustomerCity.text = responseByDate.City


        btnPrint.setOnClickListener {

            try {
                val printManager = MainActivity.originalContext?.getSystemService(Context.PRINT_SERVICE) as PrintManager

                val jobName = this.getString(R.string.app_name) +
                        " Document"
                val attbuilder = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.NA_INDEX_4X6)
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
                    .build()
                printManager.print(
                    jobName,
                    MyPrintDocumentAdapter(MainActivity.originalContext!!, doseEntryList,responseByDate),
                    attbuilder
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // generatePdf()
    }

    private fun printSales(responseSscBody: ResponseSscBody?) {
        if (responseSscBody != null) {
            try {
                val type = object : TypeToken<java.util.ArrayList<ResponseById>>() {}.type
                val l = Gson().toJson(responseSscBody.Data)
                val sales = Gson().fromJson<java.util.ArrayList<ResponseById>>(l, type)
                doseEntryList.addAll(sales)
                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
