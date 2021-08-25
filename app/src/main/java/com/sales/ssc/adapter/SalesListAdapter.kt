package com.sales.ssc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.ui.sales.SalesNewFragmentDirections
import kotlinx.android.synthetic.main.recyclerview_sales_list.view.*

class SalesListAdapter(
    private val arrayList: ArrayList<ResponseByDate>,
    private val isReturn: Boolean
    //private val sendQuantityType: SendSelectedType
) :
    RecyclerView.Adapter<SalesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_sales_list, viewGroup,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position], position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(result: ResponseByDate, position: Int) {
            if (isReturn) {
                itemView.txtDate.text = result.printReturningDate()
                result.isSalesResponse = false
                itemView.txtSalesName.text = result.CollectionPerson

            } else {
                itemView.txtDate.text = result.printSellingDate()
                result.isSalesResponse = true
                itemView.txtSalesName.text = result.SalesPerson
            }

            itemView.txtPQuantity.text = result.TotalProductQuantity.toString()
            itemView.txtPPrice.text = result.TotalPrice.toString()
            if (result.CustomerName == "") {
                itemView.groupCustomer.visibility = View.GONE
            } else {
                itemView.groupCustomer.visibility = View.VISIBLE
                itemView.txtCustomerName.text = result.CustomerName
            }



            itemView.ccSales.setOnClickListener {

                Navigation.findNavController(it)
                    .navigate(
                        SalesNewFragmentDirections.actionNavigationSalesToSalesPrintFragment(
                            result
                        )
                    )

            }
        }
    }

    interface SendSelectedType {
        fun send(result: ResponseByDate, position: Int)
    }
}