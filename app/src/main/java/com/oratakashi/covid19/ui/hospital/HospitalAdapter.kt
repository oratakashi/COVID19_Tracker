package com.oratakashi.covid19.ui.hospital

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataHospital
import kotlinx.android.synthetic.main.adapter_hospital.view.*

class HospitalAdapter(val data : List<DataHospital>, val parent : HospitalInterface) : RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hospital,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvName.text = data[position].name
        holder.itemView.tvLocation.text = data[position].address
        if(data[position].phone == null){
            holder.itemView.tvPhone.text = "Tidak diketahui"
        }else{
            holder.itemView.tvPhone.text = data[position].phone
        }
        holder.itemView.setOnClickListener {
            parent.onSelected(data[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}