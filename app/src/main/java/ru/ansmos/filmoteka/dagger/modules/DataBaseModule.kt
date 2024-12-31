package ru.ansmos.filmoteka.dagger.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.IRepo
import ru.ansmos.filmoteka.data.MainRepository
import javax.inject.Singleton

@Module
abstract class DataBaseModule {
    @Singleton
    @Binds
    abstract fun provideRepository(iRepo: IRepo) : IRepo
}