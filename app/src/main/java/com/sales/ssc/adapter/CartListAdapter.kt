package com.sales.ssc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.AppApplication
import com.sales.ssc.R
import com.sales.ssc.db.CartDetails
import kotlinx.android.synthetic.main.recyclerview_cart_list.view.*

class CartListAdapter(private val arrayList: ArrayList<CartDetails>, val sendSelectedType:SendSelectedType,private val context: Context) :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_cart_list, viewGroup, false
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

        fun bindItems(result: CartDetails, position: Int) {

            itemView.txtPName.text = result.pName
            val symbol = context.getString(R.string.Rs)
            itemView.txtPQuantity.text = "${AppApplication.appContext.getString(R.string.quantity_colon)} ${result.pQuantity}"
            itemView.txtPTPrice.text = symbol + result.pTotalPrice.toString()
            itemView.txtQuantity.text = result.pQuantity.toString()

            var quantity = result.pQuantity
            itemView.txtMinus.setOnClickListener {
                if (quantity!! > 1) {
                    quantity--
                    itemView.txtQuantity.text = quantity.toString()
                    result.pQuantity = quantity
                    sendSelectedType.send(result,position)
                } else {
                    result.pQuantity = null
                    sendSelectedType.send(result,position)
                }
            }

            itemView.txtPlus.setOnClickListener {
                if (quantity!! < result.pTQuantity!!) {
                    quantity++
                    itemView.txtQuantity.text = quantity.toString()
                    result.pQuantity= quantity
                    sendSelectedType.send(result,position)
                } else {

                }
            }

        }
    }

    fun addDoseHistory(doseHistoryList: List<CartDetails>?) {
        if (doseHistoryList != null) {
            arrayList.clear()
            arrayList.addAll(doseHistoryList)
            notifyDataSetChanged()
        }
    }

    interface SendSelectedType {
        fun send(cartDetails: CartDetails, position: Int)
    }
}