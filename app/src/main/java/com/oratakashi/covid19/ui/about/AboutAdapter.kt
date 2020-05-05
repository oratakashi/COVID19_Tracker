package com.oratakashi.covid19.ui.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.contributor.DataContributor
import com.oratakashi.covid19.utils.ImageHelper
import kotlinx.android.synthetic.main.adapter_contributor.view.*

class AboutAdapter(
    val data : List<DataContributor>, val parent : AboutInterface
) : RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contributor,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHelper.getPicasso(holder.itemView.ivPhoto, data[position].photo!!)
        holder.itemView.tvName.text = data[position].name
        holder.itemView.tvPosition.text = data[position].role
        holder.itemView.tvInstagram.text = data[position].instagram
        holder.itemView.cvContributor.setOnClickListener {
            parent.onKontributor(data[position].link!!)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}