package ru.ansmos.filmoteka.dagger.modules

import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.MainRepository
import javax.inject.Singleton

@Module
class DataBaseModule {
    @Singleton
    @Provides
    fun provideRepository() = MainRepository()
}