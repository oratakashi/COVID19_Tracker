package com.oratakashi.covid19.ui.death

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.data.model.localstorage.DataGlobal
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.ui.death.DeathState.Error
import com.oratakashi.covid19.ui.death.DeathState.Loading
import com.oratakashi.covid19.ui.death.DeathState.Result
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.sortirdialog.sort_global.SortDialogFragment
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import com.oratakashi.covid19.utils.Converter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_death.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class DeathFragment(val parent : MainInterfaces) : DaggerFragment(), SortDialogInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var adapter: DeathAdapter

    val viewModel : DeathViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DeathViewModel::class.java)
    }

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

        adapter = DeathAdapter(data, parent, requireContext())

        when(App.sessions!!.getInt(Sessions.last_confirmed)){
            0 -> {
                Toast.makeText(context, "Jumlah data belum tersedia!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                tvConfirmed.text = Converter.numberFormat(App.sessions!!.getInt(Sessions.last_confirmed))
                tvRecovered.text = Converter.numberFormat(App.sessions!!.getInt(Sessions.last_recovered))
                tvDeath.text = Converter.numberFormat(App.sessions!!.getInt(Sessions.last_death))
            }
        }

        rvDeath.adapter = adapter
        rvDeath.layoutManager = LinearLayoutManager(context)

        viewModel.setupInstantSearch(etSearch)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> {
                        llLoading.visibility = View.VISIBLE
                        llContent.visibility = View.GONE
                    }
                    is Result -> {
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE

                        viewModel.cacheData(it.data)
                        viewModel.getCache()

                        parent.resultDeath(it.data)
                    }
                    is Error -> {
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE

                        viewModel.getCache()

                        Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                            .setAction("Coba Lagi"){
                                viewModel.getData()
                            }.show()

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.cacheDeath.observe(viewLifecycleOwner, Observer { cache ->
            cache?.let{
                data.clear()
                it.forEach {
                    data.add(it)
                }
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getData()
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
