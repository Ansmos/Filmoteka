package ru.ansmos.filmoteka.utils

import ru.ansmos.filmoteka.bll.Film

object ConverterTmdb {
    fun convertApiListToDtoList(list: List<ru.dombuketa.net_tmdb.imp.TmdbFilmDTO>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    id = it.id.toString(),
                    title = it.title,
                    //poster = ru.dombuketa.net_tmdb.ApiConstants.IMAGES_URL_TMDB + "w780" + it.posterPath,
                    poster = it.posterPath,
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