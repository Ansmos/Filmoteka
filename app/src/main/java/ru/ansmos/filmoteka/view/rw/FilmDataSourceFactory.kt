package ru.ansmos.filmoteka.view.rw
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import io.reactivex.rxjava3.core.Observable
//import io.reactivex.rxkotlin.Observables
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.db.Film

class FilmDataSourceFactory(private val filmStorage: FakeRepo) : DataSource.Factory<Int, Film>() {

    //val sourceLiveData = MutableLiveData<FilmPositionalDataSource>()
    //val sourceLiveDataRx : Observable<FilmPositionalDataSource>

    override fun create(): DataSource<Int, Film> {
        //PageListConfig
        val config1 = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()
        val latestSource = FilmPositionalDataSource(filmStorage).apply {
       //     Config()
        }

        //sourceLiveData.postValue(latestSource)
        return latestSource
    }
}


