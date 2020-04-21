package com.oratakashi.covid19.ui.main.v2

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.oratakashi.covid19.R
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.ui.confirm.ConfirmFragment
import com.oratakashi.covid19.ui.main.MainInterfaces
import com.oratakashi.covid19.ui.statistik.StatistikFragment
import dagger.android.support.DaggerAppCompatActivity

import kotlinx.android.synthetic.main.activity_global_detail.*
import kotlinx.android.synthetic.main.bottomsheet_v2.*

class GlobalDetailActivity : DaggerAppCompatActivity(), OnMapReadyCallback,
    MainInterfaces {
    private lateinit var mMap: GoogleMap
    lateinit var bsHome : BottomSheetBehavior<LinearLayout>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_detail)

        ButterKnife.bind(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.frHome) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bsHome = BottomSheetBehavior.from(bottom_sheet)

        openFragment(StatistikFragment(this), "statistik")
    }

    fun openFragment(fragment : Fragment, name : String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHome, fragment, name)
            .commit()
    }

    fun navBarEvent(selected : String){
        tvOverview.typeface = ResourcesCompat.getFont(applicationContext, R.font.regular)
        tvOverview.setTextColor(ContextCompat.getColor(applicationContext, R.color.grey))
        vOverview.visibility = View.INVISIBLE
        tvDetail.typeface = ResourcesCompat.getFont(applicationContext, R.font.regular)
        tvDetail.setTextColor(ContextCompat.getColor(applicationContext, R.color.grey))
        vDetail.visibility = View.INVISIBLE

        when(selected){
            "overview" -> {
                tvOverview.typeface = ResourcesCompat.getFont(applicationContext, R.font.bold)
                tvOverview.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                vOverview.visibility = View.VISIBLE
            }
            "detail" -> {
                tvDetail.typeface = ResourcesCompat.getFont(applicationContext, R.font.bold)
                tvDetail.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                vDetail.visibility = View.VISIBLE
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!

        try {
            val success: Boolean = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json
                )
            )
            if (!success) {
                Log.e("Log", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Log", "Can't find style. Error: ", e)
        }

        val indonesia = LatLng(-0.7893, 113.9213)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indonesia))
    }

    override fun resultStatistik(data: ResponseStatistik) {
        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-0.7893, 113.9213), 1f))
        bsHome.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun resultConfirmed(data: List<DataConfirm>) {
        bsHome.state = BottomSheetBehavior.STATE_COLLAPSED
        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-0.7893, 113.9213), 1f))
        var i = 0
        data.forEach {
            i++
            if(it.lat != null && it.long != null) mMap.addMarker(
                MarkerOptions().position(
                LatLng(
                    it.lat.toDouble(),
                    it.long.toDouble()
                )
            ).title(
                "${
                when(it.provinceState != null){
                    true -> {
                        "${it.provinceState}, ${it.countryRegion}"
                    }
                    false -> {
                        it.countryRegion
                    }
                }
                }"
            )).snippet = "Confirmed : ${it.confirmed} Orang"
        }
    }

    override fun resultRecovered(data: List<DataRecovered>) {

    }

    override fun resultDeath(data: List<DataDeath>) {

    }

    override fun resultProvince(data: List<DataProvince>) {

    }

    override fun getLocation(lat: Double, lng: Double, zoom: Float) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoom))
        bsHome.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onFocus(focus: Boolean) {

    }

    @OnClick(R.id.llOverview) fun onOverview(){
        navBarEvent("overview")
        openFragment(StatistikFragment(this), "statistik")
    }

    @OnClick(R.id.llDetail) fun onDetail(){
        navBarEvent("detail")
        openFragment(ConfirmFragment(this), "confirm")
    }

    @OnClick(R.id.fab) fun onBack(){
        finish()
    }
}
