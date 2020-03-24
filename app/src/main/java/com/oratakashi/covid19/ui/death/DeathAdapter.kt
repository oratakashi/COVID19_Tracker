package com.oratakashi.covid19.ui.death

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.localstorage.DataGlobal
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.adapter_list.view.*

class DeathAdapter(
    val data : List<DataGlobal>,
    val parent : MainInterfaces,
    val context: Context
) : RecyclerView.Adapter<DeathAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvCountry.text = when(data[position].provinceState!=null){
            true -> {
                "${data[position].provinceState}, ${data[position].countryRegion}"
            }
            false -> {
                "${data[position].countryRegion}"
            }
        }
        holder.itemView.tvConfirmed.text = "${context.resources.getString(R.string.title_confirm)} : ${data[position].confirmed} Orang"
        holder.itemView.tvRecovered.text = "${context.resources.getString(R.string.title_recovered)} : ${data[position].recovered} Orang"
        holder.itemView.tvDeath.text = "${context.resources.getString(R.string.title_deaths)} : ${data[position].deaths} Orang"
        holder.itemView.llAdapter.setOnClickListener {
            parent.getLocation(data[position].lat!!.toDouble(), data[position].long!!.toDouble())
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}