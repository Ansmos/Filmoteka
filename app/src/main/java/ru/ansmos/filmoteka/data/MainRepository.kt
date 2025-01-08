package ru.ansmos.filmoteka.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import ru.ansmos.filmoteka.R
import ru.ansmos.filmoteka.db.Film
import javax.inject.Inject

class MainRepository(databaseHelper: DatabaseHelper) {
    //Инициализируем объект для взаимодействия с БД
    private val sqlDb = databaseHelper.readableDatabase
    //Создаем курсор для обработки запросов из БД
    private lateinit var cursor: Cursor

    fun putToDB(film: Film) {
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COL_TITLE, film.title)
            put(DatabaseHelper.COL_POSTER, film.poster)
            put(DatabaseHelper.COL_DESC, film.description)
            put(DatabaseHelper.COL_RATING, film.rating)
        }
        //Кладем фильм в БД
        sqlDb.insert(DatabaseHelper.TABLE_NAME, null, cv)    }

    @SuppressLint("Range")
    fun getAllFromDB(): List<Film> {
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        //Сюда будем сохранять результат получения данных
        val result = mutableListOf<Film>()
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()) {
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE))
                val poster = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_POSTER))
                val description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESC))
                val rating = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_RATING))

                result.add(Film(id = "", title = title, poster =  poster, description =  description, rating = rating))
            } while (cursor.moveToNext())
        }
        //Возвращаем список фильмов
        return result
    }

    fun clearAll(){
        sqlDb.delete(DatabaseHelper.TABLE_NAME, "", null)
    }
}