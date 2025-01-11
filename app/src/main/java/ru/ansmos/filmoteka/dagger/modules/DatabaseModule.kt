package ru.ansmos.filmoteka.dagger.modules

import android.content.Context
import android.provider.DocumentsContract.Root
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ansmos.filmoteka.data.AppDatabase
import ru.ansmos.filmoteka.data.DatabaseHelper
import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.data.dao.ITmdbFilmDao
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Singleton
    @Provides
    fun provideFilmDao(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "films.db")
        .build().tmdbFilmDao()

    @Singleton
    @Provides
    fun provideRepository(filmDao: ITmdbFilmDao) = MainRepository(filmDao)
}

