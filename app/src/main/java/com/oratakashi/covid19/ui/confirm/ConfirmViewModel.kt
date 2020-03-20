package com.oratakashi.covid19.ui.confirm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ConfirmViewModel : ViewModel() {
    val progressConfirm = MutableLiveData<Boolean>()
    val responseConfirm = MutableLiveData<List<DataConfirm>>()
    val errorConfirm = MutableLiveData<Boolean>()

    val showMessage = MutableLiveData<String>()

    fun getConfirm(){
        progressConfirm.value = true
        App.disposable!!.add(
            App.service!!.getConfirm()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DataConfirm>>() {
                    override fun onSuccess(t: List<DataConfirm>) {
                        progressConfirm.value = false
                        errorConfirm.value = false
                        responseConfirm.value = t
                    }

                    override fun onError(e: Throwable) {
                        progressConfirm.value = false
                        errorConfirm.value = true
                        if(BuildConfig.DEBUG) showMessage.value = e.message
                    }
                })
        )
    }
}