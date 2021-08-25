package com.sales.ssc.ui.sales

import android.os.Bundle
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
import com.sales.ssc.adapter.SalesListAdapter
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.response.ResponseSscBody
import com.sales.ssc.utils.Constant
import com.sales.ssc.viewmodel.SalesViewModel
import kotlinx.android.synthetic.main.fragment_sales.*
import kotlinx.android.synthetic.main.progress_bar.*
import java.util.*

class SalesFragment : Fragment() {

    private lateinit var salesListAdapter: SalesListAdapter
    private lateinit var salesViewModel: SalesViewModel
    private var salesList: ArrayList<ResponseByDate>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        salesViewModel = ViewModelProviders.of(activity!!).get(SalesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        salesList = ArrayList()
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL

        val verticalDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.vertical_divider)
        verticalDecoration.setDrawable(verticalDivider!!)
        recyclerViewSales.addItemDecoration(verticalDecoration)
        recyclerViewSales.isNestedScrollingEnabled = false
        recyclerViewSales.layoutManager = layoutManager
        salesListAdapter = SalesListAdapter(salesList!!, false)

        recyclerViewSales.adapter = salesListAdapter

        salesViewModel.getSelectedDate().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            getSales(false)
        }
        )

        swipeViewSales.setOnRefreshListener {
            getSales(true)
        }
    }

    private fun getSales(callService:Boolean) {

        salesViewModel.getSalesResponse(callService)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseSscBody ->
                try {
                    if (responseSscBody != null) {

                        val type = object : TypeToken<ArrayList<ResponseByDate>>() {}.type
                        val l = Gson().toJson(responseSscBody.Data)
                        val sales = Gson().fromJson<ArrayList<ResponseByDate>>(l, type)

                        if (sales.isNotEmpty()) {
                            txtEmpty.visibility = View.GONE
                            recyclerViewSales.visibility = View.VISIBLE
                            salesList!!.clear()
                            salesList!!.addAll(sales)
                            salesListAdapter.notifyDataSetChanged()
                        } else {
                            txtEmpty.visibility = View.VISIBLE
                            recyclerViewSales.visibility = View.GONE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                swipeViewSales.isRefreshing= false
            })

    }
}
