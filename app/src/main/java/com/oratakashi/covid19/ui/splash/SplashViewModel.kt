package com.oratakashi.covid19.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.version.ResponseVersion
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SplashViewModel : ViewModel() {
    val responseUpdate = MutableLiveData<ResponseVersion>()
    val errorUpdate = MutableLiveData<Boolean>()

    fun cekUpdate(){
        App.disposable!!.add(
            App.orata!!.getVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseVersion>() {
                    override fun onSuccess(t: ResponseVersion) {
                        errorUpdate.value = false
                        responseUpdate.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorUpdate.value = true
                        if(BuildConfig.DEBUG) Log.e("Splash", e.message)
                    }
                })
        )
    }
}