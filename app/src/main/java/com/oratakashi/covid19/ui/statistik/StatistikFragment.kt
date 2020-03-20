package com.oratakashi.covid19.ui.statistik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

import com.oratakashi.covid19.R
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.fragment_statistik.*

/**
 * A simple [Fragment] subclass.
 */
class StatistikFragment(val parent : MainInterfaces) : Fragment() {

    lateinit var viewModel: StatistikViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(StatistikViewModel::class.java)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.errorStatistik.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data statistik!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getStatistik()
                    }.show()
            }
        })
        viewModel.progressStatistik.observe(this, Observer { progress ->
            progress?.let {
                when(it){
                    true -> {
                        tvConfirmed.text = "Memuat data..."
                        tvRecovered.text = "Memuat data..."
                        tvDeath.text = "Memuat data..."
                    }
                    false -> {
                        tvConfirmed.text = resources.getString(R.string.foo)
                        tvRecovered.text = resources.getString(R.string.foo)
                        tvDeath.text = resources.getString(R.string.foo)
                    }
                }
            }
        })
        viewModel.responseStatistik.observe(this, Observer { response ->
            response?.let{
                tvConfirmed.text = it.confirmed!!.value.toString()+" Orang"
                tvRecovered.text = it.recovered!!.value.toString()+" Orang"
                tvDeath.text = it.deaths!!.value.toString()+" Orang"

                parent.resultStatistik(it)
            }
        })

        viewModel.getStatistik()
    }
}
