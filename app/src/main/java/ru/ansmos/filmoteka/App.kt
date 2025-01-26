package ru.ansmos.filmoteka

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.ansmos.filmoteka.dagger.AppComponent
import ru.ansmos.filmoteka.dagger.DaggerAppComponent
import ru.ansmos.filmoteka.dagger.modules.DatabaseModule
import ru.ansmos.filmoteka.dagger.modules.DomainModule
import ru.ansmos.filmoteka.dagger.modules.RemoteModuleTmdb
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.ApiConstants
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
   //         .databaseModule(DatabaseModule())
   //         .remoteModule(RemoteModule())
   //         .remoteModuleTmdb(RemoteModuleTmdb())
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