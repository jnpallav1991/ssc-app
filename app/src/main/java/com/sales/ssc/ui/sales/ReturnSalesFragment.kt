package com.sales.ssc.ui.sales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sales.ssc.R
import com.sales.ssc.adapter.SalesListAdapter
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.viewmodel.SalesViewModel
import kotlinx.android.synthetic.main.fragment_return_sales.*
import java.util.ArrayList


class ReturnSalesFragment : Fragment() {

    private lateinit var salesListAdapter: SalesListAdapter
    private lateinit var salesViewModel: SalesViewModel
    private var salesList: ArrayList<ResponseByDate>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        salesViewModel = ViewModelProviders.of(activity!!).get(SalesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        salesList = ArrayList()
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL

        val verticalDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(activity!!, R.drawable.vertical_divider)
        verticalDecoration.setDrawable(verticalDivider!!)
        recyclerViewReturnSales.addItemDecoration(verticalDecoration)
        recyclerViewReturnSales.isNestedScrollingEnabled = false
        recyclerViewReturnSales.layoutManager = layoutManager
        salesListAdapter = SalesListAdapter(salesList!!,true)

        recyclerViewReturnSales.adapter = salesListAdapter

        salesViewModel.getSelectedDate().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            getReturnSales(false)
        }
        )

        swipeViewReturn.setOnRefreshListener {
            getReturnSales(true)
        }
    }

    private fun getReturnSales(callService:Boolean)
    {
        salesViewModel.getReturnSalesResponse(callService)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseSscBody ->
                try {
                    if (responseSscBody != null) {

                        val type = object : TypeToken<ArrayList<ResponseByDate>>() {}.type
                        val l = Gson().toJson(responseSscBody.Data)
                        val sales = Gson().fromJson<ArrayList<ResponseByDate>>(l, type)

                        if (sales.isNotEmpty()) {
                            txtEmpty.visibility = View.GONE
                            recyclerViewReturnSales.visibility = View.VISIBLE
                            salesList!!.clear()
                            salesList!!.addAll(sales)
                            salesListAdapter.notifyDataSetChanged()
                        } else {
                            txtEmpty.visibility = View.VISIBLE
                            recyclerViewReturnSales.visibility = View.GONE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                swipeViewReturn.isRefreshing=false
            })

    }

}
