package com.oratakashi.covid19.ui.hospital

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataHospital
import com.oratakashi.covid19.ui.hospital.HospitalState.Error
import com.oratakashi.covid19.ui.hospital.HospitalState.Loading
import com.oratakashi.covid19.ui.hospital.HospitalState.Result
import com.oratakashi.covid19.ui.hospital.dialog.HospitalDialogFragment
import com.oratakashi.covid19.ui.hospital.dialog.HospitalDialogInterface
import com.oratakashi.covid19.utils.Utility
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_hospital.*
import javax.inject.Inject

class HospitalActivity : DaggerAppCompatActivity(), HospitalInterface, HospitalDialogInterface {

    @Inject
    lateinit var viewmodelFactory : ViewModelProvider.Factory

    val data : MutableList<DataHospital> = ArrayList()

    val viewModel : HospitalViewModel by lazy {
        ViewModelProviders.of(this, viewmodelFactory).get(HospitalViewModel::class.java)
    }

    val adapter : HospitalAdapter by lazy {
        HospitalAdapter(data, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)

        ButterKnife.bind(this)

        supportActionBar!!.hide()

        rvHospital.adapter = adapter
        rvHospital.layoutManager = LinearLayoutManager(this)

        viewModel.state.observe(this, Observer { state ->
            state?.let{
                when(it){
                    is Loading  -> srHospital.isRefreshing = true
                    is Result   -> {
                        srHospital.isRefreshing = false

                        viewModel.cacheData(it.data.data)
                    }
                    is Error    -> {
                        srHospital.isRefreshing = false

                        viewModel.getCache()

                        Toast.makeText(applicationContext, "Gagal memuat data terbaru!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        viewModel.cache.observe(this, Observer { cache ->
            cache?.let{
                data.clear()
                data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        srHospital.setOnRefreshListener {
            viewModel.getData()
        }

        viewModel.setupInstantSearch(etSearch)
    }

    override fun onSelected(data: DataHospital) {
        HospitalDialogFragment.newInstance(data, this).show(supportFragmentManager, "dialog")
    }

    override fun onSelected(menu: String, data : DataHospital) {
        when(menu){
            "direction" -> {
                if(data.lat != null && data.lang != null){
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=${data.lat},${data.lang}")
                    )
                    if(intent.resolveActivity(packageManager)!=null){
                        startActivity(intent)
                    }else{
                        Toast.makeText(applicationContext, "Tidak ada aplikasi Map!", Toast.LENGTH_SHORT).show()
                    }
                }else Toast.makeText(applicationContext, "Lokasi tidak diketahui!",
                    Toast.LENGTH_SHORT).show()
            }
            "phone" -> {
                if(data.phone != null){
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${data.phone}")
                    startActivity(intent)
                }else Toast.makeText(applicationContext, "Nomor Telepon tidak diketahui!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.checkCache()){
            viewModel.getCache()
        }else{
            viewModel.getData()
        }
    }

    @OnClick(R.id.ivBack) fun onBack(){
        finish()
    }

    @OnClick(R.id.ivSearch) fun onSearchTrigger(){
        llHeader.visibility = View.GONE
        rlSearch.visibility = View.VISIBLE
        etSearch.requestFocus()
        Utility.showKeyboard(this)
    }

    @OnClick(R.id.ivCancel) fun onCancel(){
        llHeader.visibility = View.VISIBLE
        rlSearch.visibility = View.GONE
        etSearch.setText("")
        Utility.dismisKeyboard(this)
    }
}