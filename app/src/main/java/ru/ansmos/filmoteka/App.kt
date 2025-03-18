package ru.ansmos.filmoteka

import android.app.Application
import ru.ansmos.filmoteka.dagger.AppComponent
import ru.ansmos.filmoteka.dagger.DaggerAppComponent
import ru.ansmos.filmoteka.dagger.modules.DomainModule
import ru.ansmos.filmoteka.data.MainRepository
import ru.dombuketa.net_tmdb.dagger.DaggerITmdbComponent

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        val tmdbProvider = DaggerITmdbComponent.create()
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
   //         .databaseModule(DatabaseModule())
   //         .remoteModule(RemoteModule())
   //         .remoteModuleTmdb(RemoteModuleTmdb())
            .iTmdbProvider(tmdbProvider)
            .domainModule(DomainModule(this))
            .build()
    }

    companion object{
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
        //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
        private set
    }
}