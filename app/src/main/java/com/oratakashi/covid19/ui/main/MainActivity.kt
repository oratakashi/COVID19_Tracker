package com.oratakashi.covid19.ui.main

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.ui.about.AboutFragment
import com.oratakashi.covid19.ui.confirm.ConfirmFragment
import com.oratakashi.covid19.ui.death.DeathFragment
import com.oratakashi.covid19.ui.recovered.RecoveredFragment
import com.oratakashi.covid19.ui.statistik.StatistikFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottomsheet.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, MainInterfaces {

    private lateinit var mMap: GoogleMap
    lateinit var bsHome : BottomSheetBehavior<CardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.frHome) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bsHome = BottomSheetBehavior.from(bottom_sheet)

        openFragment(StatistikFragment(this), "statistik")

        swLive.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> {
                    AlertDialog.Builder(this)
                        .setTitle("Opps....")
                        .setMessage("Maaf fitur Live data belum tersedia, karena server sedang dalam perbaikan!")
                        .setPositiveButton("Tutup"){ dialog, _ ->
                            swLive.isChecked = false
                            dialog.dismiss()
                        }.setCancelable(false).show()
                }
            }
        }
    }

    fun openFragment(fragment : Fragment, name : String){
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHome, fragment, name)
            .commit()

        when(name){
            "statistik" -> {
                tvTitle.text = resources.getString(R.string.title_data_statistik)
            }
            "confirm" -> {
                tvTitle.text = resources.getString(R.string.title_data_confirm)
            }
            "recovered" -> {
                tvTitle.text = resources.getString(R.string.title_data_recovered)
            }
            "death" -> {
                tvTitle.text = resources.getString(R.string.title_data_death)
            }
            "about" -> {
                tvTitle.text = resources.getString(R.string.title_about)
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
        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-0.7893, 113.9213), 1f))
        data.forEach {
            mMap.addMarker(MarkerOptions().position(
                LatLng(
                    it.lat!!.toDouble(),
                    it.long!!.toDouble()
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
        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-0.7893, 113.9213), 1f))
        data.forEach {
            mMap.addMarker(MarkerOptions().position(
                LatLng(
                    it.lat!!.toDouble(),
                    it.long!!.toDouble()
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
            )).snippet = "Recovered : ${it.recovered} Orang"
        }
    }

    override fun resultDeath(data: List<DataDeath>) {
        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-0.7893, 113.9213), 1f))
        data.forEach {
            mMap.addMarker(MarkerOptions().position(
                LatLng(
                    it.lat!!.toDouble(),
                    it.long!!.toDouble()
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
            )).snippet = "Deaths : ${it.deaths} Orang"
        }
    }

    override fun getLocation(lat: Double, lng: Double, zoom : Float) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoom))
        bsHome.state = BottomSheetBehavior.STATE_COLLAPSED
        if(zoom != 5f){
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(
                LatLng(
                    lat,
                    lng
                )
            ).title("Bandung, Indonesia"))
        }
    }

    @OnClick(R.id.btnStatistik) fun onStatistik(){
        openFragment(StatistikFragment(this), "statistik")
    }

    @OnClick(R.id.btnConfirmed) fun onConfirmed(){
        openFragment(ConfirmFragment(this), "confirm")
    }

    @OnClick(R.id.btnRecovered) fun onRecovered(){
        openFragment(RecoveredFragment(this), "recovered")
    }

    @OnClick(R.id.btnDeaths) fun onDeaths(){
        openFragment(DeathFragment(this), "death")
    }

    @OnClick(R.id.btnAbout) fun onAbout(){
        openFragment(AboutFragment(this), "about")
    }
}
