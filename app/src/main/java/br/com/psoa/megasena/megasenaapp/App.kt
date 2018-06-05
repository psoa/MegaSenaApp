package br.com.psoa.megasena.megasenaapp

import android.app.Application
import com.facebook.stetho.Stetho

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //Access database on browser
        Stetho.initializeWithDefaults(this)
    }

}