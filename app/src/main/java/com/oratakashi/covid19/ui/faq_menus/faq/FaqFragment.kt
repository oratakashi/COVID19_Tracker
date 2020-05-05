package com.oratakashi.covid19.ui.faq_menus.faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.faq.DataFaq
import com.oratakashi.covid19.utils.AssetsManager
import kotlinx.android.synthetic.main.fragment_faq.*

/**
 * A simple [Fragment] subclass.
 */
class FaqFragment : Fragment() {

    val data : MutableList<DataFaq> = ArrayList()

    val viewModel: FaqViewModel by lazy {
        ViewModelProviders.of(this).get(FaqViewModel::class.java)
    }

    val adapter: FaqAdapter by lazy {
        FaqAdapter(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFaq.adapter = adapter
        rvFaq.layoutManager = LinearLayoutManager(context)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.dataFaq.observe(viewLifecycleOwner, Observer { faq ->
            faq?.let{
                data.clear(); data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getFaq(requireContext())
    }
}
