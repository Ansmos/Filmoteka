package ru.ansmos.filmoteka.utils

import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.db.OmdbFilmDTO
import ru.ansmos.filmoteka.db.OmdbFilmListDTO

object Converter {
    fun ConverterToFilmList(listApi: List<OmdbFilmDTO>?): List<Film> {
        val result = mutableListOf<Film>()
        listApi?.forEach {
            result.add(Film(
                id = it.id,
                title = it.title,
                poster = it.poster,
                description = "Кратткое описание. В Api в списке его нет, нужно выдергивать как-то по каджому элементу списка отдельным запросом. Заодно и вопрос к ментору: Как это сделать ????",
                rating = ((1..10).random() / (1..100).random()).toFloat(),
                year = it.year,
                isInFavorites = false
            ))
        }
        return result
    }
}