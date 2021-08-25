package com.sales.ssc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.response.GetProducts
import com.sales.ssc.ui.returnItem.ProductsSearchFragmentDirections

import kotlinx.android.synthetic.main.recyclerview_search_product.view.*

class SearchProductAdapter(
    private val arrayList: ArrayList<GetProducts>,
    private val sendQuantityType: SendSelectedType
) :
    RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_search_product, viewGroup,
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
            itemView.txtPName.text = result.ProductName
            itemView.txtPCode.text = result.ProductCode

            itemView.searchCC.setOnClickListener {

                Navigation.findNavController(it)
                    .navigate(ProductsSearchFragmentDirections.actionProductsSearchFragmentToBottomProductReturnFragment(result))
            }
        }
    }

    interface SendSelectedType {
        fun send(result: GetProducts, position: Int)
    }
}