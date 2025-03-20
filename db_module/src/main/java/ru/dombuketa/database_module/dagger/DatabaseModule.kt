package ru.dombuketa.database_module.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.dombuketa.database_module.dao.ITmdbFilmDao
import ru.dombuketa.database_module.db.AppDatabase
import ru.dombuketa.database_module.db.DatabaseHelper
import ru.dombuketa.database_module.repositories.MainRepository
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
    fun provideRepository(filmDao: ITmdbFilmDao) =
        MainRepository(filmDao)
}

