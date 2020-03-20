package com.oratakashi.covid19.ui.statistik

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class StatistikViewModel : ViewModel() {
    val progressStatistik = MutableLiveData<Boolean>()
    val responseStatistik = MutableLiveData<ResponseStatistik>()
    val errorStatistik = MutableLiveData<Boolean>()

    val showMessage = MutableLiveData<String>()

    fun getStatistik(){
        progressStatistik.value = true
        App.disposable!!.add(
            App.service!!.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseStatistik>() {
                    override fun onSuccess(t: ResponseStatistik) {
                        progressStatistik.value = false
                        errorStatistik.value = false
                        responseStatistik.value = t
                    }

                    override fun onError(e: Throwable) {
                        progressStatistik.value = false
                        errorStatistik.value = true
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
    }
}