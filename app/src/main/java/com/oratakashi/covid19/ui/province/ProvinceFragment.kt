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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.localstorage.DataProvince
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import com.oratakashi.covid19.ui.sortirdialog.SortLocalFragment
import kotlinx.android.synthetic.main.fragment_province.*

/**
 * A simple [Fragment] subclass.
 */
class ProvinceFragment(val parent : MainInterfaces) : Fragment(), SortDialogInterface {

    lateinit var viewModel: ProvinceViewModel
    lateinit var adapter: ProvinceAdapter

    val data : MutableList<DataProvince> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_province, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(ProvinceViewModel::class.java)
        adapter = ProvinceAdapter(data, parent, context!!)

        rvProvince.adapter = adapter
        rvProvince.layoutManager = LinearLayoutManager(context)

        viewModel.setupInstantSearch(etSearch)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.errorProvince.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getData()
                    }
            }
        })
        viewModel.progressProvince.observe(this, Observer { progress ->
            progress?.let{
                when(it){
                    true -> {
                        llLoading.visibility = View.VISIBLE
                        llContent.visibility = View.GONE
                        llMaintence.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE
                        llMaintence.visibility = View.GONE
                    }
                }
            }
        })
        viewModel.responseProvince.observe(this, Observer { response ->
            response?.let{
                when(it.data.isEmpty()){
                    true -> {
                        rvProvince.visibility = View.VISIBLE
                        llMaintence.visibility = View.VISIBLE
                        tvServer.text = "Sumber data : covid19.bnpb.go.id"
                    }
                    false -> {
                        parent.resultProvince(it.data)
                    }
                }
            }
        })
        viewModel.cacheProvince.observe(this, Observer { cache ->
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
}
