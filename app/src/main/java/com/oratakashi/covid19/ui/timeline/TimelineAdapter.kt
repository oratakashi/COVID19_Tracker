package com.oratakashi.covid19.ui.timeline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataTimeline
import com.oratakashi.covid19.utils.Tmp
import com.oratakashi.covid19.utils.VectorDrawableUtils
import kotlinx.android.synthetic.main.adapter_timeline.view.*

class TimelineAdapter(val data : List<DataTimeline>, val context: Context) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_timeline,
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(Tmp.sort){
            "asc" -> {
                if(position == (data.size - 1)){
                    setMarker(holder, R.drawable.ic_marker_active, R.color.red)
                }else{
                    setMarker(holder, R.drawable.ic_marker_inactive, R.color.red)
                }
            }
            "desc" -> {
                if(position == 0){
                    setMarker(holder, R.drawable.ic_marker_active, R.color.red)
                }else{
                    setMarker(holder, R.drawable.ic_marker_inactive, R.color.red)
                }
            }
        }

        holder.itemView.tvDate.text = data[position].date
        holder.itemView.tvCase.text =
            "${context.resources.getString(R.string.title_case)} : ${data[position].case}"
        holder.itemView.tvConfirmed.text =
            "${context.resources.getString(R.string.title_confirm)} : ${data[position].confirm}"
        holder.itemView.tvRecovered.text =
            "${context.resources.getString(R.string.title_recovered)} : ${data[position].recovered}"
        holder.itemView.tvDeath.text =
            "${context.resources.getString(R.string.title_deaths)} : ${data[position].death}"
    }

    private fun setMarker(holder: ViewHolder, drawableResId: Int, colorFilter: Int) {
        holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, drawableResId, ContextCompat.getColor(holder.itemView.context, colorFilter))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeline : TimelineView = itemView.findViewById(R.id.timeline) as TimelineView
        init {
            timeline.initLine(itemViewType)
        }
    }
}