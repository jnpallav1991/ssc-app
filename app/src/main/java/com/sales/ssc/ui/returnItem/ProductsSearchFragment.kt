package com.sales.ssc.ui.returnItem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sales.ssc.R
import com.sales.ssc.adapter.SearchProductAdapter
import com.sales.ssc.response.GetProducts
import com.sales.ssc.response.SearchProduct
import com.sales.ssc.utils.Constant
import com.sales.ssc.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsSearchFragment : Fragment(), SearchProductAdapter.SendSelectedType {

    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var productListAdapter: SearchProductAdapter
    private var productList: ArrayList<GetProducts>? = null

    private var isSearching = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsViewModel =
            ViewModelProviders.of(this).get(ProductsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*productsViewModel.text.observe(viewLifecycleOwner, Observer {
            text_dashboard.text = it
        })*/
        try {
            productList = ArrayList()
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = RecyclerView.VERTICAL

            val verticalDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.vertical_divider)
            verticalDecoration.setDrawable(verticalDivider!!)
            recyclerViewProductList.addItemDecoration(verticalDecoration)
            recyclerViewProductList.isNestedScrollingEnabled = false
            recyclerViewProductList.layoutManager = layoutManager
            productListAdapter = SearchProductAdapter(productList!!, this)

            recyclerViewProductList.adapter = productListAdapter

            edProductSearch.requestFocus()
            Constant.openKeyBoard(context!!, edProductSearch)
            edProductSearch.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {

                    if (isSearching) {
                        if (s.toString().isNotEmpty()) {
                            isSearching = false
                            onSearchProduct(SearchProduct(s.toString()))
                        } else {
                            clearList()
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                }
            })

            // getAllProductList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearList() {

        productList!!.clear()
        productListAdapter.notifyDataSetChanged()

    }

    private fun onSearchProduct(searchProduct: SearchProduct) {
        productsViewModel.searchProduct(searchProduct)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseSscBody ->
                try {
                    isSearching = true
                    val type = object : TypeToken<ArrayList<GetProducts>>() {}.type
                    val l = Gson().toJson(responseSscBody.Data)
                    val productList = Gson().fromJson<ArrayList<GetProducts>>(l, type)

                    if (productList.isNotEmpty()) {
                        txtEmpty.visibility = View.GONE
                        recyclerViewProductList.visibility = View.VISIBLE
                        this.productList!!.clear()
                        this.productList!!.addAll(productList)
                        productListAdapter.notifyDataSetChanged()

                    } else {
                        txtEmpty.visibility = View.VISIBLE
                        recyclerViewProductList.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
    }


    override fun send(result: GetProducts, position: Int) {

    }
}
