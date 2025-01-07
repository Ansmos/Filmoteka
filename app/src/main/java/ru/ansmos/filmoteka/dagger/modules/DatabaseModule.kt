package ru.ansmos.filmoteka.dagger.modules

import dagger.Binds
import dagger.Module
import ru.ansmos.filmoteka.data.IRepo
import javax.inject.Singleton

@Module
abstract class DatabaseModule {
    @Singleton
    @Binds
    abstract fun provideRepository(iRepo: IRepo) : IRepo
}