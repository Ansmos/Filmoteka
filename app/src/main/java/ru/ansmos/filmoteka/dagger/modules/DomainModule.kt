package ru.ansmos.filmoteka.dagger.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.MainRepository
import ru.dombuketa.net_tmdb.api.IThemoviedbApi
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import javax.inject.Singleton

@Module
//Передаем контекст для SharedPreferences через конструктор
class DomainModule(val context: Context) {
    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Singleton
    @Provides
    fun provideContext() = context

    //Создаем экземпляр SharedPreferences
    @Singleton
    @Provides
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractorTmdb(repository: MainRepository, tmdbApi: ru.dombuketa.net_tmdb.api.IThemoviedbApi, preferences: PreferenceProvider)
            = InteractorTmdb(repository, tmdbApi, preferences )

}