package com.oratakashi.covid19.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.data.model.contributor.DataContributor
import com.oratakashi.covid19.root.App
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.main.v1.MainActivity
import com.oratakashi.covid19.ui.main.v2.SecondaryActivity
import com.oratakashi.covid19.ui.theme.ThemeDialogFragment
import com.oratakashi.covid19.ui.theme.ThemeInterfaces
import com.oratakashi.covid19.utils.ImageHelper
import kotlinx.android.synthetic.main.fragment_about.*


/**
 * A simple [Fragment] subclass.
 */
class AboutFragment(val parent : MainInterfaces) : Fragment(), AboutInterface, ThemeInterfaces {

    val dataKontributor : MutableList<DataContributor> = ArrayList()

    val adapter : AboutAdapter by lazy {
        AboutAdapter(dataKontributor, this)
    }

    val viewModel : AboutViewModel by lazy {
        ViewModelProviders.of(this).get(AboutViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this, view)

        parent.getLocation(-6.9201766, 107.6049431, 12f)

        tvSource.text = BuildConfig.BASE_URL+"\nhttp://covid19.bnpb.go.id/"

        tvSourceCode.text = "https://github.com/oratakashi/COVID19_Tracker"
        tvSourceCode.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://github.com/oratakashi/COVID19_Tracker")
            startActivity(i)
        }

        rvContributor.adapter = adapter
        rvContributor.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.dataAbout.observe(viewLifecycleOwner, Observer { data ->
            data?.let{
                dataKontributor.clear()
                dataKontributor.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.getContributor(requireContext())
    }

    override fun onKontributor(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
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

    @OnClick(R.id.ivConfig) fun onConfig(){
        ThemeDialogFragment.newInstance(this).show(childFragmentManager, "dialog")
    }
}
