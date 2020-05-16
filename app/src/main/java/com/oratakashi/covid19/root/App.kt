package com.oratakashi.covid19.root

import com.oratakashi.covid19.data.db.Database
import com.oratakashi.covid19.data.db.QueryBuilder
import com.oratakashi.covid19.data.db.Sessions
import com.oratakashi.covid19.data.network.ApiBNPB
import com.oratakashi.covid19.data.network.ApiOrata
import com.oratakashi.covid19.data.network.ApiService
import dagger.android.*
import io.reactivex.disposables.CompositeDisposable


class App : DaggerApplication() {
    /**
     * Tempat Deklarasi Retrofit agar bisa di pakai di seluruh aplikasi tanpa deklarasi ulang
     */
    companion object{
//        var service : ApiService?= null
//        var bnpb : ApiBNPB ?= null
//        var orata : ApiOrata ?= null
        var builder : QueryBuilder ?= null
        var disposable : CompositeDisposable?= null
        var db : Database ?= null
        var sessions : Sessions ?= null
    }

    override fun onCreate() {
        super.onCreate()
//        service = ApiService()
//        bnpb = ApiBNPB()
//        orata = ApiOrata()
        disposable = CompositeDisposable()
        db = Database(this)
        sessions = Sessions(this)

        builder = object : QueryBuilder() {}
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.create().apply { inject(this@App) }
    }
}