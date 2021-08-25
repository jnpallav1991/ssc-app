package com.sales.ssc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.databinding.RecyclerviewProductListBinding
import com.sales.ssc.response.GetProducts
import kotlinx.android.synthetic.main.recyclerview_product_list.view.*


class ProductListAdapter(
    private val arrayList: ArrayList<GetProducts>,
    private val sendQuantityType: SendSelectedType
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val binding: RecyclerviewProductListBinding =
            DataBindingUtil.inflate(inflater, R.layout.recyclerview_product_list, viewGroup, false)
        //val binding = RecyclerviewProductListBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position], position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(private val binding: RecyclerviewProductListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(result: GetProducts, position: Int) {

            binding.product = result
            binding.executePendingBindings()
            //itemView.txtPName.text = result.ProductName
            //itemView.txtPCode.text = result.ProductCode
            //itemView.txtPQuantity.text = result.Quantity.toString()
            //itemView.txtPPrice.text = result.SellingPrice.toString()
            itemView.ccProduct.setOnClickListener {
                sendQuantityType.send(result)
            }

        }
    }

    fun onClickListener()
    {

    }

    interface SendSelectedType {
        fun send(result: GetProducts)
    }
}