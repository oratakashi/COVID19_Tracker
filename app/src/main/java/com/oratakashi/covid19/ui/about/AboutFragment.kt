package com.oratakashi.covid19.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oratakashi.covid19.BuildConfig

import com.oratakashi.covid19.R
import com.oratakashi.covid19.ui.main.MainInterfaces
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment(val parent : MainInterfaces) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parent.getLocation(-6.9201766, 107.6049431, 12f)

        tvSource.text = BuildConfig.BASE_URL
    }
}
