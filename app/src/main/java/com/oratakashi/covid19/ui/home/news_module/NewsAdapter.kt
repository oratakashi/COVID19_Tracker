package com.oratakashi.covid19.ui.home.news_module

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.news.DataNews
import com.oratakashi.covid19.utils.Converter
import com.oratakashi.covid19.utils.ImageHelper
import kotlinx.android.synthetic.main.adapter_news.view.*
import java.util.*

class NewsAdapter(val data : List<DataNews>, val parent : NewsInterface) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_news,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHelper.getPicasso(holder.itemView.ivImage, data[position].image!!)
        holder.itemView.tvTitle.text = data[position].title
        holder.itemView.tvSource.text = data[position].source!!.toLowerCase(Locale.getDefault())
        holder.itemView.tvDate.text = Converter.dateFormatNews(data[position].date!!)
        holder.itemView.cvNews.setOnClickListener {
            parent.onNewsClick(data[position].link!!)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}