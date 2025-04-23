package ru.ansmos.filmoteka

import android.app.Application
import android.content.Context
import ru.ansmos.filmoteka.dagger.AppComponent
import ru.ansmos.filmoteka.dagger.DaggerAppComponent
import ru.ansmos.filmoteka.dagger.DomainModule
import ru.ansmos.filmoteka.services.NotificationHelper
import ru.dombuketa.database_module.dagger.DaggerIDatabaseComponent
import ru.dombuketa.database_module.dagger.IContextProvider
import ru.dombuketa.net_tmdb.dagger.DaggerITmdbComponent
import java.util.*

class App : Application(), IContextProvider {
    lateinit var repo: ru.dombuketa.database_module.repositories.MainRepository
    lateinit var dagger: AppComponent
    var isPromoShow = false

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        val tmdbProvider = DaggerITmdbComponent.create()
        val databaseProvider = DaggerIDatabaseComponent.builder().iContextProvider(provideContext() as IContextProvider).build()
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
   //         .databaseModule(DatabaseModule())
   //         .remoteModule(RemoteModule())
   //         .remoteModuleTmdb(RemoteModuleTmdb())
            .iTmdbProvider(tmdbProvider)
            .iDatabaseProvider(databaseProvider)
            .domainModule(DomainModule(this))
            .build()
        //Создаем канал
        NotificationHelper.createChannel(this)
        //52 Запомним последний запуск
        dagger.getInteractor().setStartAppTimeToPreferences(Date().time)  //52*
    }

    companion object{
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
        //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
        private set
    }

    override fun provideContext(): Context = this
}