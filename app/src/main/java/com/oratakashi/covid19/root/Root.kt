package com.oratakashi.covid19.root

import android.app.Application
import com.oratakashi.covid19.data.network.ApiService
import io.reactivex.disposables.CompositeDisposable

class App : Application() {
    companion object{
        var service : ApiService?= null
        var disposable : CompositeDisposable?= null
    }
    override fun onCreate() {
        super.onCreate()
        service = ApiService()
        disposable = CompositeDisposable()
    }
}