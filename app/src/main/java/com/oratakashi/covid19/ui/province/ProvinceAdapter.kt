package com.oratakashi.covid19.ui.province

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataProvince
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.adapter_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class ProvinceAdapter(
    val data : List<DataProvince>, val parent : MainInterfaces, val context: Context
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
        holder.itemView.tvCountry.text = data[position].provinsi
        holder.itemView.tvConfirmed.text = "${context.resources.getString(R.string.title_confirm)} : ${data[position].confirm} Orang"
        holder.itemView.tvRecovered.text = "${context.resources.getString(R.string.title_recovered)} : ${data[position].recovered} Orang"
        holder.itemView.tvDeath.text = "${context.resources.getString(R.string.title_deaths)} : ${data[position].death} Orang"
//        holder.itemView.tvUpdate.visibility = View.VISIBLE

        val df = SimpleDateFormat("dd MMMM yyyy")

//        holder.itemView.tvUpdate.text = "Diperbarui : ${df.format(Calendar.getInstance().time)}"

        holder.itemView.llAdapter.setOnClickListener {
            parent.getLocation(
                data[position].lat.toDouble(),
                data[position].lang.toDouble(),
                8f
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}