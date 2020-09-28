package br.eloibrito.com.movie_db.utils

import android.app.Application
import br.eloibrito.com.movie_db.di.component.AppComponent
import br.eloibrito.com.movie_db.di.component.DaggerAppComponent
import br.eloibrito.com.movie_db.di.module.RetrofitModule

class App : Application() {

    companion object {
        lateinit var mAppComponent: AppComponent

        fun getComponent(): AppComponent {
            return mAppComponent;
        }

    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    fun initializeDagger() {
        mAppComponent = DaggerAppComponent.builder()
            .retrofitModule(RetrofitModule())
            .build()
    }

}