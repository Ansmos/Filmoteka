package ru.ansmos.filmoteka.view.rw
import androidx.paging.DataSource
//import io.reactivex.rxkotlin.Observables
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.data.IRepo
import ru.ansmos.filmoteka.db.Film

class FilmDataSourceFactory(private val filmStorage: IRepo) : DataSource.Factory<Int, Film>() {

    override fun create(): DataSource<Int, Film> {
        val latestSource = FilmPositionalDataSource(filmStorage)
        return latestSource
    }
}


