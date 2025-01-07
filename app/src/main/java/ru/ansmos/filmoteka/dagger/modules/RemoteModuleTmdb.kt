package ru.ansmos.filmoteka.dagger.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ansmos.filmoteka.db.ApiConstants
import ru.ansmos.filmoteka.db.IThemoviedbApi
import javax.inject.Named
import javax.inject.Singleton

@Module
class RemoteModuleTmdb {
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
            .build()

    @Singleton
    @Provides
    fun provideRetrofitService(@Named("TMDB") retrofit: Retrofit): IThemoviedbApi = retrofit.create(IThemoviedbApi::class.java)

}