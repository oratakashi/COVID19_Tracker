package com.oratakashi.covid19.ui.home.dialog

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.dashboard.DataLocal
import com.oratakashi.covid19.utils.Converter
import kotlinx.android.synthetic.main.fragment_home_dialog.*

class HomeDialogFragment : BottomSheetDialogFragment(), HomeDialogInterface.adapter {

    lateinit var viewModel: HomeDialogViewModel

    val data : MutableList<DataLocal> = ArrayList()

    lateinit var adapter : HomeDialogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(HomeDialogViewModel::class.java)
        adapter = HomeDialogAdapter(data, this, context!!, viewModel.countData())

        rvProvince.adapter = adapter
        rvProvince.layoutManager = LinearLayoutManager(context)

        setupViewModel()
    }

    fun setupViewModel() {
        viewModel.count.observe(this, Observer { count ->
            count?.let {
                tvRecovered.text = "${resources.getString(R.string.title_recovered)} : ${it.recovered!!} Orang | " +
                        "${Converter.persentase(it.recovered.toFloat(), it.confirm!!.toFloat())}"
                tvDeath.text = "${resources.getString(R.string.title_deaths)} : ${it.death!!} Orang | " +
                        "${Converter.persentase(it.death!!.toFloat(), it.confirm!!.toFloat())}"
            }
        })
        viewModel.dataProvince.observe(this, Observer { province ->
            province?.let{
                data.clear(); data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.countData()
        viewModel.getData()
    }

    override fun onSelected(province: String) {
        parent.onSelected(province)
        dismiss()
    }

    @OnClick(R.id.rlIndonesia) fun onIndonesia(){
        parent.onSelected()
        dismiss()
    }

    companion object {
        lateinit var parent : HomeDialogInterface
        fun newInstance(interfaces : HomeDialogInterface): HomeDialogFragment =
            HomeDialogFragment().apply {
                parent = interfaces
            }

    }
}
