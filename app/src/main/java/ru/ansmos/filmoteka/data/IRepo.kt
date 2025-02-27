package ru.ansmos.filmoteka.data

import androidx.paging.DataSource
import ru.ansmos.filmoteka.db.Film

interface IRepo {
    fun getData(startPosition :Int, loadSize : Int) : MutableList<Film> //DataSource.Factory<Int, Film>
}