package com.oratakashi.covid19.ui.about

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.contributor.DataContributor
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), AboutInterface {
    val dataKontributor : MutableList<DataContributor> = ArrayList()

    val adapter : AboutAdapter by lazy {
        AboutAdapter(dataKontributor, this)
    }

    val viewModel : AboutViewModel by lazy {
        ViewModelProviders.of(this).get(AboutViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_bg)))
        supportActionBar!!.title = "Tentang Aplikasi"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        rvContributor.adapter = adapter
        rvContributor.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)

        tvVersion.text = BuildConfig.VERSION_NAME
        tvSource.text = BuildConfig.BASE_URL+"\nhttp://covid19.bnpb.go.id/"

        cvGithub.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://github.com/oratakashi/COVID19_Tracker")
            startActivity(i)
        }

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.dataAbout.observe(this, Observer { data ->
            data?.let{
                dataKontributor.clear()
                dataKontributor.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getContributor(this)
    }

    override fun onKontributor(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
