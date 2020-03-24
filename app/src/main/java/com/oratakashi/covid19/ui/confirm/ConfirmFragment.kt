package com.oratakashi.covid19.ui.confirm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.localstorage.DataGlobal
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.sortirdialog.SortDialogFragment
import com.oratakashi.covid19.ui.sortirdialog.SortDialogInterface
import kotlinx.android.synthetic.main.fragment_confirm.*

/**
 * Class UI for Confirm
 */
class ConfirmFragment(val parent : MainInterfaces) : Fragment(), SortDialogInterface {

    lateinit var viewModel: ConfirmViewModel
    lateinit var adapter: ConfirmAdapter

    val data : MutableList<DataGlobal> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(ConfirmViewModel::class.java)
        adapter = ConfirmAdapter(data, parent, context!!)

        rvConfirm.adapter = adapter
        rvConfirm.layoutManager = LinearLayoutManager(context)

        viewModel.setupInstantSearch(etSearch)

        etSearch.setOnFocusChangeListener { v, hasFocus ->
            parent.onFocus(hasFocus)
        }

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
                        llContent.visibility = View.GONE
                    }
                    false -> {
                        llLoading.visibility = View.GONE
                        llContent.visibility = View.VISIBLE
                    }
                }
            }
        })
        viewModel.responseConfirm.observe(this, Observer { response ->
            response?.let{
                parent.resultConfirmed(it)
            }
        })
        viewModel.cacheConfirm.observe(this, Observer { cache ->
            cache?.let{
                data.clear()
                data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getConfirm()
    }

    override fun onSort(option: String) {
        when(option){
            "asc" -> {
                viewModel.getCache(option, Database.confirmed, etSearch.text.toString())
            }
            "desc" -> {
                viewModel.getCache(option, Database.confirmed, etSearch.text.toString())
            }
            "abjad" -> {
                viewModel.getCache("asc", Database.countryRegion, etSearch.text.toString())
            }
        }
    }

    @OnClick(R.id.ivShort) fun onSort(){
        SortDialogFragment.newInstance(this).show(childFragmentManager, "dialog")
    }
}
