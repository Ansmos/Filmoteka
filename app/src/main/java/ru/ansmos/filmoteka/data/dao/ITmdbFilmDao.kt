package ru.ansmos.filmoteka.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.*
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import ru.ansmos.filmoteka.data.FakeRepo
import ru.ansmos.filmoteka.data.entity.FilmEntity
import ru.ansmos.filmoteka.db.Film

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao
interface ITmdbFilmDao {
    //Запрос на всю таблицу постранично
    @Query("SELECT * FROM cached_films LIMIT (:pageSize) OFFSET (:pageIndex * 10)")
    fun getFilmsByPage(pageIndex : Int, pageSize: Int): Observable<List<FilmEntity>>

//    @Query("SELECT * FROM cached_films")
//    fun getFilmsByPage_Paging(): DataSource<Int, FilmEntity>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<FilmEntity>)

    @Query("DELETE FROM cached_films")
    fun clearAll() : Int
}

