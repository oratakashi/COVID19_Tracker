package com.oratakashi.covid19.ui.main.v2

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.ui.about.AboutFragment
import com.oratakashi.covid19.ui.faq_menus.FaqMenusFragment
import com.oratakashi.covid19.ui.home.HomeFragment
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.timeline.TimelineFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : DaggerAppCompatActivity(),
    MainInterfaces {

    var tab_position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

//        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        openFragment(HomeFragment(), "home")

        bnMenu.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    openFragment(HomeFragment(), "home")
                    true
                }
                R.id.navigation_timeline -> {
                    openFragment(TimelineFragment(), "timeline")
                    true
                }
                R.id.navigation_faq -> {
                    openFragment(FaqMenusFragment(), "faq")
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

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
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
                flHome.setPadding(0,0,0,0)
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
