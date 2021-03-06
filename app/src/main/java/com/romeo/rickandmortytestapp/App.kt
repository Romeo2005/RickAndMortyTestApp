package com.romeo.rickandmortytestapp

import androidx.multidex.MultiDexApplication
import com.romeo.character.koin.characterModule
import com.romeo.core.koin.*
import com.romeo.main.koin.mainModule
import com.romeo.sign_up_log_in.koin.signUpLogInModule
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                signUpLogInModule,
                dataModule,
                apiModule,
                constantsModule,
                realmModule,
                mainModule,
                dispatcherModule,
                characterModule,
                globalActionsModule
            )
        }

        Realm.init(this)
    }
}