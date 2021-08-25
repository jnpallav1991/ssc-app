package com.sales.ssc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.response.ResponseByDate
import com.sales.ssc.response.ResponseById
import kotlinx.android.synthetic.main.recyclerview_print_list.view.*

class PrintSalesListAdapter(
    private val arrayList: ArrayList<ResponseById>,
    val context: Context
) :
    RecyclerView.Adapter<PrintSalesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_print_list, viewGroup,
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

        fun bindItems(result: ResponseById, position: Int) {
            itemView.txtPName.text = "${result.ProductName}"
            itemView.txtPQuantity.text = result.Quantity.toString()
            val symbol = context.getString(R.string.Rs)
            itemView.txtPTPrice.text = "$symbol${result.Price}"
        }
    }

    interface SendSelectedType {
        fun send(result: ResponseByDate, position: Int)
    }
}