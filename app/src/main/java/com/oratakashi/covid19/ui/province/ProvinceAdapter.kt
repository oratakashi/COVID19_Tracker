package com.oratakashi.covid19.ui.province

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.adapter_list.view.*

class ProvinceAdapter(
    val data : List<DataProvince>, val parent : MainInterfaces
) : RecyclerView.Adapter<ProvinceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvCountry.text = data[position].attributes.provinsi
        holder.itemView.tvConfirmed.text = "Confirmed : ${data[position].attributes.confirm} Orang"
        holder.itemView.tvRecovered.text = "Recovered : ${data[position].attributes.recovered} Orang"
        holder.itemView.tvDeath.text = "Deaths : ${data[position].attributes.death} Orang"
        holder.itemView.tvUpdate.visibility = View.VISIBLE
        holder.itemView.tvUpdate.text = "Diperbarui : ${data[position].attributes.updates}"

        holder.itemView.llAdapter.setOnClickListener {
            parent.getLocation(
                data[position].geometry.lang.toDouble(),
                data[position].geometry.lat.toDouble(),
                8f
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}