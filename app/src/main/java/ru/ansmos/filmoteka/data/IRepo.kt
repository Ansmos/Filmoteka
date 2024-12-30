package ru.ansmos.filmoteka.data

import ru.ansmos.filmoteka.db.Film

interface IRepo {
    val filmsDataBase: List<Film>
}