package com.oratakashi.covid19.ui.timeline

import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataTimeline
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import com.oratakashi.covid19.ui.sortirdialog.sort_timeline.SortTimelineFragment
import com.oratakashi.covid19.ui.timeline.TimelineState.Error
import com.oratakashi.covid19.ui.timeline.TimelineState.Loading
import com.oratakashi.covid19.ui.timeline.TimelineState.Result
import com.oratakashi.covid19.utils.Converter
import com.oratakashi.covid19.utils.Tmp
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_timeline.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TimelineActivity : DaggerAppCompatActivity(), SortDialogInterface {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    val data : MutableList<DataTimeline> = ArrayList()

    lateinit var adapter: TimelineAdapter

    val viewModel : TimelineViewModel by lazy {
        ViewModelProviders.of(this, viewmodelFactory).get(TimelineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        ButterKnife.bind(this)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_bg)))
        supportActionBar!!.title = "Timeline Indonesia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        adapter = TimelineAdapter(data, this)

        rvTimeline.layoutManager = LinearLayoutManager(this)
        rvTimeline.adapter = adapter

        srTimeline.setOnRefreshListener {
            Tmp.sort = "asc"
            etSearch.setText("")
            viewModel.getData()
        }

        etSearch.addTextChangedListener {
            if(etSearch.text.toString().isNotEmpty()){
                viewModel.getCache(Tmp.sort, Database.date, Converter.dateFormat(etSearch.text.toString()))
            }
        }

        setupViewModel()
    }

    fun setupViewModel(){

        viewModel.state.observe(this, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> srTimeline.isRefreshing = true
                    is Result -> {
                        srTimeline.isRefreshing = false
                        viewModel.cacheData(it.data)
                        viewModel.getCache()
                    }
                    is Error -> {
                        srTimeline.isRefreshing = false
                        viewModel.getCache()
                        Snackbar.make(srTimeline, "Gagal memuat data terbaru!", Snackbar.LENGTH_SHORT)
                            .setAction("Coba lagi"){
                                viewModel.getData()
                            }.show()
                        if(BuildConfig.DEBUG) Toast.makeText(applicationContext, it.error.message,
                            Toast.LENGTH_SHORT).show()
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

        viewModel.getData()
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
