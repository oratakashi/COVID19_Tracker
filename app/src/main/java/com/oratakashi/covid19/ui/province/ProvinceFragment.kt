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
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.fragment_province.*

/**
 * A simple [Fragment] subclass.
 */
class ProvinceFragment(val parent : MainInterfaces) : Fragment() {

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
        adapter = ProvinceAdapter(data, parent)

        rvProvince.adapter = adapter
        rvProvince.layoutManager = LinearLayoutManager(context)

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
                        rvProvince.visibility = View.GONE
                        llMaintence.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        rvProvince.visibility = View.VISIBLE
                        llMaintence.visibility = View.GONE
                    }
                }
            }
        })
        viewModel.responseProvince.observe(this, Observer { response ->
            response?.let{
                when(it.status.isMaintence){
                    true -> {
                        rvProvince.visibility = View.GONE
                        llMaintence.visibility = View.VISIBLE
                        tvServer.text = "Sumber data : covid19.bnpb.go.id"
                    }
                    false -> {
                        data.clear()
                        it.data.forEach {
                            if(it.attributes.provinsi != null && it.attributes.provinsi != "Indonesia"){
                                data.add(it)
                            }
                        }
                        adapter.notifyDataSetChanged()
                        parent.resultProvince(it.data)
                    }
                }
            }
        })
        viewModel.getData()
    }

    @OnClick(R.id.btnCheck) fun onCheck(){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("https://bnpb-inacovid19.hub.arcgis.com/datasets/covid19-indonesia-per-provinsi/data?geometry=89.223%2C-9.881%2C148.593%2C5.442")
        startActivity(i)
    }
}
