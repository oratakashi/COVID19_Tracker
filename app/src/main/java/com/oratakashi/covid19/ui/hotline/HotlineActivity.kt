package com.oratakashi.covid19.ui.hotline


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.localstorage.DataHotline
import com.oratakashi.covid19.utils.Utility
import kotlinx.android.synthetic.main.activity_hotline.*


class HotlineActivity : AppCompatActivity(), HotlineInterface {

    val data : MutableList<DataHotline> = ArrayList()

    val viewModel : HotlineViewModel by lazy {
        ViewModelProviders.of(this).get(HotlineViewModel::class.java)
    }

    val adapter : HotlineAdapter by lazy {
        HotlineAdapter(data, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotline)
        ButterKnife.bind(this)

        supportActionBar!!.hide()

        rvHotline.adapter = adapter
        rvHotline.layoutManager = LinearLayoutManager(this)

        viewModel.cache.observe(this, Observer { cache ->
            cache?.let{
                data.clear()
                data.addAll(it)
                adapter.notifyDataSetChanged()
                when(data.isEmpty()){
                    true -> {
                        llEmpty.visibility = View.VISIBLE
                        rvHotline.visibility = View.GONE
                    }
                    false -> {
                        llEmpty.visibility = View.GONE
                        rvHotline.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.setupInstantSearch(etSearch)

        when(viewModel.checkCache()){
            true    -> viewModel.cacheData(viewModel.getHotline(this))
            false   -> viewModel.getCache()
        }
    }

    override fun onPhone(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
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
