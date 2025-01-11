package ru.ansmos.filmoteka.data.dao

import androidx.room.*
import ru.ansmos.filmoteka.data.entity.FilmEntity
import ru.ansmos.filmoteka.db.Film
import ru.ansmos.filmoteka.utils.ConverterRoom

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao


interface ITmdbFilmDao {
    //Запрос на всю таблицу постранично
    //@Query("SELECT * FROM cached_films(:page)")
    @Query("SELECT * FROM cached_films LIMIT (:pageSize) OFFSET (:pageIndex)")
    @TypeConverters(ConverterRoom::class)
    fun getFilms(pageIndex : Int, pageSize: Int): List<FilmEntity>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<FilmEntity>)

    @Query("DELETE FROM cached_films")
    fun clearAll() : Int
}