package com.oratakashi.covid19.ui.faq_menus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import butterknife.ButterKnife
import butterknife.OnClick

import com.oratakashi.covid19.R
import com.oratakashi.covid19.ui.faq_menus.faq.FaqFragment
import com.oratakashi.covid19.ui.faq_menus.pencegahan.PencegahanFragment
import kotlinx.android.synthetic.main.fragment_faq_menus.*

/**
 * A simple [Fragment] subclass.
 */
class FaqMenusFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq_menus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        openFragment(PencegahanFragment(), "pencegahan")
    }

    fun openFragment(fragment : Fragment, name : String){
        childFragmentManager.beginTransaction()
            .replace(R.id.flHome, fragment, name)
            .commit()
    }

    fun navBarEvent(selected : String){
        tvPencegahan.typeface = ResourcesCompat.getFont(requireContext(), R.font.regular)
        tvPencegahan.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        vPencegahan.visibility = View.INVISIBLE
        tvFaq.typeface = ResourcesCompat.getFont(requireContext(), R.font.regular)
        tvFaq.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        vFaq.visibility = View.INVISIBLE

        when(selected){
            "pencegahan" -> {
                tvPencegahan.typeface = ResourcesCompat.getFont(requireContext(), R.font.bold)
                tvPencegahan.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                vPencegahan.visibility = View.VISIBLE
            }
            "faq" -> {
                tvFaq.typeface = ResourcesCompat.getFont(requireContext(), R.font.bold)
                tvFaq.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                vFaq.visibility = View.VISIBLE
            }
        }
    }

    @OnClick(R.id.llPencegahan) fun onPencegahan(){
        navBarEvent("pencegahan")
        openFragment(PencegahanFragment(), "pencegahan")
    }

    @OnClick(R.id.llFaq) fun onFaq(){
        navBarEvent("faq")
        openFragment(FaqFragment(), "faq")
    }
}
