package ru.ansmos.filmoteka.utils

import ru.ansmos.filmoteka.bll.Film
import ru.dombuketa.net_tmdb.imp.TmdbFilmDTO

object ConverterTmdb {
    fun convertApiListToDtoList(list: List<TmdbFilmDTO>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add( convertApiToDto(it)
            )
        }
        return result
    }

    fun convertApiToDto(filmDto: TmdbFilmDTO): Film {
        val result = mutableListOf<Film>()
           return Film(
                id = filmDto.id.toString(),
                title = filmDto.title,
                poster = filmDto.posterPath,
                description = filmDto.overview,
                releaseDate =  filmDto.releaseDate, // LocalDate.parse(it.releaseDate).year,
                rating = filmDto.voteAverage,
                isInFavorites = false
            )
    }

}