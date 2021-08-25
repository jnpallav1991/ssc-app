package com.sales.ssc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sales.ssc.R
import com.sales.ssc.response.Users
import kotlinx.android.synthetic.main.recyclerview_user_list.view.*

class UserListAdapter(
    private val arrayList: ArrayList<Users>,
    private val sendQuantityType: SendSelectedType
) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recyclerview_user_list, viewGroup,
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

        fun bindItems(result: Users, position: Int) {
            itemView.txtName.text = result.name
            itemView.txtEmail.text = result.email
        }
    }

    interface SendSelectedType {
        //fun send(bleDeviceData: Users, position: Int)
    }
}