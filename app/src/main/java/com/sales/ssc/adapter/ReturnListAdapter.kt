package com.sales.ssc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.response.GetProducts
import kotlinx.android.synthetic.main.recyclerview_return_list.view.*

class ReturnListAdapter(
    private val arrayList: ArrayList<GetProducts>,
    val context: Context,
    val sendSelectedType: SendSelectedType
) :
    RecyclerView.Adapter<ReturnListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_return_list, viewGroup,
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

        fun bindItems(result: GetProducts, position: Int) {
            itemView.txtPName.text = "${result.ProductName}"
            itemView.txtPQuantity.text = result.selectedQuantity.toString()
            val symbol = context.getString(R.string.Rs)
            itemView.txtPTPrice.text = "$symbol${result.selectedPrice}"

            itemView.imgDelete.setOnClickListener {
                sendSelectedType.send(result)
            }
        }
    }

    interface SendSelectedType {
        fun send(result: GetProducts)
    }
}