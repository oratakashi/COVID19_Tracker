package com.oratakashi.covid19.ui.confirm

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
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.fragment_confirm.*

/**
 * A simple [Fragment] subclass.
 */
class ConfirmFragment(val parent : MainInterfaces) : Fragment() {

    lateinit var viewModel: ConfirmViewModel
    lateinit var adapter: ConfirmAdapter

    val data : MutableList<DataConfirm> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ConfirmViewModel::class.java)
        adapter = ConfirmAdapter(data, parent)

        rvConfirm.adapter = adapter
        rvConfirm.layoutManager = LinearLayoutManager(context)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.showMessage.observe(this, Observer { message ->
            message?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                Log.e("Debug", it)
            }
        })
        viewModel.errorConfirm.observe(this, Observer { error ->
            error?.let{
                if(it) Snackbar.make(clBase, "Gagal memuat data!", Snackbar.LENGTH_SHORT)
                    .setAction("Coba Lagi"){
                        viewModel.getConfirm()
                    }.show()
            }
        })
        viewModel.progressConfirm.observe(this, Observer { progress ->
            progress?.let{
                when(it){
                    true -> {
                        llLoading.visibility = View.VISIBLE
                        rvConfirm.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        rvConfirm.visibility = View.VISIBLE
                    }
                }
            }
        })
        viewModel.responseConfirm.observe(this, Observer { response ->
            response?.let{
                data.clear()
                it.forEach {
                    data.add(it)
                }
                parent.resultConfirmed(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getConfirm()
    }
}
