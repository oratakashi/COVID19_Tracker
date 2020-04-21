package com.oratakashi.covid19.ui.statistik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.oratakashi.covid19.BuildConfig

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.statistik.StatistikState.Error
import com.oratakashi.covid19.ui.statistik.StatistikState.Loading
import com.oratakashi.covid19.ui.statistik.StatistikState.Result
import com.oratakashi.covid19.utils.Converter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_statistik.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class StatistikFragment(val parent : MainInterfaces) : DaggerFragment() {

    @Inject
    lateinit var viewmodelFactory : ViewModelProvider.Factory

    val viewModel: StatistikViewModel by lazy {
        ViewModelProviders.of(this, viewmodelFactory).get(StatistikViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> {
                        tvConfirmed.text = "Memuat data..."
                        tvRecovered.text = "Memuat data..."
                        tvDeath.text = "Memuat data..."
                    }
                    is Result -> {
                        tvConfirmed.text = Converter.numberFormat(it.data.confirmed!!.value)
                        tvRecovered.text = Converter.numberFormat(it.data.recovered!!.value)
                        tvRecoveredPercent.text = Converter.persentase(
                            it.data.recovered.value.toFloat(), it.data.confirmed.value.toFloat()
                        )
                        tvDeath.text = Converter.numberFormat(it.data.deaths!!.value)
                        tvDeathPercent.text = Converter.persentase(
                            it.data.deaths.value.toFloat(), it.data.confirmed.value.toFloat()
                        )

                        App.sessions!!.putInt(Sessions.last_confirmed, it.data.confirmed.value)
                        App.sessions!!.putInt(Sessions.last_recovered, it.data.recovered.value)
                        App.sessions!!.putInt(Sessions.last_death, it.data.deaths.value)

                        parent.resultStatistik(it.data)
                    }
                    is Error -> {
                        tvConfirmed.text = App.sessions!!.getInt(Sessions.last_confirmed).toString()
                        tvRecovered.text = App.sessions!!.getInt(Sessions.last_recovered).toString()
                        tvRecoveredPercent.text = Converter.persentase(
                            App.sessions!!.getInt(Sessions.last_recovered).toFloat(),
                            App.sessions!!.getInt(Sessions.last_confirmed).toFloat()
                        )
                        tvDeath.text = App.sessions!!.getInt(Sessions.last_death).toString()
                        tvDeathPercent.text = Converter.persentase(
                            App.sessions!!.getInt(Sessions.last_death).toFloat(),
                            App.sessions!!.getInt(Sessions.last_confirmed).toFloat()
                        )

                        Snackbar.make(clBase, "Gagal memuat data statistik!", Snackbar.LENGTH_SHORT)
                            .setAction("Coba Lagi"){
                                viewModel.getData()
                            }.show()

                        if(BuildConfig.DEBUG) Toast.makeText(context, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
//        viewModel.showMessage.observe(this, Observer { message ->
//            message?.let{
//                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//            }
//        })
//        viewModel.errorStatistik.observe(this, Observer { error ->
//            error?.let{
//                if(it) Snackbar.make(clBase, "Gagal memuat data statistik!", Snackbar.LENGTH_SHORT)
//                    .setAction("Coba Lagi"){
//                        viewModel.getStatistik()
//                    }.show()
//            }
//        })
//        viewModel.progressStatistik.observe(this, Observer { progress ->
//            progress?.let {
//                when(it){
//                    true -> {
//                        tvConfirmed.text = "Memuat data..."
//                        tvRecovered.text = "Memuat data..."
//                        tvDeath.text = "Memuat data..."
//                    }
//                    false -> {
//                        tvConfirmed.text = resources.getString(R.string.foo)
//                        tvRecovered.text = resources.getString(R.string.foo)
//                        tvDeath.text = resources.getString(R.string.foo)
//                    }
//                }
//            }
//        })
//        viewModel.responseStatistik.observe(this, Observer { response ->
//            response?.let{
//                tvConfirmed.text = it.confirmed!!.value.toString()+" Orang"
//                tvRecovered.text = it.recovered!!.value.toString()+" Orang"
//                tvDeath.text = it.deaths!!.value.toString()+" Orang"
//
//                parent.resultStatistik(it)
//            }
//        })

        viewModel.getData()
    }
}
