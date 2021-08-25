package com.sales.ssc.ui.returnItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.adapter.ReturnListAdapter
import com.sales.ssc.dialog.CustomAlertDialog
import com.sales.ssc.response.GetProducts
import com.sales.ssc.response.ProductReturnRequest
import com.sales.ssc.response.UserDetail
import com.sales.ssc.utils.Constant
import com.sales.ssc.utils.SharedPreferenceUtil
import com.sales.ssc.viewmodel.ReturnViewModel
import kotlinx.android.synthetic.main.fragment_return.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReturnFragment : Fragment(), ReturnListAdapter.SendSelectedType  {

    private lateinit var returnViewModel: ReturnViewModel
    private lateinit var returnList: ArrayList<GetProducts>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        returnViewModel = ViewModelProviders.of(activity!!).get(ReturnViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewReturnList.layoutManager = layoutManager

        val verticalDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.vertical_divider)
        verticalDecoration.setDrawable(verticalDivider!!)
        recyclerViewReturnList.addItemDecoration(verticalDecoration)

        returnList = ArrayList()
        val adapter = ReturnListAdapter(returnList, context!!, this)
        recyclerViewReturnList.adapter = adapter

        txtSearchProduct.setOnClickListener {

            Navigation.findNavController(it)
                .navigate(ReturnFragmentDirections.actionNavigationReturnToProductsSearchFragment())
        }

        returnViewModel.getProduct().observe(viewLifecycleOwner, Observer {

            returnList.clear()
            returnList.addAll(it)
            adapter.notifyDataSetChanged()
            if (returnList.isNotEmpty()) {
                btnReturnProduct.visibility = View.VISIBLE
            } else {
                btnReturnProduct.visibility = View.GONE
            }

        })

        btnReturnProduct.setOnClickListener {
            try {
                val customAlertDialog = CustomAlertDialog.newInstance(
                    context!!, getString(R.string.are_you_sure_want_to_return_product),
                    getString(R.string.yes), true, getString(R.string.no)
                )
                //customAlertDialog.showCustomerView()
                customAlertDialog.setOnAlertClickListener(object : CustomAlertDialog.ButtonClick {
                    override fun onPositiveButtonClick(userDetail: UserDetail) {

                        customAlertDialog.cancel()
                        //connectPrinter(customAlertDialog)
                        sendToServer(userDetail)

                    }

                    override fun onNegativeButtonClick() {
                        customAlertDialog.cancel()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendToServer(userDetail: UserDetail) {

        val productReturnRequest = ProductReturnRequest()
        var totalPrice: Double = 0.0
        val totalQuantity = returnList.size
        //val todayDate =dd
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)

        val timeFormat = SimpleDateFormat(Constant.TIME_FORMAT_SERVER, Locale.ENGLISH)
        val date = dateFormat.format(calendar.time)
        val time = timeFormat.format(calendar.time)
        val productList = ArrayList<ProductReturnRequest.Product>()
        for (products in returnList) {
            val product = ProductReturnRequest.Product()
            product.ProductCode = products.ProductCode!!
            product.SellingPrice = products.selectedPrice!!
            product.Quantity = products.selectedQuantity!!
            totalPrice += products.selectedPrice!!
            productList.add(product)
        }

        val userName = SharedPreferenceUtil(context!!).getString("username","")
        productReturnRequest.ProductList = productList
        productReturnRequest.ReturningDate = date
        productReturnRequest.ReturningTime = time
        productReturnRequest.TotalProductQuantity = totalQuantity
        productReturnRequest.TotalPrice = totalPrice
        productReturnRequest.CustomerName= getString(R.string.txtCash)
       // productCheckoutRequest.MobileNumber= userDetail.mobileNumber
       // productCheckoutRequest.City= userDetail.city
        productReturnRequest.SalesPerson= userName
      //  cartPresenter.returnProductCheckout(productReturnRequest)
        onReturnCheckout(productReturnRequest)

    }
    private fun onReturnCheckout(productReturnRequest: ProductReturnRequest) {
        returnViewModel.responseReturnCheckout(productReturnRequest)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseSscBody ->
                try {


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
    }

    override fun send(result: GetProducts) {
        returnViewModel.removeProduct(result)
    }

}
