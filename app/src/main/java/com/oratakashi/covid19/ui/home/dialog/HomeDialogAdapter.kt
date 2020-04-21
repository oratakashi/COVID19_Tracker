package com.oratakashi.covid19.ui.home.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.dashboard.DataLocal
import com.oratakashi.covid19.utils.Converter
import kotlinx.android.synthetic.main.adapter_list.view.*

class HomeDialogAdapter(
    val data : List<DataLocal>, val parent : HomeDialogInterface.adapter, val context: Context,
    val all : DataLocal?
) : RecyclerView.Adapter<HomeDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvCountry.text = data[position].name
        if(all != null){
            holder.itemView.tvConfirmed.text =
                "${Converter.persentase(data[position].confirm!!.toFloat(), all.confirm!!.toFloat())}"
            holder.itemView.tvRecovered.text =
                "${Converter.persentase(data[position].recovered!!.toFloat(), all.confirm!!.toFloat())}"
            holder.itemView.tvDeath.text =
                "${Converter.persentase(data[position].death!!.toFloat(), all.confirm!!.toFloat())}"
        }
        holder.itemView.llAdapter.setOnClickListener {
            parent.onSelected(data[position].name)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}