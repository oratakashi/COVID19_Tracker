package com.oratakashi.covid19.ui.death

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_death.*

/**
 * A simple [Fragment] subclass.
 */
class DeathFragment(val parent : MainInterfaces) : Fragment(), SortDialogInterface {

    lateinit var viewModel: DeathViewModel
    lateinit var adapter: DeathAdapter

    val data : MutableList<DataGlobal> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_death, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(DeathViewModel::class.java)
        adapter = DeathAdapter(data, parent, context!!)

        rvDeath.adapter = adapter
        rvDeath.layoutManager = LinearLayoutManager(context)

        viewModel.setupInstantSearch(etSearch)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.errorDeath.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getDeath()
                    }.show()
            }
        })
        viewModel.progressDeath.observe(this, Observer { progress ->
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
        viewModel.responseDeath.observe(this, Observer { response ->
            response?.let{
                parent.resultDeath(it)
            }
        })
        viewModel.cacheDeath.observe(this, Observer { cache ->
            cache?.let{
                data.clear()
                it.forEach {
                    data.add(it)
                }
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getDeath()
    }

    override fun onSort(option: String) {
        when(option){
            "asc" -> {
                viewModel.getCache(option, Database.deaths, etSearch.text.toString())
            }
            "desc" -> {
                viewModel.getCache(option, Database.deaths, etSearch.text.toString())
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
