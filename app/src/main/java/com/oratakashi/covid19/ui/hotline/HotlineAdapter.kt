package com.oratakashi.covid19.ui.hotline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataHotline
import kotlinx.android.synthetic.main.adapter_hotline.view.*

class HotlineAdapter(val data : List<DataHotline>, val parent : HotlineInterface) : RecyclerView.Adapter<HotlineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvProvince.text = data[position].province
        holder.itemView.tvPhone.text = data[position].phone
        holder.itemView.cvHotline.setOnClickListener {
            parent.onPhone(data[position].phone)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}