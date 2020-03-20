package com.oratakashi.covid19.ui.death

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DeathViewModel : ViewModel() {
    val progressDeath = MutableLiveData<Boolean>()
    val responseDeath = MutableLiveData<List<DataDeath>>()
    val errorDeath = MutableLiveData<Boolean>()

    val showMessage = MutableLiveData<String>()

    fun getDeath(){
        progressDeath.value = true
        App.disposable!!.add(
            App.service!!.getDeath()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DataDeath>>() {
                    override fun onSuccess(t: List<DataDeath>) {
                        progressDeath.value = false
                        errorDeath.value = false
                        responseDeath.value = t
                    }

                    override fun onError(e: Throwable) {
                        progressDeath.value = false
                        errorDeath.value = true
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
    }
}