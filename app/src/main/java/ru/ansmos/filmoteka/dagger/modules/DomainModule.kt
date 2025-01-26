package ru.ansmos.filmoteka.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.IThemoviedbApi
import ru.ansmos.filmoteka.domain.InteractorTmdb
import ru.ansmos.filmoteka.utils.PreferenceProvider
import javax.inject.Named
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
    fun provideInteractorTmdb(repository: MainRepository, tmdbApi: IThemoviedbApi, preferences: PreferenceProvider)
            = InteractorTmdb(repository, tmdbApi, preferences )

}