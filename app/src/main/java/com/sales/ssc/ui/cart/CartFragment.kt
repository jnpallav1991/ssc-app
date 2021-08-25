package com.sales.ssc.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sales.ssc.R
import com.sales.ssc.adapter.CartListAdapter
import com.sales.ssc.db.CartDetails
import com.sales.ssc.dialog.CustomAlertDialog
import com.sales.ssc.response.ProductCheckoutRequest
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.response.ResponseSuccessfulBody
import com.sales.ssc.response.UserDetail
import com.sales.ssc.services.interactor.CartInteractor
import com.sales.ssc.services.presenter.CartPresenter
import com.sales.ssc.services.view.CartView
import com.sales.ssc.viewmodel.CartViewModel
import com.sales.ssc.utils.Constant
import com.sales.ssc.utils.OnFragmentInteractionListener
import com.sales.ssc.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment(), CartListAdapter.SendSelectedType, CartView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var cartViewModel: CartViewModel
    private lateinit var productEntryList: ArrayList<CartDetails>
    private lateinit var cartPresenter: CartPresenter
    private var listener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        cartPresenter = CartInteractor(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity!!)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewCart.layoutManager = layoutManager

        val verticalDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.vertical_divider)
        verticalDecoration.setDrawable(verticalDivider!!)
        recyclerViewCart.addItemDecoration(verticalDecoration)

        productEntryList = ArrayList()
        val adapter = CartListAdapter(productEntryList, this, context!!)
        recyclerViewCart.adapter = adapter

        cartViewModel.getAllCart()!!.observe(viewLifecycleOwner,
            Observer<List<CartDetails>?> { doseHistoryList ->
                if (doseHistoryList!!.isEmpty()) {
                    txtEmptyCart.visibility = View.VISIBLE
                    group.visibility = View.GONE
                  //  listener!!.badgeCount(0)
                } else {
                    txtEmptyCart.visibility = View.GONE
                    group.visibility = View.VISIBLE
                    adapter.addDoseHistory(doseHistoryList)
                    getTotalAmount(doseHistoryList)
                  //  listener!!.badgeCount(doseHistoryList.size)
                }

            })

        checkoutButton.setOnClickListener {

            try {
                val customAlertDialog = CustomAlertDialog.newInstance(
                    context!!, getString(R.string.are_you_sure_want_to_checkout),
                    getString(R.string.yes), true, getString(R.string.no)
                )
                customAlertDialog.showCustomerView()
                customAlertDialog.setOnAlertClickListener(object : CustomAlertDialog.ButtonClick {
                    override fun onPositiveButtonClick(userDetail: UserDetail) {

                        sendToServer(userDetail)
                        customAlertDialog.cancel()
                        //connectPrinter(customAlertDialog)

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

        val productCheckoutRequest = ProductCheckoutRequest()
        var totalPrice: Double = 0.0
        val totalQuantity = productEntryList.size
        //val todayDate =dd
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH)

        val timeFormat = SimpleDateFormat(Constant.TIME_FORMAT_SERVER, Locale.ENGLISH)
        val date = dateFormat.format(calendar.time)
        val time = timeFormat.format(calendar.time)
        val productList = ArrayList<ProductCheckoutRequest.Product>()
        for (products in productEntryList) {
            val product = ProductCheckoutRequest.Product()
            product.ProductCode = products.pCode!!
            product.SellingPrice = products.pTotalPrice!!
            product.Quantity = products.pQuantity!!
            totalPrice += products.pTotalPrice!!
            productList.add(product)
        }

        val userName = SharedPreferenceUtil(context!!).getString("username","")
        productCheckoutRequest.ProductList = productList
        productCheckoutRequest.SellingDate = date
        productCheckoutRequest.SellingTime = time
        productCheckoutRequest.TotalProductQuantity = totalQuantity
        productCheckoutRequest.TotalPrice = totalPrice
        if (userDetail.customerName.equals(""))
        {
            userDetail.customerName = getString(R.string.txtCash)
        }
        productCheckoutRequest.CustomerName= userDetail.customerName
        productCheckoutRequest.MobileNumber= userDetail.mobileNumber
        productCheckoutRequest.City= userDetail.city
        productCheckoutRequest.SalesPerson= userName
        cartPresenter.cartCheckout(productCheckoutRequest)

    }

    private fun onItemClick(cartDetails: CartDetails) {

        doAsync {
            if (cartDetails.pCode != null) {

                val cart = cartViewModel.getProductByCode(cartDetails.pCode!!)
                val quantity = cartDetails.pQuantity
                if (cart != null) {
                    when (quantity) {
                        null -> {
                            cartViewModel.deleteProduct(cart.id!!)
                        }
                        else -> {
                            cart.pQuantity = quantity

                            cart.pTotalPrice = cart.pQuantity!! * cart.pPrice!!

                            cartViewModel.updateProduct(
                                cart.pTotalPrice!!,
                                cart.pQuantity!!,
                                cart.id!!
                            )
                        }
                    }
                }

            }
        }

    }

    private fun getTotalAmount(cartList: List<CartDetails>?) {
        var total = 0.0
        if (cartList != null) {
            for (cart in cartList) {
                total += cart.pTotalPrice!!
            }
        }
        val symbol = getString(R.string.Rs)
        txtTotal.text = "${getString(R.string.total)} $symbol$total"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun send(cartDetails: CartDetails, position: Int) {
        onItemClick(cartDetails)
    }

    override fun showProgress() {
        if (progress_bar != null) {
            progress_bar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        if (progress_bar != null) {
            progress_bar.visibility = View.GONE
        }
    }

    override fun onFailure(message: String) {
        if (activity != null) {
            Constant.alertDialog(activity!!, "", msg = message)
        }
    }

    override fun onResponseCheckoutSuccess(responseSuccessfulBody: ResponseSuccessfulBody?) {
        if (responseSuccessfulBody != null) {
            if (responseSuccessfulBody.StatusCode == 200) {
                doAsync {
                    cartViewModel.deleteAllProduct()

                    uiThread {

                        try {
                            val type = object : TypeToken<ResponseByDate>() {}.type
                            val l = Gson().toJson(responseSuccessfulBody.Data)
                            val sales = Gson().fromJson<ResponseByDate>(l, type)

                            sales.isSalesResponse=true
                            val countryFactBundle = Bundle().apply {
                                putParcelable("responseByDate", sales)
                            }
                            view?.findNavController()?.navigate(
                                R.id.action_navigation_cart_to_salesPrintFragment,
                                countryFactBundle
                            )


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

    }

    override fun onResponseReturnSuccess(responseSuccessfulBody: ResponseSuccessfulBody?) {
        TODO("Not yet implemented")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }
}
