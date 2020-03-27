package com.oratakashi.covid19.ui.timeline

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataTimeline
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import com.oratakashi.covid19.ui.sortirdialog.sort_timeline.SortTimelineFragment
import com.oratakashi.covid19.utils.Converter
import com.oratakashi.covid19.utils.Tmp
import kotlinx.android.synthetic.main.activity_timeline.*
import kotlinx.android.synthetic.main.fragment_statistik.*
import java.util.*
import kotlin.collections.ArrayList

class TimelineActivity : AppCompatActivity(), SortDialogInterface {

    lateinit var adapter: TimelineAdapter
    lateinit var viewModel: TimelineViewModel

    val data : MutableList<DataTimeline> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        ButterKnife.bind(this)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_bg)))
        supportActionBar!!.title = "Timeline Indonesia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        adapter = TimelineAdapter(data, this)
        viewModel = ViewModelProviders.of(this).get(TimelineViewModel::class.java)

        rvTimeline.layoutManager = LinearLayoutManager(this)
        rvTimeline.adapter = adapter

        srTimeline.setOnRefreshListener {
            Tmp.sort = "asc"
            etSearch.setText("")
            viewModel.getTimeLine()
        }

        etSearch.addTextChangedListener {
            if(etSearch.text.toString().isNotEmpty()){
                viewModel.getCache(Tmp.sort, Database.date, Converter.dateFormat(etSearch.text.toString()))
            }
        }

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.errorTimeLine.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(srTimeline, "Gagal memuat data terbaru!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba lagi"){
                        viewModel.getTimeLine()
                    }.show()
            }
        })
        viewModel.progressTimeLine.observe(this, Observer { progress ->
            progress?.let{
                when(it){
                    true -> {
                        srTimeline.isRefreshing = true
                    }
                    false -> {
                        srTimeline.isRefreshing = false
                    }
                }
            }
        })
        viewModel.responseTimeLine.observe(this, Observer { response ->
            response?.let{
                if(it.isNotEmpty()){
                    rvTimeline.visibility = View.VISIBLE
                    llEmpty.visibility = View.GONE
                    data.clear()
                    data.addAll(it)
                    adapter.notifyDataSetChanged()
                }else{
                    rvTimeline.visibility = View.GONE
                    llEmpty.visibility = View.VISIBLE
                }

            }
        })

        viewModel.getTimeLine()
    }

    override fun onSort(option: String) {
        viewModel.getCache(option, Database.date, etSearch.text.toString())
        Tmp.sort = option
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    @OnClick(R.id.fab) fun onSort(){
        SortTimelineFragment.newInstance(this).show(supportFragmentManager, "dialog")
    }

    @OnClick(R.id.etSearch) fun onSearch(){
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            etSearch.setText("$dayOfMonth-"+ Converter.decimalFormat(month+1)+"-$year")
        },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
    }
}
