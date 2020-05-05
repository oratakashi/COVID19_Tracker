package com.oratakashi.covid19.ui.province

import android.content.Intent
import android.net.Uri
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
import com.oratakashi.covid19.data.model.localstorage.DataProvince
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.province.ProvinceState.Error
import com.oratakashi.covid19.ui.province.ProvinceState.Loading
import com.oratakashi.covid19.ui.province.ProvinceState.Result
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import com.oratakashi.covid19.ui.sortirdialog.sort_indonesia.SortLocalFragment
import com.oratakashi.covid19.ui.timeline.TimelineActivity
import com.oratakashi.covid19.utils.Converter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_province.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ProvinceFragment(val parent : MainInterfaces) : DaggerFragment(), SortDialogInterface {

    @Inject
    lateinit var viewmodelFactory : ViewModelProvider.Factory

    val data : MutableList<DataProvince> = ArrayList()

    val viewModel: ProvinceViewModel by lazy {
        ViewModelProviders.of(this, viewmodelFactory).get(ProvinceViewModel::class.java)
    }

    val adapter: ProvinceAdapter by lazy {
        ProvinceAdapter(data, parent, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_province, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        rvProvince.adapter = adapter
        rvProvince.layoutManager = LinearLayoutManager(context)

        viewModel.setupInstantSearch(etSearch)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> { //Handling Loading Statement
                        when(viewModel.checkData()){
                            true -> {
                                llLoading.visibility = View.GONE
                                llContent.visibility = View.VISIBLE
                                llMaintence.visibility = View.GONE

                                viewModel.getCache()
                                val result = viewModel.countData()

                                tvConfirmed.text = Converter.numberFormat(result.confirm!!)
                                tvRecovered.text = Converter.numberFormat(result.recovered!!)
                                tvDeath.text = Converter.numberFormat(result.death!!)
                            }
                            false -> {
                                llLoading.visibility = View.VISIBLE
                                llContent.visibility = View.GONE
                                llMaintence.visibility = View.GONE
                            }
                        }
                    }
                    is Result -> { //Handling Result Statement
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE
                        llMaintence.visibility = View.GONE

                        val result = viewModel.countData()

                        tvConfirmed.text = Converter.numberFormat(result.confirm!!)
                        tvRecovered.text = Converter.numberFormat(result.recovered!!)
                        tvDeath.text = Converter.numberFormat(result.death!!)

                        when(it.data.data.isNotEmpty()){
                            true -> {
                                parent.resultProvince(it.data.data)
                                viewModel.cacheData(it.data)
                                viewModel.getCache()
                            }
                            false -> {
                                rvProvince.visibility = View.VISIBLE
                                llMaintence.visibility = View.VISIBLE
                                tvServer.text = "Sumber data : covid19.bnpb.go.id"
                            }
                        }
                    }
                    is Error -> { //Handling Error Statement
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE
                        llMaintence.visibility = View.GONE

                        viewModel.getCache()

                        val result = viewModel.countData()

                        tvConfirmed.text = Converter.numberFormat(result.confirm!!)
                        tvRecovered.text = Converter.numberFormat(result.recovered!!)
                        tvDeath.text = Converter.numberFormat(result.death!!)

                        Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                            .setAction("Coba Lagi"){
                                viewModel.getData()
                            }

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        viewModel.cacheProvince.observe(viewLifecycleOwner, Observer { cache ->
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
            "asc_confirm" -> {
                viewModel.getCache("asc", Database.confirmed, etSearch.text.toString())
            }
            "desc_confirm" -> {
                viewModel.getCache("desc", Database.confirmed, etSearch.text.toString())
            }
            "asc_recovered" -> {
                viewModel.getCache("asc", Database.recovered, etSearch.text.toString())
            }
            "desc_recovered" -> {
                viewModel.getCache("desc", Database.recovered, etSearch.text.toString())
            }
            "asc_death" -> {
                viewModel.getCache("asc", Database.deaths, etSearch.text.toString())
            }
            "desc_death" -> {
                viewModel.getCache("desc", Database.deaths, etSearch.text.toString())
            }
            "abjad" -> {
                viewModel.getCache("asc", Database.provinceState, etSearch.text.toString())
            }
        }
    }

    @OnClick(R.id.btnCheck) fun onCheck(){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("https://bnpb-inacovid19.hub.arcgis.com/datasets/covid19-indonesia-per-provinsi/data?geometry=89.223%2C-9.881%2C148.593%2C5.442")
        startActivity(i)
    }

    @OnClick(R.id.ivShort) fun onSort(){
        SortLocalFragment.newInstance(this).show(childFragmentManager, "dialog")
    }

    @OnClick(R.id.ivTimeline) fun onTimeline(){
        startActivity(Intent(context, TimelineActivity::class.java))
    }
}
