package com.oratakashi.covid19.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.R
import com.oratakashi.covid19.ui.main.MainActivity
import com.oratakashi.covid19.ui.splash.SplashState.Error
import com.oratakashi.covid19.ui.splash.SplashState.Loading
import com.oratakashi.covid19.ui.splash.SplashState.Result
import com.oratakashi.covid19.utils.NetworkUtils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    val viewModel : SplashView by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvVersion.text = "Version : ${BuildConfig.VERSION_NAME}"

        viewModel.state.observe(this, Observer { state ->
            state?.let{
                when(it){
                    is Loading -> {

                    }
                    is Result -> {
                        when(it.data.status){
                            true -> {
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            }
                            false -> {
                                AlertDialog.Builder(this)
                                   .setTitle("Versi baru telah di rilis!")
                                   .setMessage("Changelog : \n\n*${it.data.data.changelog
                                       .replace("|", "\n*")}\n" +
                                           "\nDirilis pada : ${it.data.data.updated_at}\n\nVersion : ${it.data.data.version_char}")
                                   .setCancelable(false)
                                   .setPositiveButton("Download Official"){ dialog, _ ->
                                       val i = Intent(Intent.ACTION_VIEW)
                                       i.data = Uri.parse(it.data.data.link_official)
                                       startActivity(i)
                                       finish()
                                       dialog.dismiss()
                                   }
                                   .setNegativeButton("Download Mirror"){ dialog, _ ->
                                       val i = Intent(Intent.ACTION_VIEW)
                                       i.data = Uri.parse(it.data.data.link_mirror)
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
                    }
                    is Error -> {
                        Toast.makeText(
                            applicationContext,
                            it.error.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        NetworkUtils.checkConnectivity(this, object : NetworkUtils.NetworkUtilCallback {
            override fun onSuccess() {
                viewModel.getData()
            }

            override fun onCancel() {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })
    }
}
