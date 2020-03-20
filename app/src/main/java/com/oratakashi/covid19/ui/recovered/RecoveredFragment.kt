package com.oratakashi.covid19.ui.recovered

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.fragment_recovered.*

/**
 * A simple [Fragment] subclass.
 */
class RecoveredFragment(val parent : MainInterfaces) : Fragment() {

    lateinit var adapter: RecoveredAdapter
    lateinit var viewModel: RecoveredViewModel

    val data : MutableList<DataRecovered> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recovered, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(RecoveredViewModel::class.java)
        adapter = RecoveredAdapter(data, parent)

        rvRecovered.adapter = adapter
        rvRecovered.layoutManager = LinearLayoutManager(context)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                Log.e("Debug", it)
            }
        })
        viewModel.errorRecovered.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getRecovered()
                    }.show()
            }
        })
        viewModel.progressRecovered.observe(this, Observer { progress ->
            progress?.let{
                when(it){
                    true -> {
                        llLoading.visibility = View.VISIBLE
                        rvRecovered.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        rvRecovered.visibility = View.VISIBLE
                    }
                }
            }
        })
        viewModel.responseRecovered.observe(this, Observer { response ->
            response?.let{
                data.clear()
                it.forEach {
                    data.add(it)
                }
                parent.resultRecovered(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getRecovered()
    }
}
