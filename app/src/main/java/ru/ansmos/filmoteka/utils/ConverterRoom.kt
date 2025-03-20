package ru.ansmos.filmoteka.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.dombuketa.database_module.entity.FilmEntity
import ru.ansmos.filmoteka.bll.Film

object ConverterRoom {

    fun convertliveEntityToFilms(list: LiveData<List<ru.dombuketa.database_module.entity.FilmEntity>>): LiveData<List<Film>> {
        val result = Transformations.map(list){ filmEntityList ->
            val filmList = arrayListOf<Film>()
            filmEntityList.forEach {
                convertEntityToFilm(it)?.let { it1 -> filmList.add(it1) }
            }
            return@map filmList.toList()
        }
        return result
    }

    fun convertFlowEntityToFilms(list: Flow<List<ru.dombuketa.database_module.entity.FilmEntity>>): Flow<List<Film>> {
        val result = list.map {
             filmEntityList ->
                val filmList = arrayListOf<Film>()
                filmEntityList.forEach {
                    convertEntityToFilm(it)?.let { it1 -> filmList.add(it1) }
                }
                return@map filmList.toList()
            }
        return result
    }

    fun convertPagingEntityToFilms(list: DataSource.Factory<Int, FilmEntity>): DataSource.Factory<Int, Film> {
        return list.map {
                convertEntityToFilm(it)
        }
    }


    fun convertRxEntityToFilms(list: Observable<List<ru.dombuketa.database_module.entity.FilmEntity>>): Observable<List<Film>> {
        val result = list.map {
                filmEntityList ->
            val filmList = arrayListOf<Film>()
            filmEntityList.forEach {
                convertEntityToFilm(it)?.let { it1 -> filmList.add(it1) }
            }
            return@map filmList.toList()
        }
        return result
    }

    fun convertEntityToFilms(list: List<ru.dombuketa.database_module.entity.FilmEntity>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(convertEntityToFilm(it))
        }
        return result
    }

    private fun convertEntityToFilm(filmEntity: ru.dombuketa.database_module.entity.FilmEntity): Film {
        return Film(
            id = filmEntity.id_tmdb.toString(),
            title = filmEntity.title,
            poster = filmEntity.poster,
            description = filmEntity.description,
            rating = filmEntity.rating,
            releaseDate = filmEntity.release_date  //LocalDate.parse(it.release_date).year,
        )
    }

    fun convertFilmsToEntity(list: List<Film>?): List<ru.dombuketa.database_module.entity.FilmEntity> {
        val result = mutableListOf<ru.dombuketa.database_module.entity.FilmEntity>()
        list?.forEach {
            result.add( convertFilmToEntity(it))
        }
        return result
    }

    fun convertFilmToEntity(film: Film): ru.dombuketa.database_module.entity.FilmEntity {
        return ru.dombuketa.database_module.entity.FilmEntity(
            id = 0,
            id_tmdb = film.id.toInt(),
            title = film.title,
            poster = film.poster,
            description = film.description,
            rating = film.rating,
            release_date = film.releaseDate
        )
    }
}