package ru.ansmos.filmoteka.dagger.modules

import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.IOmdbApi
import ru.ansmos.filmoteka.domain.Interactor
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, omdbApi: IOmdbApi) = Interactor(repository,omdbApi)
}