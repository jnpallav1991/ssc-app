package com.sales.ssc.ui.returnItem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sales.ssc.R
import com.sales.ssc.response.GetProducts
import com.sales.ssc.viewmodel.ReturnViewModel
import kotlinx.android.synthetic.main.fragment_bottom_return_product_dialog.*


class BottomProductReturnFragment : BottomSheetDialogFragment() {
    private lateinit var getProducts: GetProducts
    private lateinit var returnViewModel: ReturnViewModel
    private var pQuantity = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        returnViewModel = ViewModelProviders.of(activity!!).get(ReturnViewModel::class.java)
        arguments?.let {
            getProducts = it.getParcelable("getProducts")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_bottom_return_product_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        txtProductName.text = getProducts.ProductName
        editProductPrice.setText(getProducts.SellingPrice.toString())

        getProducts.selectedQuantity = 1

        btnReturn.setOnClickListener {

            if (txtProductEdit.text == getString(R.string.txt_save)) {
                Toast.makeText(context, getString(R.string.save_price_first), Toast.LENGTH_SHORT).show()
            } else {
                getProducts.SellingPrice = editProductPrice.text.toString().toDouble()
                //mListener.onItemClick(getProducts)
                returnViewModel.setProduct(getProducts)
                //dismiss()
                NavHostFragment.findNavController(this).navigate(BottomProductReturnFragmentDirections.actionBottomProductReturnFragmentToNavigationReturn())
            }
        }

        txtMinus.setOnClickListener {
            if (pQuantity > 1) {
                pQuantity--
                getProducts.selectedQuantity = pQuantity
                txtQuantity.text = pQuantity.toString()
            }
        }

        txtPlus.setOnClickListener {

            pQuantity++
            getProducts.selectedQuantity = pQuantity
            txtQuantity.text = pQuantity.toString()

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
}