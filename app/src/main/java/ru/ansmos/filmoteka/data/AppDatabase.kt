package ru.ansmos.filmoteka.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.ansmos.filmoteka.data.dao.ITmdbFilmDao
import ru.ansmos.filmoteka.data.entity.FilmEntity
import ru.ansmos.filmoteka.utils.ConverterRoom

@Database(entities = [FilmEntity::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun tmdbFilmDao(): ITmdbFilmDao
}