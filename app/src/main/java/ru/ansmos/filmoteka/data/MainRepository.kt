package ru.ansmos.filmoteka.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import ru.ansmos.filmoteka.data.dao.ITmdbFilmDao
import ru.ansmos.filmoteka.data.entity.FilmEntity
import ru.ansmos.filmoteka.db.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: ITmdbFilmDao)  {

    fun putFilms(films: List<FilmEntity>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getFilms(pageIndex: Int, pageSize: Int): Observable<List<FilmEntity>> {
        return filmDao.getFilmsByPage(pageIndex, pageSize)
    }

    fun getFilmsPaging(): DataSource.Factory<Int, FilmEntity> {
        return filmDao.getFilms_Paging()
    }


    fun clearAllFilms() : Int {
        var deletedItemsCount : Int = 0
        Executors.newSingleThreadExecutor().execute {
             deletedItemsCount = filmDao.clearAll()
        }
        //Омновной поток не ждет другого, поэтому возвращает 0, если через дебаг, правильно. Как сделать возврат?
        return deletedItemsCount
    }

    fun getDataPDS(startPosition: Int, loadSize: Int): DataSource.Factory<Int, FilmEntity> {
        return filmDao.getFilmsByPage_Paging(startPosition, loadSize)
    }
}