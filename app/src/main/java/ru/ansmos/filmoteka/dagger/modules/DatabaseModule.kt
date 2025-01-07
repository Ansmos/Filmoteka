package ru.ansmos.filmoteka.dagger.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.DatabaseHelper
import ru.ansmos.filmoteka.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Singleton
    @Provides
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}