package ru.ansmos.filmoteka.domain

import ru.ansmos.filmoteka.data.MainRepository
import ru.ansmos.filmoteka.db.Film

class Interactor(val repo: MainRepository) {
    fun getFilmsDB(): List<Film> = repo.filmsDataBase
}