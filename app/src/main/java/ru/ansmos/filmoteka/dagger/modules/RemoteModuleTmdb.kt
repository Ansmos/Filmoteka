package ru.ansmos.filmoteka.dagger.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.ansmos.filmoteka.BuildConfig
import ru.ansmos.filmoteka.db.ApiConstants
import ru.ansmos.filmoteka.db.IThemoviedbApi
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RemoteModuleTmdb {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        //Создаём кастомный клиент
        return OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()
    }
    @Singleton
    @Named("TMDB")
    @Provides
    fun provideRetrofitTmdb(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ApiConstants.BASE_URL_TMDB)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            //добавляем JavaRx
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideRetrofitService(@Named("TMDB") retrofit: Retrofit): IThemoviedbApi = retrofit.create(IThemoviedbApi::class.java)

}