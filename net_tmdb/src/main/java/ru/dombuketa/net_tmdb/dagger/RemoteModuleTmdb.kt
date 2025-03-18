package ru.dombuketa.net_tmdb.dagger

import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dombuketa.net_tmdb.BuildConfig
import ru.dombuketa.net_tmdb.api.IThemoviedbApi
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
            .baseUrl(ru.dombuketa.net_tmdb.ApiConstants.BASE_URL_TMDB)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            //добавляем JavaRx
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideRetrofitService(@Named("TMDB") retrofit: Retrofit): IThemoviedbApi = retrofit.create(IThemoviedbApi::class.java)

}