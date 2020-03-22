package com.oratakashi.covid19.ui.province

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.province.ResponseProvince
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ProvinceViewModel : ViewModel() {
    val progressProvince = MutableLiveData<Boolean>()
    val responseProvince = MutableLiveData<ResponseProvince>()
    val errorProvince = MutableLiveData<Boolean>()

    val showMessage = MutableLiveData<String>()

    fun getData(){
        progressProvince.value = true
        App.disposable!!.add(
            App.bnpb!!.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseProvince>() {
                    override fun onSuccess(t: ResponseProvince) {
                        progressProvince.value = false
                        errorProvince.value = false
                        responseProvince.value = t
                    }

                    override fun onError(e: Throwable) {
                        progressProvince.value = false
                        errorProvince.value = true
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
    }
}