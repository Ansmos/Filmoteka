package ru.dombuketa.database_module.dao

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.database_module.entity.FilmEntity

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao
interface ITmdbFilmDao {
    //Запрос на всю таблицу постранично
    @Query("SELECT * FROM cached_films LIMIT (:pageSize) OFFSET (:pageIndex * 10)")
    fun getFilmsByPage(pageIndex : Int, pageSize: Int): Observable<List<FilmEntity>>

    @Query("SELECT * FROM cached_films LIMIT (:pageSize) OFFSET (:pageIndex * 10)")
    fun getFilmsByPage_Paging(pageIndex : Int, pageSize: Int): DataSource.Factory<Int, FilmEntity>

    @Query("SELECT * FROM cached_films")
    fun getFilms_Paging(): DataSource.Factory<Int, FilmEntity>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<FilmEntity>)

    @Query("DELETE FROM cached_films")
    fun clearAll() : Int
}

