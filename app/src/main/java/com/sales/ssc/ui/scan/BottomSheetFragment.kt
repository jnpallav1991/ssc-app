package com.sales.ssc.ui.scan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sales.ssc.R
import com.sales.ssc.response.GetProducts
import com.sales.ssc.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var mListener: ItemClickListener
    private lateinit var cartViewModel: CartViewModel
    private var pQuantity = 1
    private var pTotalQuantity = 0

    companion object {
        fun newInstance(getProducts: GetProducts) = BottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable("data", getProducts)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cartDetail = arguments!!.getParcelable<GetProducts>("data")
        var dbProductQuantity = 0
        doAsync {

            if (cartDetail != null) {

                val cart = cartViewModel.getProductByCode(cartDetail.ProductCode!!)

                uiThread {
                    if (cart != null) {
                        dbProductQuantity = cart.pQuantity!!
                    }
                    txtProductTotalQuantity.text =
                        (cartDetail.Quantity!!.minus(dbProductQuantity)).toString()
                    if (cartDetail.Quantity != null) {
                        if (cartDetail.Quantity == 0) {
                            pTotalQuantity = 0
                            txtQuantity.setText("0")
                            textButton.isEnabled = false
                        } else {
                            txtQuantity.setText("1")
                            pTotalQuantity = (cartDetail.Quantity!!.minus(dbProductQuantity))
                            if (pTotalQuantity == 0) {
                                txtQuantity.setText("0")
                                textButton.isEnabled = false
                            } else {
                                cartDetail.selectedQuantity = 1
                            }
                        }
                    }
                }

            }
        }


        txtProductName.text = cartDetail!!.ProductName
        val symbol = context!!.getString(R.string.Rs)

        editProductPrice.setText(cartDetail.SellingPrice.toString())

        textButton.setOnClickListener {

            if (txtProductEdit.text == getString(R.string.txt_save))
            {
                Toast.makeText(context,getString(R.string.save_price_first),Toast.LENGTH_SHORT).show()
            }
            else if(txtQuantity.text.toString().toInt()>pTotalQuantity)
            {
                Toast.makeText(context,getString(R.string.quantity_grt_then_availabiltiy),Toast.LENGTH_SHORT).show()
            }
            else {
                cartDetail.selectedQuantity = txtQuantity.text.toString().toInt()
                cartDetail.SellingPrice = editProductPrice.text.toString().toDouble()
                mListener.onItemClick(cartDetail)
                dismiss()
            }
        }

        txtMinus.setOnClickListener {
            if (pQuantity > 1) {
                pQuantity--
                cartDetail.selectedQuantity = pQuantity
                txtQuantity.setText(pQuantity.toString())
            }
        }

        txtPlus.setOnClickListener {
            if (pQuantity < pTotalQuantity) {
                pQuantity++
                cartDetail.selectedQuantity = pQuantity
                txtQuantity.setText(pQuantity.toString())
            }
        }

        txtProductEdit.setOnClickListener {

            if (txtProductEdit.text == getString(R.string.txt_save)) {

                editProductPrice.isEnabled = false
                editProductPrice.isFocusable = false
                val imm: InputMethodManager? =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(it.windowToken, 0)
                txtProductEdit.text = getString(R.string.edit_price)

            } else {
                txtProductEdit.text = getString(R.string.txt_save)
                editProductPrice.isFocusable = true
                editProductPrice.isEnabled = true
                // editProductPrice.isCursorVisible = true
                editProductPrice.requestFocus()
                val imm: InputMethodManager? =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(editProductPrice, InputMethodManager.SHOW_IMPLICIT)
                editProductPrice.setSelection(editProductPrice.text!!.length)
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context as ItemClickListener
        } else {
            throw RuntimeException(
                "$context must implement ItemClickListener"
            )
        }

    }

    interface ItemClickListener {
        fun onItemClick(getProducts: GetProducts)
    }

}