package ru.ansmos.filmoteka.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.data.dao.ITmdbFilmDao
import ru.ansmos.filmoteka.data.entity.FilmEntity
import ru.ansmos.filmoteka.db.Film
import java.util.concurrent.Executors
import javax.inject.Inject

class MainRepository(private val filmDao: ITmdbFilmDao) {

    fun putFilmsToDB(films: List<FilmEntity>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getFilmsFromDB(pageIndex: Int, pageSize: Int): List<FilmEntity> = filmDao.getFilms(pageIndex, pageSize)

    fun clearAllFilms() : Int = filmDao.clearAll()
}