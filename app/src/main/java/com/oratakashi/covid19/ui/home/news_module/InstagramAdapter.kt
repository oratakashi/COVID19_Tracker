package com.oratakashi.covid19.ui.home.news_module

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.instagram.DataMedia
import com.oratakashi.covid19.utils.ImageHelper
import kotlinx.android.synthetic.main.adapter_instagram.view.*

class InstagramAdapter(val data : List<DataMedia>, val parent : InstagramInterface) : RecyclerView.Adapter<InstagramAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_instagram,
            parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHelper.getPicasso(holder.itemView.ivImage, data[position].thumbs!!)
        ImageHelper.getPicasso(holder.itemView.ivPhoto, data[position].user_photo!!)
        holder.itemView.tvName.text = data[position].username
        holder.itemView.cvInstagram.setOnClickListener {
            parent.onPostClick(data[position].link!!)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}