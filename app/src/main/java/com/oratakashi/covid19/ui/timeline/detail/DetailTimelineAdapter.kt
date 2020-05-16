package com.oratakashi.covid19.ui.timeline.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.timeline.detail.DataDetailTimeline
import com.oratakashi.covid19.utils.Converter
import kotlinx.android.synthetic.main.adapter_list.view.*

class DetailTimelineAdapter(val data : List<DataDetailTimeline>) : RecyclerView.Adapter<DetailTimelineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvCountry.text = data[position].attributes.Provinsi
        holder.itemView.tvConfirmed.text = Converter.numberFormat(data[position].attributes.confirm!!)+" Orang"
        holder.itemView.tvRecovered.text = Converter.numberFormat(data[position].attributes.recovered!!)+" Orang"
        holder.itemView.tvDeath.text = Converter.numberFormat(data[position].attributes.death!!)+" Orang"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}