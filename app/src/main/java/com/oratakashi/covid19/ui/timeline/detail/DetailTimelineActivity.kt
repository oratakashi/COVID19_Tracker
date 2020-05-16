package com.oratakashi.covid19.ui.timeline.detail

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataTimeline
import com.oratakashi.covid19.data.model.timeline.detail.DataDetailTimeline
import com.oratakashi.covid19.ui.timeline.detail.DetailTimelineState.Error
import com.oratakashi.covid19.ui.timeline.detail.DetailTimelineState.Loading
import com.oratakashi.covid19.ui.timeline.detail.DetailTimelineState.Result
import com.oratakashi.covid19.utils.Converter
import com.txusballesteros.widgets.FitChartValue
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_timeline.*
import javax.inject.Inject

class DetailTimelineActivity : DaggerAppCompatActivity() {

    val data : MutableList<DataDetailTimeline> = ArrayList()
    val value : MutableList<FitChartValue> = ArrayList()

    lateinit var data_parent : DataTimeline

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel : DetailTimelineViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailTimelineViewModel::class.java)
    }

    val adapter : DetailTimelineAdapter by lazy {
        DetailTimelineAdapter(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_timeline)
        
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_bg)))
        supportActionBar!!.title = Converter.dateFormatIndo(intent.getStringExtra("date"))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        rvTimeline.adapter = adapter
        rvTimeline.layoutManager = LinearLayoutManager(this)

        data_parent = intent.getParcelableExtra<DataTimeline>("data")!!

        chTimeline.minValue =  0f
        chTimeline.maxValue = data_parent.case!!.toFloat()
        value.add(
            FitChartValue(
                data_parent.recovered!!.toFloat(),
                ContextCompat.getColor(this, R.color.circle_blue)
            )
        )
        value.add(
            FitChartValue(
                data_parent.death!!.toFloat(),
                ContextCompat.getColor(this, R.color.circle_red)
            )
        )
        chTimeline.setValues(value)

        tvConfirmed.text =
            "${resources.getString(R.string.title_confirm)} : " +
            "${Converter.numberFormat(data_parent.case!!)} Orang"
        tvRecovered.text =
            "${resources.getString(R.string.title_recovered)} : " +
            "${Converter.numberFormat(data_parent.recovered!!)} Orang"
        tvDeath.text =
            "${resources.getString(R.string.title_deaths)} : " +
            "${Converter.numberFormat(data_parent.death!!)} Orang"

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.state.observe(this, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> {
                        llLoading.visibility = View.VISIBLE
                        llEmpty.visibility = View.GONE
                        llContent.visibility = View.GONE
                    }
                    is Result -> {
                        llLoading.visibility = View.GONE
                        llEmpty.visibility = View.GONE
                        llContent.visibility = View.VISIBLE

                        val province : MutableList<String> = ArrayList()

                        data.clear()

                        it.data.data.forEach {
                            if(!province.contains(it.attributes.Provinsi) && it.attributes.Provinsi != "Indonesia"){
                                province.add(it.attributes.Provinsi!!)
                                data.add(it)
                            }
                        }

                        if(data.isEmpty()){
                            llEmpty.visibility = View.VISIBLE
                            llContent.visibility = View.GONE
                        }

                        adapter.notifyDataSetChanged()
                    }
                    is Error -> {
                        llLoading.visibility = View.GONE
                        llEmpty.visibility = View.VISIBLE
                        llContent.visibility = View.GONE

                        Toast.makeText(applicationContext, "Gagal memuat data!", Toast.LENGTH_SHORT)
                            .show()

                        if(BuildConfig.DEBUG) Toast.makeText(applicationContext, it.error.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData(intent.getStringExtra("date"))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
