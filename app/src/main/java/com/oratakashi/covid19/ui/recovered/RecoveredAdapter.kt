package com.oratakashi.covid19.ui.recovered

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataGlobal
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.utils.Converter
import kotlinx.android.synthetic.main.adapter_list.view.*

class RecoveredAdapter(
    val data : List<DataGlobal>, val parent : MainInterfaces, val context: Context
) : RecyclerView.Adapter<RecoveredAdapter.ViewHolder>() {

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
        holder.itemView.tvRecoveredPercent.visibility = View.VISIBLE
        holder.itemView.tvRecoveredPercent.text = Converter.persentase(
            data[position].recovered!!.toFloat(),
            data[position].confirmed!!.toFloat()
        )+" "+context.resources.getString(R.string.title_recovered)
        holder.itemView.tvDeathPercent.visibility = View.VISIBLE
        holder.itemView.tvDeathPercent.text = Converter.persentase(
            data[position].deaths!!.toFloat(),
            data[position].confirmed!!.toFloat()
        )+" "+context.resources.getString(R.string.title_deaths)
        holder.itemView.tvConfirmed.text = "${Converter.numberFormat(data[position].confirmed!!)} Orang"
        holder.itemView.tvRecovered.text = "${Converter.numberFormat(data[position].recovered!!)} Orang"
        holder.itemView.tvDeath.text = "${Converter.numberFormat(data[position].deaths!!)} Orang"
        holder.itemView.llAdapter.setOnClickListener {
            parent.getLocation(data[position].lat!!.toDouble(), data[position].long!!.toDouble())
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}