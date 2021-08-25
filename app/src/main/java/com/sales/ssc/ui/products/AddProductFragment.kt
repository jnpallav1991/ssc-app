package com.sales.ssc.ui.products

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.sales.ssc.R
import com.sales.ssc.response.InsertProduct
import com.sales.ssc.utils.Constant
import com.sales.ssc.utils.DecimalDigitsInputFilter
import com.sales.ssc.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_addproduct.*
import java.text.SimpleDateFormat
import java.util.*


class AddProductFragment : Fragment() {


    private lateinit var cal: Calendar

    private lateinit var productsViewModel: ProductsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsViewModel =
            ViewModelProviders.of(this).get(ProductsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addproduct, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edPurchasePrice.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(7, 2))
        edSellingPrice.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(7, 2))
        edPurchaseDate.inputType = InputType.TYPE_NULL
        cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val selectedDate =
                    SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).format(cal.timeInMillis)
                edPurchaseDate.setText(selectedDate.toString())

            }
        edPurchaseDate.setOnClickListener {
            DatePickerDialog(
                context!!, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnAddProduct.setOnClickListener {

            if (checkValidation()) {
                val insertProduct = InsertProduct()
                insertProduct.ProductCode = edProductCode.text.toString()
                insertProduct.ProductName = edProductName.text.toString()
                insertProduct.PurchaseDate = edPurchaseDate.text.toString()
                insertProduct.PurchasePrice = edPurchasePrice.text.toString().toDouble()
                insertProduct.SellingPrice = edSellingPrice.text.toString().toDouble()
                insertProduct.Quantity = edQuantity.text.toString().toInt()
                insertProductMaster(insertProduct)
            }
        }
    }

    private fun checkValidation(): Boolean {
        when {
            edProductCode.text.isNullOrEmpty() -> {
                Toast.makeText(context,getString(R.string.toast_product_code),Toast.LENGTH_SHORT).show()
                return false
            }
            edProductName.text.isNullOrEmpty() -> {
                Toast.makeText(context,getString(R.string.toast_product_name),Toast.LENGTH_SHORT).show()
                return false
            }
            edPurchaseDate.text.isNullOrEmpty() -> {
                Toast.makeText(context,getString(R.string.toast_purchase_date),Toast.LENGTH_SHORT).show()
                return false
            }
            edPurchasePrice.text.isNullOrEmpty() -> {
                Toast.makeText(context,getString(R.string.toast_purchase_price),Toast.LENGTH_SHORT).show()
                return false
            }
            edSellingPrice.text.isNullOrEmpty() -> {
                Toast.makeText(context,getString(R.string.toast_selling_price),Toast.LENGTH_SHORT).show()
                return false
            }
            edQuantity.text.isNullOrEmpty() -> {
                Toast.makeText(context,getString(R.string.toast_quantity),Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }


    private fun insertProductMaster(insertProduct: InsertProduct) {

        productsViewModel.insertProductMaster(insertProduct)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseSscBody ->
                try {

                    if (responseSscBody != null) {
                        //val type = object : TypeToken<InsertProduct>() {}.type
                        //val l = Gson().toJson(responseSscBody.Data)
                        //val insertProduct = Gson().fromJson<InsertProduct>(l, type)
                        if (responseSscBody.Message == "SUCCESS") {
                            NavHostFragment.findNavController(this)
                                .navigate(AddProductFragmentDirections.actionAddProductFragmentToNavigationProduct())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
    }
}
