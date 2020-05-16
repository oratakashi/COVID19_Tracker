package com.oratakashi.covid19.ui.timeline

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.oratakashi.covid19.ui.timeline.detail.DetailTimelineActivity
import com.oratakashi.covid19.utils.Converter
import com.oratakashi.covid19.utils.Tmp
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_timeline.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class TimelineFragment : DaggerFragment(), SortDialogInterface, TimelineInterface {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    val data : MutableList<DataTimeline> = ArrayList()

    lateinit var adapter: TimelineAdapter

    val viewModel : TimelineViewModel by lazy {
        ViewModelProviders.of(this, viewmodelFactory).get(TimelineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        tvDate.text = "Last Updated : ${SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID")).format(Date())}"

        adapter = TimelineAdapter(data, requireContext(), this)

        rvTimeline.layoutManager = LinearLayoutManager(requireContext())
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

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is TimelineState.Loading -> srTimeline.isRefreshing = true
                    is TimelineState.Result -> {
                        srTimeline.isRefreshing = false
                        viewModel.cacheData(it.data)
                        Tmp.sort = "desc"
                        viewModel.getCache("desc", Database.date)
                    }
                    is TimelineState.Error -> {
                        srTimeline.isRefreshing = false
                        viewModel.getCache()
                        Snackbar.make(srTimeline, "Gagal memuat data terbaru!", Snackbar.LENGTH_SHORT)
                            .setAction("Coba lagi"){
                                viewModel.getData()
                            }.show()
                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.responseTimeLine.observe(viewLifecycleOwner, Observer { response ->
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

    override fun onSelect(data :DataTimeline) {
        val intent = Intent(requireContext(), DetailTimelineActivity::class.java)
        intent.putExtra("date", data.date)
        intent.putExtra("data", data)
        startActivity(intent)
    }

    @OnClick(R.id.fab) fun onSort(){
        SortTimelineFragment.newInstance(this).show(childFragmentManager, "dialog")
    }

    @OnClick(R.id.etSearch) fun onSearch(){
        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            etSearch.setText("$dayOfMonth-"+ Converter.decimalFormat(month+1)+"-$year")
        },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
    }
}
