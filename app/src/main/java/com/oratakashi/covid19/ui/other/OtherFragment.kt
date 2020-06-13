package com.oratakashi.covid19.ui.other

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick

import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.ui.about.AboutActivity
import com.oratakashi.covid19.ui.hospital.HospitalActivity
import com.oratakashi.covid19.ui.hotline.HotlineActivity
import com.oratakashi.covid19.ui.main.v1.MainActivity
import com.oratakashi.covid19.ui.main.v2.SecondaryActivity
import com.oratakashi.covid19.ui.theme.ThemeDialogFragment
import com.oratakashi.covid19.ui.theme.ThemeInterfaces

/**
 * A simple [Fragment] subclass.
 */
class OtherFragment : Fragment(), ThemeInterfaces {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)
    }

    override fun onThemeChanged(theme: String) {
        Log.e("theme", theme)
        if(theme != App.sessions!!.getTheme()){
            Toast.makeText(context, "Theme Changed!", Toast.LENGTH_SHORT).show()
            when(theme){
                "basic" -> {
                    App.sessions!!.putString(Sessions.theme, "basic")
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                }
                "advance" -> {
                    App.sessions!!.putString(Sessions.theme, "advance")
                    startActivity(Intent(context, SecondaryActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    @OnClick(R.id.llSettings) fun onSettings(){
        ThemeDialogFragment.newInstance(this).show(childFragmentManager, "dialog")
    }

    @OnClick(R.id.llAbout) fun onAbout(){
        startActivity(Intent(context, AboutActivity::class.java))
    }

    @OnClick(R.id.llHotline) fun onHotline(){
        startActivity(Intent(context, HotlineActivity::class.java))
    }

    @OnClick(R.id.llHospital) fun onHospital(){
        startActivity(Intent(context, HospitalActivity::class.java))
    }
}
