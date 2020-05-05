package com.oratakashi.covid19.ui.faq_menus.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.faq.DataFaq
import kotlinx.android.synthetic.main.adapter_faq.view.*

class FaqAdapter(val data : List<DataFaq>) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_faq,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvTitle.text = data[position].title
        holder.itemView.tvMessage.text = data[position].message
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}