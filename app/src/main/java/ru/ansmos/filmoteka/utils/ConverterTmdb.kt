package ru.ansmos.filmoteka.utils

import ru.ansmos.filmoteka.db.ApiConstants
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.db.TmdbFilmDTO
import java.time.LocalDate

object ConverterTmdb {
    fun convertApiListToDtoList(list: List<TmdbFilmDTO>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    id = it.id.toString(),
                    title = it.title,
                    poster = ApiConstants.IMAGES_URL_TMDB + "w780" + it.posterPath,
                    description = it.overview,
                    releaseDate =  it.releaseDate, // LocalDate.parse(it.releaseDate).year,
                    rating = it.voteAverage,
                    isInFavorites = false
                )
            )
        }
        return result
    }
}