package com.oratakashi.covid19.ui.main.v2

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.ui.about.AboutFragment
import com.oratakashi.covid19.ui.home.HomeFragment
import com.oratakashi.covid19.ui.main.MainInterfaces
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : DaggerAppCompatActivity(),
    MainInterfaces {

    var tab_position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        openFragment(HomeFragment(), "home")

        bnMenu.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    openFragment(HomeFragment(), "home")
                    true
                }
                R.id.navigation_about -> {
                    openFragment(AboutFragment(this), "about")
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun openFragment(fragment : Fragment, name : String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHome, fragment, name)
            .commit()
        when(name){
            "home" -> {
                tab_position = 0
                flHome.setPadding(0,0,0,0)
            }
            "timeline" -> {
                tab_position = 1
                flHome.setPadding(0,0,0,0)
            }
            "faq" -> {
                tab_position = 2
            }
            "about" -> {
                tab_position = 3
                flHome.setPadding(
                    resources.getDimensionPixelSize(R.dimen.padding_15),
                    resources.getDimensionPixelSize(R.dimen.padding_15),
                    resources.getDimensionPixelSize(R.dimen.padding_15),0)
            }
        }
    }

    override fun resultStatistik(data: ResponseStatistik) {

    }

    override fun resultConfirmed(data: List<DataConfirm>) {

    }

    override fun resultRecovered(data: List<DataRecovered>) {

    }

    override fun resultDeath(data: List<DataDeath>) {

    }

    override fun resultProvince(data: List<DataProvince>) {

    }

    override fun getLocation(lat: Double, lng: Double, zoom: Float) {

    }

    override fun onFocus(focus: Boolean) {

    }
}
