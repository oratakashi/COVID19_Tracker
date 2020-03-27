package com.oratakashi.covid19.ui.splash

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.ui.main.MainActivity
import com.oratakashi.covid19.ui.timeline.TimelineActivity
import com.oratakashi.covid19.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvVersion.text = "Version : ${BuildConfig.VERSION_NAME}"

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        NetworkUtils.checkConnectivity(this, object : NetworkUtils.NetworkUtilCallback {
            override fun onSuccess() {
                viewModel.cekUpdate()
            }

            override fun onCancel() {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        setupViewModel()
    }

    fun setupViewModel(){
        viewModel.errorUpdate.observe(this, Observer { error ->
            error?.let{
                if(it) AlertDialog.Builder(this)
                    .setTitle("Oopss....")
                    .setMessage("Gagal melakukan validasi aplikasi ke server oratakashi!, " +
                            "mohon periksa kembali jaringan anda!")
                    .setCancelable(false)
                    .setPositiveButton("Ya"){dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        })
        viewModel.responseUpdate.observe(this, Observer { response ->
            response?.let{
               if(it.status){
                   startActivity(Intent(applicationContext, MainActivity::class.java))
                   finish()
               }else{
                   AlertDialog.Builder(this)
                       .setTitle("Versi baru telah di rilis!")
                       .setMessage("Changelog : \n\n*${it.data.changelog.replace("|", "\n*")}\n" +
                               "\nDirilis pada : ${it.data.updated_at}\n\nVersion : ${it.data.version_char}")
                       .setCancelable(false)
                       .setPositiveButton("Download Official"){ dialog, _ ->
                           val i = Intent(Intent.ACTION_VIEW)
                           i.data = Uri.parse(it.data.link_official)
                           startActivity(i)
                           finish()
                           dialog.dismiss()
                       }
                       .setNegativeButton("Download Mirror"){ dialog, _ ->
                           val i = Intent(Intent.ACTION_VIEW)
                           i.data = Uri.parse(it.data.link_mirror)
                           startActivity(i)
                           finish()
                           dialog.dismiss()
                       }
                       .setNeutralButton("Ingatkan Nanti"){ dialog, _ ->
                           startActivity(Intent(applicationContext, MainActivity::class.java))
                           finish()
                           dialog.dismiss()
                       }.show()
               }
            }
        })
    }
}
