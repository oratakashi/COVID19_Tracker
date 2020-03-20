package com.oratakashi.covid19.ui.death

import android.os.Bundle
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
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.fragment_death.*

/**
 * A simple [Fragment] subclass.
 */
class DeathFragment(val parent : MainInterfaces) : Fragment() {

    lateinit var viewModel: DeathViewModel
    lateinit var adapter: DeathAdapter

    val data : MutableList<DataDeath> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_death, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DeathViewModel::class.java)
        adapter = DeathAdapter(data, parent)

        rvDeath.adapter = adapter
        rvDeath.layoutManager = LinearLayoutManager(context)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.errorDeath.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getDeath()
                    }.show()
            }
        })
        viewModel.progressDeath.observe(this, Observer { progress ->
            progress?.let{
                when(it){
                    true -> {
                        llLoading.visibility = View.VISIBLE
                        rvDeath.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        rvDeath.visibility = View.VISIBLE
                    }
                }
            }
        })
        viewModel.responseDeath.observe(this, Observer { response ->
            response?.let{
                data.clear()
                it.forEach {
                    data.add(it)
                }
                parent.resultDeath(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getDeath()
    }
}
