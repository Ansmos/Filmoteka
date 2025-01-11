package ru.ansmos.filmoteka.utils

import androidx.room.TypeConverter
import ru.ansmos.filmoteka.data.entity.FilmEntity
import ru.ansmos.filmoteka.db.ApiConstants
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.db.TmdbFilmDTO
import java.time.LocalDate

object ConverterRoom {
    @TypeConverter
    fun convertEntityToFilms(list: List<FilmEntity>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    id = it.id_tmdb.toString(),
                    title = it.title,
                    poster = it.poster,
                    description = it.description,
                    rating = it.rating,
                    releaseDate = it.release_date  //LocalDate.parse(it.release_date).year,

                    //isInFavorites = false
                )
            )
        }
        return result
    }
    @TypeConverter
    fun convertFilmsToEntity(list: List<Film>?): List<FilmEntity> {
        val result = mutableListOf<FilmEntity>()
        list?.forEach {
            result.add(
                FilmEntity(
                    id = 0,
                    id_tmdb = it.id.toInt(),
                    title = it.title,
                    poster = it.poster,
                    description = it.description,
                    rating = it.rating,
                    release_date = it.releaseDate
                    //isInFavorites = false
                )
            )
        }
        return result
    }

}