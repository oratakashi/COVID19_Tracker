package com.oratakashi.covid19.ui.recovered

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataGlobal
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.sortirdialog.sort_global.SortDialogFragment
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import kotlinx.android.synthetic.main.fragment_recovered.*

/**
 * A simple [Fragment] subclass.
 */
class RecoveredFragment(val parent : MainInterfaces) : Fragment(), SortDialogInterface {

    lateinit var adapter: RecoveredAdapter
    lateinit var viewModel: RecoveredViewModel

    val data : MutableList<DataGlobal> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recovered, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(RecoveredViewModel::class.java)
        adapter = RecoveredAdapter(data, parent, context!!)

        rvRecovered.adapter = adapter
        rvRecovered.layoutManager = LinearLayoutManager(context)

        viewModel.setupInstantSearch(etSearch)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                Log.e("Debug", it)
            }
        })
        viewModel.errorRecovered.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getRecovered()
                    }.show()
            }
        })
        viewModel.progressRecovered.observe(this, Observer { progress ->
            progress?.let{
                when(it){
                    true -> {
                        llLoading.visibility = View.VISIBLE
                        llContent.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE
                    }
                }
            }
        })
        viewModel.responseRecovered.observe(this, Observer { response ->
            response?.let{
                parent.resultRecovered(it)
            }
        })
        viewModel.cacheRecovered.observe(this, Observer { cache ->
            cache?.let{
                data.clear()
                it.forEach {
                    data.add(it)
                }
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getRecovered()
    }

    override fun onSort(option: String) {
        when(option){
            "asc" -> {
                viewModel.getCache(option, Database.recovered, etSearch.text.toString())
            }
            "desc" -> {
                viewModel.getCache(option, Database.recovered, etSearch.text.toString())
            }
            "abjad" -> {
                viewModel.getCache("asc", Database.countryRegion, etSearch.text.toString())
            }
        }
    }

    @OnClick(R.id.ivShort) fun onSort(){
        SortDialogFragment.newInstance(this).show(childFragmentManager, "dialog")
    }
}
